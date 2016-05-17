package org.eframe.rpcAccess;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import org.eframe.rpcAccess.encypt.algorithm.RSA;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class RSATest {


	/**
	 * 模拟一个请求响应
	 * @param requestStr
	 * @param publicKey
	 * @param privateKey
	 * @throws Exception
	 * @param
	 */
	public static void TestRSA(String requestStr, final String publicKey,
			final String privateKey) throws Exception {
		RSA encoder = new RSA();
		
		//模拟client到server
		//私钥加密
		String encyptedReq = encoder.encryptByPrivateKey(requestStr, privateKey);
		System.err.println("私钥加密后请求：" + encyptedReq);

		//模拟server收到后解密
		//公钥解密
		String sourceReq = encoder.decryptByPublic(encyptedReq, publicKey);
		System.err.println("公钥解密，请求：" + sourceReq);			

		//模拟server加密
		String responseStr = "原封不动响应："+requestStr;
		//公钥加密
		String encyptedResp = encoder.encyptByPublicKey(responseStr, publicKey);
		System.err.println("加密后响应：" + encyptedResp);

		//模拟client收到后解密
		//私钥解密
		String sourceResp = encoder.decryptByPrivate(encyptedResp, privateKey);
		System.err.println("私钥解密，内容：" + sourceResp);			
	}

	public static String RSAClientReq(String source, String privateKey) throws Exception{
		RSA encoder = new RSA();
		String encyptedReq = encoder.encryptByPrivateKey(source, privateKey);
		System.err.println("私钥加密后请求：" + encyptedReq);
		return encyptedReq;
	}
	
	public static String RSAServerResp(String source, String publicKey) throws Exception{
		RSA encoder = new RSA();
		String sourceReq = encoder.decryptByPublic(source, publicKey);
		System.err.println("公钥解密，请求：" + sourceReq);	
		
		//反转
		String responseStr = "处理后的请求："+new StringBuilder(sourceReq).reverse().toString();
		String encyptedResp = encoder.encyptByPublicKey(responseStr, publicKey);
		System.err.println("公钥加密后请求：" + encyptedResp);
		return encyptedResp;
	}
	
	public static void RSAClientGetResp(String encyptedResp, String privateKey) throws Exception{
		RSA encoder = new RSA();
		String sourceResp = encoder.decryptByPrivate(encyptedResp, privateKey);
		System.err.println("私钥解密，内容：" + sourceResp);	
	}
	
	public static void main(String[] args) throws Exception {
		//md5Test("内容", "key");

		//base64Test("原来的内容");
		Map<String, Object> keys = RSA.initKey(1024);
		RSAPublicKey publicKey = (RSAPublicKey) keys.get(RSA.PUBLIC_KEY);
		RSAPrivateKey privateKey = (RSAPrivateKey) keys.get(RSA.PRIVATE_KEY);
		
		String publicKeyStr = Base64.encode(publicKey.getEncoded());
		String privateKeyStr = Base64.encode(privateKey.getEncoded());
		
		//RSATest("悲了个剧", publicKeyStr, privateKeyStr);
		
		String encyptedReq = RSAClientReq("原来的请求",privateKeyStr);
		String encyptedResp = RSAServerResp(encyptedReq, publicKeyStr);
		RSAClientGetResp(encyptedResp, privateKeyStr);
	}

}
