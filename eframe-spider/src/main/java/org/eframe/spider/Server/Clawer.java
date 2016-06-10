package org.eframe.spider.Server;

import java.io.File;
import java.util.HashSet;

import org.apache.http.impl.client.CloseableHttpClient;

public class Clawer {

	/**
	 * 已下载地址
	 */
	private static HashSet<String> DONE_URL = new HashSet<String>();
	
	public Clawer(CloseableHttpClient httpClient, String entranceUrl,
			File folder) {
		super();
		this.httpClient = httpClient;
		this.entranceUrl = entranceUrl;
		this.folder = folder;
	}

	private CloseableHttpClient httpClient;
	private String entranceUrl;	
	private File folder;
	
	//private CountDownLatch cdl = new CountDownLatch(1);
	
	public void exec(){
		//约束，三者都必须不为空
		if(httpClient==null){
			throw new RuntimeException("httpClient 为空！");
		}
		if(entranceUrl==null){
			throw new RuntimeException("入口地址 为空！");
		}
		if(folder==null){
			throw new RuntimeException("存储目录 为空！");
		}		

		
		exec(this.entranceUrl);
	}
	
	private void exec(String url){
		if(DONE_URL.contains(url)){
			return;
		}
		
		if(isPattern(url)){
			//解析，下载
			
			//TODO: 替换文字内容
		}
		
		DONE_URL.add(url);
	}
	
	
	private boolean isPattern(String _url){
		String url = _url.toLowerCase();
		//产品页
		if(url.contains("prlb_") || url.contains("prnav")){
			return true;
		}
		
		//css,js,jpg,png,gif
		if(url.endsWith("css") || url.endsWith("js")){
			return true;
		}
		
		if(url.endsWith("jpg") || url.endsWith("png") || url.endsWith("gif")){
			return true;
		}
		
		return false;
	}
	
}
