package org.eframe.rpcAccess.encypt;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import org.eframe.rpcAccess.encypt.algorithm.BASE64;
import org.eframe.rpcAccess.encypt.algorithm.RSA;

public class RSATest {

	/**
	 * 模拟一个请求响应
	 * 
	 * @param requestStr
	 * @param publicKey
	 * @param privateKey
	 * @throws Exception
	 * @param
	 */
	public static void TestRSA(String requestStr, final String publicKey,
			final String privateKey) throws Exception {
		RSA encoder = new RSA();

		// 模拟client到server
		// 私钥加密
		String encyptedReq = encoder
				.encryptByPrivateKey(requestStr, privateKey);
		System.err.println("私钥加密后请求：" + encyptedReq);

		// 模拟server收到后解密
		// 公钥解密
		String sourceReq = encoder.decryptByPublic(encyptedReq, publicKey);
		System.err.println("公钥解密，请求：" + sourceReq);

		// 模拟server加密
		String responseStr = "原封不动响应：" + requestStr;
		// 公钥加密
		String encyptedResp = encoder.encyptByPublicKey(responseStr, publicKey);
		System.err.println("加密后响应：" + encyptedResp);

		// 模拟client收到后解密
		// 私钥解密
		String sourceResp = encoder.decryptByPrivate(encyptedResp, privateKey);
		System.err.println("私钥解密，内容：" + sourceResp);
	}

	public static String RSAClientReq(String source, String privateKey)
			throws Exception {
		RSA encoder = new RSA();
		String encyptedReq = encoder.encryptByPrivateKey(source, privateKey);
		System.err.println("私钥加密后请求：" + encyptedReq);
		return encyptedReq;
	}

	public static String RSAServerResp(String source, String publicKey)
			throws Exception {
		RSA encoder = new RSA();
		String sourceReq = encoder.decryptByPublic(source, publicKey);
		System.err.println("公钥解密，请求：" + sourceReq);

		// 反转
		String responseStr = "处理后的请求："
				+ new StringBuilder(sourceReq).reverse().toString();
		String encyptedResp = encoder.encyptByPublicKey(responseStr, publicKey);
		System.err.println("公钥加密后请求：" + encyptedResp);
		return encyptedResp;
	}

	public static void RSAClientGetResp(String encyptedResp, String privateKey)
			throws Exception {
		RSA encoder = new RSA();
		String sourceResp = encoder.decryptByPrivate(encyptedResp, privateKey);
		System.err.println("私钥解密，内容：" + sourceResp);
	}

	public static void main(String[] args) throws Exception {
		// md5Test("内容", "key");

		// base64Test("原来的内容");
		//Map<String, Object> keys = RSA.initKey(1024);
		//RSAPublicKey publicKey = (RSAPublicKey) keys.get(RSA.PUBLIC_KEY);
		//RSAPrivateKey privateKey = (RSAPrivateKey) keys.get(RSA.PRIVATE_KEY);

		//BASE64 base64 = new BASE64();
		//String publicKeyStr = base64.encypt(publicKey.getEncoded());
		//String privateKeyStr = base64.encypt(privateKey.getEncoded());

		String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCsfYUCzcT62mmO0ojXYRchdre/PblhfmaSRCidu8D72cgzdfawh6ZpwwBwx+d5Zu54AHU/mwhDA2miZQkWlunejU9GNB9N0A5VdeU69dstG+Lt1WA/+DzDiljyviHQuwUKRg3iRmAJ4/IyzU9XYFwtZIe/vnE5M2AbE6w2u7Y8uQIDAQAB";
		String privateKeyStr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKx9hQLNxPraaY7SiNdhFyF2t789uWF+ZpJEKJ27wPvZyDN19rCHpmnDAHDH53lm7ngAdT+bCEMDaaJlCRaW6d6NT0Y0H03QDlV15Tr12y0b4u3VYD/4PMOKWPK+IdC7BQpGDeJGYAnj8jLNT1dgXC1kh7++cTkzYBsTrDa7tjy5AgMBAAECgYAuLiTQTVrxdkxQiplMYJgaw3gKYXIiYf8AJfNh14ShaPsqm+iB9q9dHXIev8Z/QEmUIztA3jbmCiHb8oTuYVUXaSE0Bm+xgtfBV2ZWXvLHdsG1DC3G2/rao5Dhd8jIFYuSBviiRp1KRtHR9mXqQGDxUvtPbdVDJbUE9tcDW6anIQJBAOwVTeaAR0JM7WsGD61Whm/e9lEHrvlPtNMaUchxpuLLXaPRj4bGKT66qNloqF9PFHwvjlg9t8Jr5sBCu0PwSf0CQQC7CswokO6lqjwFgdp2wJxgxW+gFaP3dcJBxW1jX6+LbWrh3lgR+9o5XVhmsuD8ZOBbZMr9BllkOpPsSef68GxtAkBbhQ7D8qqtOyE2wgzFx6sqJycZ1n6wS4Pv6l4V9GkbtnbAkPaw6pUGqlnWnknXWNK6Kb1m29Ym2qmDktsBxV8JAkAFhQYNVGmWffWTfAeEZZ63xFvM75aNIO6AnmDiA9rcVHhI/hS/Qx1nf/ex4Cl/iUKNr+XFncBZktj2qh+sahfpAkEArTxKVcRuBj99FoUEO6E7jesxgVnqraWLkBEGvwN10j33DponXu86uZnFQ6Kys5qOax3AlfRfRjZ038YY7NHZ5Q==";
		
		// RSATest("悲了个剧", publicKeyStr, privateKeyStr);

		String encyptedReq = RSAClientReq("原来的请求", privateKeyStr);
		String encyptedResp = RSAServerResp(encyptedReq, publicKeyStr);
		RSAClientGetResp(encyptedResp, privateKeyStr);
	}

}
