package org.eframe.core.threadPool.cdLatch;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class CDLatchJob implements Runnable {

	public CDLatchJob(CountDownLatch latch, Map<String, String> result, String threadName) {
		super();
		this.latch = latch;
		this.result = result;
		this.threadName = threadName;
	}

	private CountDownLatch latch;
	
	private Map<String, String> result;
	
	private String threadName;

	public void run() {

		result.put(threadName, UUID.randomUUID().toString());
		latch.countDown();
		
		System.err.println(threadName+"执行完毕");
	}
	
	
	public CountDownLatch getLatch() {
		return latch;
	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	public Map<String, String> getResult() {
		return result;
	}

	public void setResult(Map<String, String> result) {
		this.result = result;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

}
