package org.eframe.rpcAccess.icewee;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class RSATester {
	static String publicKey;
	static String privateKey;

	static {
		try {
			Map<String, Object> keyMap = RSAUtils.genKeyPair();
			publicKey = RSAUtils.getPublicKey(keyMap);
			privateKey = RSAUtils.getPrivateKey(keyMap);
			System.err.println("公钥: \n" + publicKey);
			System.err.println("私钥： \n" + privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		//String source = "这是一行没有任何意义的文字，你看完了等于没看，不是吗？";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", "2016-01-11");
		params.put("endTime", "2016-07-11");
		params.put("currentPageNo", 1);
		String jsonStr = JSON.toJSONString(params);
		test(jsonStr);
		//testSign();
	}

	static void test(String source) throws Exception {
		System.err.println("公钥加密——私钥解密");
		System.out.println("\r加密前文字：\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = RSAUtils.encryptByPublicKey(data, publicKey);
		System.out.println("加密后文字：\r\n" + new String(encodedData));
		byte[] decodedData = RSAUtils.decryptByPrivateKey(encodedData,
				privateKey);
		String target = new String(decodedData);
		System.out.println("解密后文字: \r\n" + target);
	}

	static void testSign() throws Exception {
		System.err.println("私钥加密——公钥解密");
		String source = "这是一行测试RSA数字签名的无意义文字";
		System.out.println("原文字：\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = RSAUtils.encryptByPrivateKey(data, privateKey);
		System.out.println("加密后：\r\n" + new String(encodedData));
		byte[] decodedData = RSAUtils
				.decryptByPublicKey(encodedData, publicKey);
		String target = new String(decodedData);
		System.out.println("解密后: \r\n" + target);
		System.err.println("私钥签名——公钥验证签名");
		String sign = RSAUtils.sign(encodedData, privateKey);
		System.err.println("签名:\r" + sign);
		boolean status = RSAUtils.verify(encodedData, publicKey, sign);
		System.err.println("验证结果:\r" + status);
	}
}
