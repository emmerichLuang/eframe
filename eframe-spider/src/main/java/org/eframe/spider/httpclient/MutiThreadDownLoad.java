package org.eframe.spider.httpclient;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

public class MutiThreadDownLoad {
    /**
     * 同时下载的线程数
     */
    private int threadCount;
    /**
     * 服务器请求路径
     */
    private String serverPath;
    /**
     * 本地路径
     */
    private String localPath;
    /**
     * 线程计数同步辅助
     */
    private CountDownLatch latch;
 
    public MutiThreadDownLoad(int threadCount, String serverPath, String localPath, CountDownLatch latch) {
        this.threadCount = threadCount;
        this.serverPath = serverPath;
        this.localPath = localPath;
        this.latch = latch;
    }
 
    public void executeDownLoad() {
    	
        try {
        	CloseableHttpClient client = HttpUtil.getHttpClient(true);
        	
        	HttpGet httpget = new HttpGet(serverPath);
            HttpResponse response = client.execute(httpget);
            HttpEntity entity = response.getEntity();
        	
        	
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                //服务器返回的数据的长度，实际上就是文件的长度,单位是字节
                long length = entity.getContentLength();
                System.out.println("文件总长度:" + length + "字节(B)");
                RandomAccessFile raf = new RandomAccessFile(localPath, "rwd");
                //指定创建的文件的长度
                //raf.setLength(length);
                raf.close();
                //分割文件
                long blockSize = length / threadCount;
                for (int threadId = 1; threadId <= threadCount; threadId++) {
                    //第一个线程下载的开始位置
                	long startIndex = (threadId - 1) * blockSize;
                	long endIndex = startIndex + blockSize - 1;
                    if (threadId == threadCount) {
                        //最后一个线程下载的长度稍微长一点
                        endIndex = length;
                    }
                    System.out.println("线程" + threadId + "下载:" + startIndex + "字节~" + endIndex + "字节");
                    new DownLoadThread(threadId, startIndex, endIndex).start();
                }
 
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
 
    }
 
 
    /**
     * 内部类用于实现下载
     */
    public class DownLoadThread extends Thread {
        /**
         * 线程ID
         */
        private int threadId;
        /**
         * 下载起始位置
         */
        private long startIndex;
        /**
         * 下载结束位置
         */
        private long endIndex;
 
        public DownLoadThread(int threadId, long startIndex, long endIndex) {
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }
 
 
        @Override
        public void run() {
 
            try {
                System.out.println("线程" + threadId + "正在下载...");
                
            	CloseableHttpClient client = HttpUtil.getHttpClient(true);
            	
            	HttpGet httpget = new HttpGet(serverPath);
            	httpget.addHeader("Range", "bytes=" + startIndex + "-" + endIndex);
            	
                HttpResponse response = client.execute(httpget);
                
                int code = response.getStatusLine().getStatusCode();
 
                System.out.println("线程" + threadId + "请求返回code=" + code);
 
                InputStream is = response.getEntity().getContent();//返回资源
                RandomAccessFile raf = new RandomAccessFile(localPath, "rwd");
                //随机写文件的时候从哪个位置开始写
                raf.seek(startIndex);//定位文件
 
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    raf.write(buffer, 0, len);
                }
                is.close();
                raf.close();
                System.out.println("线程" + threadId + "下载完毕");
                //计数值减一
 
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
            	latch.countDown();
            }
        }
    }
}

