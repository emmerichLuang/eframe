package org.eframe.rpcAccess;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import org.eframe.rpcAccess.encypt.EncodeFactory;
import org.eframe.rpcAccess.encypt.behavior.IEncoder;
import org.eframe.rpcAccess.encypt.behavior.impl.RSA;
import org.eframe.rpcAccess.encypt.constant.EnCoderType;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class EncodeTest {

	public static void base64Test(String content) throws Exception {
		IEncoder encoder = EncodeFactory.getEncoder(EnCoderType.BASE64);
		String encyptedContent = encoder.encypt(content);
		System.err.println("加密内容：" + encyptedContent);
		
		String sourceContent = encoder.decrypt(encyptedContent);
		System.err.println("解密内容：" + sourceContent);
		
	}	
	
	public static void md5Test(String content, String key) throws Exception {
		IEncoder encoder = EncodeFactory.getEncoder(EnCoderType.MD5);
		String encyptedContent = encoder.encypt(content, key);
		System.err.println(encyptedContent);
	}

	
	
	public static void RSATest(String content, String publicKey,
			String privateKey) throws Exception {
		IEncoder encoder = EncodeFactory.getEncoder(EnCoderType.RSA);
		String encyptedContent = encoder.encypt(content, publicKey);
		System.err.println("加密内容：" + encyptedContent);

		String sourceContent = encoder.decrypt(encyptedContent, privateKey);
		System.err.println("解密内容：" + sourceContent);
	}

	public static void main(String[] args) throws Exception {
		//md5Test("内容", "key");

		//base64Test("原来的内容");
		Map<String, Object> keys = RSA.initKey();
		RSAPublicKey publicKey = (RSAPublicKey) keys.get(RSA.PUBLIC_KEY);
		RSAPrivateKey privateKey = (RSAPrivateKey) keys.get(RSA.PRIVATE_KEY);
		
		String publicKeyStr = Base64.encode(publicKey.getEncoded());
		String privateKeyStr = Base64.encode(privateKey.getEncoded());
		
		RSATest("悲了个剧", publicKeyStr, privateKeyStr);
	}

}
