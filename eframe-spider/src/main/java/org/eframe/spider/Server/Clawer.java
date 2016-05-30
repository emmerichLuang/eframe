package org.eframe.spider.Server;

import java.io.File;

import org.apache.http.impl.client.CloseableHttpClient;

public class Clawer {

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
		
	}
	
}
