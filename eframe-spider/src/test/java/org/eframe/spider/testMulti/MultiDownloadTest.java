package org.eframe.spider.testMulti;

import java.util.concurrent.CountDownLatch;

import org.eframe.spider.httpclient.MutiThreadDownLoad;

public class MultiDownloadTest {
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		//http调试信息
		System.setProperty("javax.net.debug", "all");

		int threadSize = 4;
		String serverPath = "https://github.com/emmerichLuang/solo/archive/master.zip";
		String localPath = "E://master.zip";

		CountDownLatch latch = new CountDownLatch(threadSize);

		MutiThreadDownLoad m = new MutiThreadDownLoad(threadSize, serverPath, localPath, latch);
		long startTime = System.currentTimeMillis();
		try {
			m.executeDownLoad();
			latch.await();// 等待所有的线程执行完毕
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("全部下载结束,共耗时" + (endTime - startTime) / 1000 + "s");
	}
}
