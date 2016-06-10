package org.eframe.spider.httpclient;

import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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
				Charset charSet = Charset.forName("utf-8");
				responseBody = EntityUtils.toString(entity,charSet);
			} else {
				CommonLog.error("http return status error:" + status);
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (Exception ex) {
			CommonLog.error(ex, ex.getMessage());
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
		try {
			//请求数据
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				//ContentType contentType = ContentType.getOrDefault(response.getEntity());
				//Charset charSet = contentType.getCharset();
				//
				Charset charSet = Charset.forName("utf-8");
				responseBody = EntityUtils.toString(entity,charSet);
			} else {
				CommonLog.error("http return status error:" + status);
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (Exception ex) {
			CommonLog.error(ex, ex.getMessage());
		} 
		return responseBody;
	}
	
}
