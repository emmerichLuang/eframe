package org.eframe.core.threadPool.cdLatch;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo {

	private static int size = 100;
	
	private static CountDownLatch latch = new CountDownLatch(size); 
	
	
	public static void main(String[] args) throws Exception {
		Map<String, String> result = new HashMap<String,String>();
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		for(int i=0;i<size;i++){
			threadPool.execute(new CDLatchJob(latch, result, "线程"+i));
		}
		
		
		latch.await();
		System.err.println("all finished!");
		System.err.println(result);
		threadPool.shutdownNow();
	}

}
