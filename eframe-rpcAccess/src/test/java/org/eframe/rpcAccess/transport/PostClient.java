package org.eframe.rpcAccess.transport;


import io.netty.util.internal.StringUtil;

import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.eframe.util.JacksonUtil;

public class PostClient {
	public static void main(String[] args){
		
	}
	
	public static Object post(String url,Object json){  
        HttpClient client = new DefaultHttpClient();  
        HttpPost post = new HttpPost(url);  
        Object response = null;  
        try {  
            StringEntity s = new StringEntity(JacksonUtil.encode(json));  
            s.setContentEncoding("UTF-8");  
            s.setContentType("application/json");  
            post.setEntity(s);  
              
            HttpResponse res = client.execute(post);  
            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
                HttpEntity entity = res.getEntity();  
                String charset = EntityUtils.getContentCharSet(entity);  
                StringWriter writer = new StringWriter();
                IOUtils.copy(entity.getContent(), writer, charset);
                String theString = writer.toString();
                if(StringUtil.isNullOrEmpty(theString)){
                	return null;
                }
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return response;  
    }  
}
