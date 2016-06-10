package org.eframe.spider.Server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.eframe.rpcAccess.encypt.algorithm.MD5;
import org.eframe.spider.httpclient.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Clawer {

	private static MD5 encoder = new MD5();
	
	// 已下载地址 避免地址过长导致比较过慢，对url进行md5指纹存放
	public static volatile ConcurrentSkipListSet<String> DONE_RECORD = new ConcurrentSkipListSet<String>();

	// 待处理，需要解析的
	
	public static volatile ConcurrentLinkedQueue<String> TODO_URL = new ConcurrentLinkedQueue<String>();

	/** 待处理，不需要解析的资源文件。比如说css，js文件 */ 
	public static volatile ConcurrentLinkedQueue<String> TODO_RESOURCES = new ConcurrentLinkedQueue<String>();
	/** 待处理，不需要解析的 图片 */ 
	public static volatile LinkedList<String> TODO_PIC = new LinkedList<String>();
	
	private static ExecutorService taskExecutor = Executors.newFixedThreadPool(4);
	
	public Clawer(CloseableHttpClient httpClient, String entranceUrl, File folder) {
		super();
		this.httpClient = httpClient;
		this.entranceUrl = entranceUrl;
		this.folder = folder;
	}

	private CloseableHttpClient httpClient;
	private String entranceUrl;
	private File folder;


	public void exec() throws Exception {
		// 约束，三者都必须不为空
		if (httpClient == null) {
			throw new RuntimeException("httpClient 为空！");
		}
		if (entranceUrl == null) {
			throw new RuntimeException("入口地址 为空！");
		}
		if (folder == null) {
			throw new RuntimeException("存储目录 为空！");
		}

		execFirst();
		
		while(hasLeft()){
			System.out.println("需要继续执行！剩余任务："+TODO_URL.size());
		}
		System.out.println("执行完毕。");
		taskExecutor.shutdown();
		taskExecutor.awaitTermination(5l, TimeUnit.SECONDS);	
	}

	private boolean hasLeft() throws Exception{
		while(TODO_URL.size()>0){
			String url = TODO_URL.poll();
			try{
				taskExecutor.execute(new DownJob(this, url));
			}catch(Exception ex){
				System.err.println(" 执行出错。放弃执行"+url);
				DONE_RECORD.add(encoder.encypt(url, ""));
				continue;
			}
		}
		Thread.sleep(3000);
		return TODO_URL.size()>0;
	}
	
	public static Document puts(String url, boolean isFirst, boolean downRes) throws Exception{
		Document doc = Jsoup.connect(url).get();
		Elements links = doc.select("a[href]");

		if(isFirst || downRes){
			Elements media = doc.select("[src]");
			Elements imports = doc.select("link[href]");			
			for (Element src : media) {
				String temp = src.absUrl("src");
				if(!isDownPattern(temp)){
					continue;
				}
				
				if(temp.contains("/..")){
					temp = temp.replaceAll("\\.\\.", "");
				}
				
				String tag = src.tagName();
				if("script".equalsIgnoreCase(tag)){
					if (!TODO_RESOURCES.contains(temp)) {
						TODO_RESOURCES.add(temp);
					}				
				}else{
					if (!TODO_PIC.contains(temp)) {
						TODO_PIC.add(temp);
					}
				}
			}
			
			for (Element link : imports) {
				String temp = link.attr("abs:href");
				if(!isDownPattern(temp) && !isPattern(temp)){
					continue;
				}			
				
				if (!TODO_RESOURCES.contains(temp)) {
					TODO_RESOURCES.add(temp);
				}
			}			
		}
		

		for (Element link : links) {
			String temp = link.attr("abs:href");
			if(!Clawer.isDownPattern(temp)){
				continue;
			}
			if(!Clawer.isPattern(temp)){
				continue;
			}
			
			if (!TODO_URL.contains(temp)) {
				TODO_URL.add(temp);
			}
		}		
		
		return doc;
	}
	
	/**
	 * 首次执行
	 * 
	 * @param url
	 * @throws IOException
	 */
	private void execFirst() throws Exception {

		Document doc = puts(this.entranceUrl, true, true);
		
		//
		// 下载
		String path = this.entranceUrl.replaceFirst("http://www.hikvision.com/", "E:/hikvision/");
		FileUtils.writeStringToFile(new File(path), doc.html(), "utf-8");

	}

	// 下载所有待处理的资源
	public synchronized void downResource() throws Exception {
		for (String _url : TODO_PIC) {
			_url = _url.replaceAll("..\\\\", "");
			String path = _url.replaceFirst("http://www.hikvision.com/", "E:/hikvision/");
			File f = new File(path);
			if (!f.exists()) {
				inputstreamtofile(_url, new File(path));

				System.out.println("下载图片：" + _url+"成功。");
			}else{
				System.out.println("下载图片：" + _url +" 失败。因为已存在。");				
			}
		}
		TODO_PIC.clear();
		
		//资源文件， js css等
		for (String _url : TODO_RESOURCES) {
			String path = _url.replaceFirst("http://www.hikvision.com/", "E:/hikvision/");
			File f = new File(path);
			if (!f.exists()) {
				String content = null;
				try{
					content = HttpUtil.get(httpClient, _url);					
				}catch(Exception ex){
					System.err.println(_url+"处理异常。");
					throw new RuntimeException(_url+"处理异常。",ex);
				}
				
				FileUtils.writeStringToFile(new File(path), content, "utf-8");

				System.out.println("下载资源：" + _url+"成功。");
			}else{
				System.out.println("下载资源：" + _url +" 失败。因为已存在。");				
			}
		}
		TODO_RESOURCES.clear();		
	}


	public static boolean isPattern(String _url) {
		String url = _url.toLowerCase();

		if (!_url.contains(".html")) {
			return false;
		}

		// 产品页
		/*if (url.contains("prlb") || url.contains("prnav")) {
			return true;
		}*/

		// 解决方案
		if (url.contains("jjfa") ) {
			return true;
		}		
		
		if (url.contains("news")) {
			return true;
		}				
		
		//
		if (url.contains("prgs_")) {
			return true;
		}		
		
		// css,js,jpg,png,gif
		if (url.endsWith("css") || url.endsWith("js")) {
			return true;
		}

		if (url.endsWith("jpg") || url.endsWith("png") || url.endsWith("gif")) {
			return true;
		}

		return false;
	}

	private static boolean isDownPattern(String url){
		if(!url.contains("hikvision")){
			return false;
		}
		
		if(url.endsWith("js") || url.endsWith("css")){
			return false;
		}
		
		if(url.endsWith("#")){
			return false;
		}
		return true;
	}
	
	public static void inputstreamtofile(String urlString, File file){
		try{
			URL url = new URL(urlString);  
            DataInputStream dataInputStream = new DataInputStream(url.openStream());  
            FileOutputStream fileOutputStream = new FileOutputStream(file);  

            byte[] buffer = new byte[1024];  
            int length;  

            while ((length = dataInputStream.read(buffer)) > 0) {  
                fileOutputStream.write(buffer, 0, length);  
            }  

            dataInputStream.close();  
            fileOutputStream.close();  
		}catch(Exception ex){
			System.err.println("inputstreamtofile :"+urlString +" 下载失败！");
			//throw new RuntimeException("filePath:"+file.getAbsolutePath(),ex);
		}
	}
}
