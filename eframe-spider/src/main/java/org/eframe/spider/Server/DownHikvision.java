package org.eframe.spider.Server;

import java.io.File;

import org.apache.http.impl.client.CloseableHttpClient;
import org.eframe.spider.httpclient.HttpUtil;

/**
 * 下载 hikvision的入口
 * @author liangrl
 * @date   2016年5月30日
 *
 */
public class DownHikvision {

	public static final String url = "www.hikvision.com/cn/index.html";
	
	public static final File folder = new File("E:/hikvision");
	
	public static void main(String[] args) throws Exception {
		
		CloseableHttpClient httpClient = HttpUtil.getHttpClient(false);

		try{
			Clawer clawer = new Clawer(httpClient,url,folder);
			clawer.exec();
			
		}finally{
			httpClient.close();
		}
	}

	
}
