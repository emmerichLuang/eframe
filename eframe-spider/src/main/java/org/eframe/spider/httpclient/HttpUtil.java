package org.eframe.spider.httpclient;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.eframe.util.CommonLog;


public class HttpUtil {

	/**
	 * 获取httpClient对象。 这个对象可以支持https。
	 * @param https
	 * @return
	 * @param
	 */
	@SuppressWarnings("deprecation")
	private static CloseableHttpClient getHttpClient(boolean https) {
		if(!https){
			return HttpClients.createDefault();
		}
		
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
		registryBuilder.register("http", new PlainConnectionSocketFactory());
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					return true;
				}
			}).build();
			
			LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			registryBuilder.register("https", sslSF);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		Registry<ConnectionSocketFactory> registry = registryBuilder.build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
		return HttpClientBuilder.create().setConnectionManager(connManager).build();
	}

	/**
	 * get
	 * @param https
	 * @param url
	 * @param path
	 * @param queries
	 * @return
	 * @throws Exception
	 * @param
	 */
	public static String get(boolean https, String url,String path, Map<String, String> queries) throws Exception {
		String responseBody = "";
		CloseableHttpClient httpClient = getHttpClient(https);

		URIBuilder uriBuilder = new URIBuilder()
        .setScheme(https?"https":"http")
        .setHost(url)
        .setPath(path);
		
		if(queries!=null){
			for(String key:queries.keySet()){
				uriBuilder.setParameter(key, queries.get(key));
				//uriBuilder.addParameter(param, value)
			}			
		}
		
		HttpGet httpGet = new HttpGet(uriBuilder.build());
		
		if (Context.SetTimeOut) {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(Context.SocketTimeout)
					.setConnectTimeout(Context.ConnectTimeout).build();//设置请求和传输超时时间
			httpGet.setConfig(requestConfig);
		}
		try {
			//请求数据
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				// do something useful with the response body
				// and ensure it is fully consumed
				responseBody = EntityUtils.toString(entity);
				//EntityUtils.consume(entity);
			} else {
				CommonLog.error("http return status error:" + status);
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (Exception ex) {
			CommonLog.error(ex, ex.getMessage());
		} finally {
			httpClient.close();
		}
		return responseBody;
	}

	/**
	 * 
	 * @param https
	 * @param url
	 * @param path
	 * @param queries
	 * @param params
	 * @return
	 * @throws Exception
	 * @param
	 */
	public static String post(boolean https, String url,String path, 
			Map<String, String> queries, Map<String, String> params) throws Exception {
		String responseBody = "";
		
		CloseableHttpClient httpClient = getHttpClient(https);

		URIBuilder uriBuilder = new URIBuilder()
        .setScheme(https?"https":"http")
        .setHost(url)
        .setPath(path);
		
		for(String key:queries.keySet()){
			uriBuilder.setParameter(key, queries.get(key));
		}
		
		//指定url,和http方式
		HttpPost httpPost = new HttpPost(uriBuilder.build());
		if (Context.SetTimeOut) {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(Context.SocketTimeout)
					.setConnectTimeout(Context.ConnectTimeout).build();//设置请求和传输超时时间
			httpPost.setConfig(requestConfig);
		}
		//添加参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (params != null && params.keySet().size() > 0) {
			Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
				nvps.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
			}
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		//请求数据
		CloseableHttpResponse response = httpClient.execute(httpPost);
		try {
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
			} else {
				CommonLog.error("http return status error:" + response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			CommonLog.error(e,e.getMessage());
		} finally {
			response.close();
		}
		return responseBody;
	}
	
	
	public static String postJson(boolean https, String url, String content) throws Exception {
		String responseBody = "";
		
		CloseableHttpClient httpClient = getHttpClient(https);

		URIBuilder uriBuilder = new URIBuilder()
        .setScheme(https?"https":"http")
        .setHost(url);
		
		//指定url,和http方式
		HttpPost httpPost = new HttpPost(uriBuilder.build());
		if (Context.SetTimeOut) {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(Context.SocketTimeout)
					.setConnectTimeout(Context.ConnectTimeout).build();//设置请求和传输超时时间
			httpPost.setConfig(requestConfig);
		}
		
		StringEntity se = new StringEntity(content, Consts.UTF_8);
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		httpPost.setEntity(se);
		
		//请求数据
		CloseableHttpResponse response = httpClient.execute(httpPost);
		try {
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
			} else {
				CommonLog.error("http return status error:" + response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			CommonLog.error(e, e.getMessage());
		} finally {
			response.close();
		}
		return responseBody;
	}	
}
