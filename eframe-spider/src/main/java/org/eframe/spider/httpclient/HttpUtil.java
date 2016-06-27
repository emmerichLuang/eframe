package org.eframe.spider.httpclient;

import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
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
	public static CloseableHttpClient getHttpClient(boolean https) {
		if(!https){
			CloseableHttpClient client = HttpClients.createDefault();
			
			return client;
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
	 * 
	 * @param httpClient
	 * @param https
	 * @param url
	 * @param path
	 * @param queries
	 * @return
	 * @throws Exception
	 * @param
	 */
	public static String get(CloseableHttpClient httpClient, boolean https, String url,String path, Map<String, String> queries) throws Exception {
		String responseBody = "";

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
		
		if (Config.SetTimeOut) {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(Config.SocketTimeout)
					.setConnectTimeout(Config.ConnectTimeout).build();//设置请求和传输超时时间
			httpGet.setConfig(requestConfig);
		}
		try {
			//请求数据
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				//ContentType contentType = ContentType.getOrDefault(response.getEntity());
				//Charset charSet = contentType.getCharset();
				//
				responseBody = EntityUtils.toString(entity,Consts.UTF_8);
			} else {
				CommonLog.error("http return status error:" + status);
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (Exception ex) {
			CommonLog.error(ex, ex.getMessage());
		} 
		return responseBody;
	}
	
	/**
	 * post提交
	 * @param httpClient
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 * @param
	 */
	public static String post(CloseableHttpClient httpClient, String url, Map<String,String> params) throws Exception {
		String responseBody = "";

		HttpPost post = new HttpPost(url);
		
		if (Config.SetTimeOut) {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(Config.SocketTimeout)
					.setConnectTimeout(Config.ConnectTimeout).build();//设置请求和传输超时时间
			post.setConfig(requestConfig);
		}
		post.addHeader("User-Agent", 
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");

		if(params!=null && params.size()>0){
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			for(String key:params.keySet()){
				formparams.add(new BasicNameValuePair(key, params.get(key)));
			}			
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
			post.setEntity(entity);
		}
		
		try {
			//请求数据
			CloseableHttpResponse response = httpClient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				//
				responseBody = EntityUtils.toString(entity,Consts.UTF_8);
			} else {
				CommonLog.error("http return status error:" + status);
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (Exception ex) {
			System.err.println("get请求错误！ url："+url);
			CommonLog.error(ex, "get请求错误！ url："+url);
		} 
		return responseBody;
	}	
	
	
	public static String get(CloseableHttpClient httpClient, String url) throws Exception {
		String responseBody = "";

		HttpGet httpGet = new HttpGet(url);
		
		if (Config.SetTimeOut) {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(Config.SocketTimeout)
					.setConnectTimeout(Config.ConnectTimeout).build();//设置请求和传输超时时间
			httpGet.setConfig(requestConfig);
		}
		httpGet.addHeader("User-Agent", 
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
		
		try {
			//请求数据
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				//ContentType contentType = ContentType.getOrDefault(response.getEntity());
				//Charset charSet = contentType.getCharset();
				//
				responseBody = EntityUtils.toString(entity,Consts.UTF_8);
			} else {
				CommonLog.error("http return status error:" + status);
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (Exception ex) {
			System.err.println("get请求错误！ url："+url);
			CommonLog.error(ex, "get请求错误！ url："+url);
		} 
		return responseBody;
	}
	
}
