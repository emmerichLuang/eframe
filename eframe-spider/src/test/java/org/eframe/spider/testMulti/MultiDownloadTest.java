package org.eframe.spider.testMulti;

import java.util.UUID;

import org.eframe.spider.httpclient.MutiThreadDownLoad;

public class MultiDownloadTest {
	
	static String serverPath = "https://github.com/emmerichLuang/solo/archive/master.zip";
	static String destPath = "E://";
	
	static int threadSize = 4;
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		
		String localPath = destPath+UUID.randomUUID()+".zip";

		MutiThreadDownLoad m = new MutiThreadDownLoad(threadSize, serverPath, localPath);
		long startTime = System.currentTimeMillis();
		m.executeDownLoad();
		long endTime = System.currentTimeMillis();
		System.out.println("全部下载结束,共耗时" + (endTime - startTime) / 1000 + "s");
	}
}
