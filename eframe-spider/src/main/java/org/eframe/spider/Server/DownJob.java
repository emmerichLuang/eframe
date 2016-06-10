package org.eframe.spider.Server;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eframe.rpcAccess.encypt.algorithm.MD5;
import org.jsoup.nodes.Document;

public class DownJob implements Runnable {

	private static MD5 encoder = new MD5();
	
	private Clawer c;
	private String url;
	
	public DownJob(Clawer c, String url){
		this.c = c;
		this.url = url;
	}
	
	public void run() {
		try {
			execLink(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 需要解析的
	 * 
	 * @param url
	 * @throws IOException
	 */
	private void execLink(String url) throws Exception {

		c.downResource();

		if (Clawer.DONE_RECORD.contains(encoder.encypt(url,""))) {
			return;
		}

		Document doc = Clawer.puts(url, false, true);
		// 下载
		String path = url.replaceFirst("http://www.hikvision.com/", "E:/hikvision/");
		File f = new File(path);
		if (!f.exists()) {
			FileUtils.writeStringToFile(f, doc.html(), "utf-8");
			System.out.println("下载链接：" + url);
		}else{
			System.out.println("下载链接：" + url +" 失败。因为已存在。");				
		}			

		Clawer.DONE_RECORD.add(url);
		
		if(Clawer.TODO_URL.size()>0){
			System.out.println("需要继续执行！剩余任务："+Clawer.TODO_URL.size());			
		}

	}
}
