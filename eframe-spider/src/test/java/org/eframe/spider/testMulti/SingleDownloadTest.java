package org.eframe.spider.testMulti;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.eframe.spider.httpclient.HttpUtil;

public class SingleDownloadTest {

	static String serverPath = "https://github.com/emmerichLuang/solo/archive/master.zip";
	static String destPath = "E://";
	
	static int BUFFER_SIZE = 1024*1024*1024;
	
	public static void main(String[] args) throws Exception, IOException {
		//http调试信息  慢很多的
		//System.setProperty("javax.net.debug", "all");
		
		CloseableHttpClient client = HttpUtil.getHttpClient(true);
		try{
	    	HttpGet httpget = new HttpGet(serverPath);
	        HttpResponse response = client.execute(httpget);
	        HttpEntity entity = response.getEntity();
	        
	        System.err.println("总的大小："+entity.getContentLength());
	        
	        InputStream is = entity.getContent();
	        
	        RandomAccessFile raf = new RandomAccessFile(destPath+UUID.randomUUID()+".zip", "rwd");			
	        raf.seek(0);//文件指针设置到最前
            int len = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((len = is.read(buffer)) != -1) {
            	System.err.println(String.format("写到文件. offset:%s, length:%s", 0, len));
                raf.write(buffer, 0, len);//每次写完，文件指针都自动走下去的
            }
            is.close();
            raf.close();
	        
		}finally{
			if(client!=null){
				client.close();
			}
		}

	}

}
