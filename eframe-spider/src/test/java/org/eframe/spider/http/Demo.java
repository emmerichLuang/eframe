package org.eframe.spider.http;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Demo {

	private static Object getHeader(HttpResponse response){
		
		Header[] headers = response.getAllHeaders();
		for(Header h :headers){
			System.err.println(h);
		}
		return null;
	}
	
	private static Charset getCharset(HttpResponse response){
		ContentType contentType = ContentType.getOrDefault(response.getEntity());
		Charset charSet = contentType.getCharset();
		
		return charSet;
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("http://www.baidu.com/");
		CloseableHttpResponse response = httpclient.execute(httpget);
		try {
			HttpEntity entity = response.getEntity();
			Charset charset = getCharset(response);
			
			/*StringWriter writer = new StringWriter();
			IOUtils.copy(entity.getContent(), writer, charset);
			//getHeader(response);
			String theString = writer.toString();
			System.err.println(theString);*/
			
			String content = EntityUtils.toString(entity, charset);
			System.err.println(content);
		} finally {
		    response.close();
		}
	}
}
