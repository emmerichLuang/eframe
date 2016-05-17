package org.eframe.rpcAccess.encypt.behavior.impl;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.eframe.core.exception.BizException;
import org.eframe.rpcAccess.encypt.behavior.IEncoder;

/**
 * 非对称加密
 * @author liangrl
 * @date   2016年5月17日
 *
 */
public class RSA implements IEncoder {
	
	public static final String PUBLIC_KEY = "RSAPublicKey";  
	public static final String PRIVATE_KEY = "RSAPrivateKey";
	
	public static final String KEY_ALGORITHM = "RSA";
	
	//公钥、私钥 本来是byte类型， 存储的时候用base64对公钥、私钥进行了base64编码，才能作为字符串存储
	private BASE64 base64 = new BASE64();
	
    /** 
     * 初始化密钥 
     *  
     * @return 
     * @throws Exception 
     */  
    public static Map<String, Object> initKey() throws Exception {  
        KeyPairGenerator keyPairGen = KeyPairGenerator  
                .getInstance(KEY_ALGORITHM);  
        keyPairGen.initialize(1024);  
  
        KeyPair keyPair = keyPairGen.generateKeyPair();  
        // 公钥  
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
        // 私钥  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
        Map<String, Object> keyMap = new HashMap<String, Object>(2);  
        keyMap.put(PUBLIC_KEY, publicKey);  
        keyMap.put(PRIVATE_KEY, privateKey);  
        return keyMap;  
    }
    
	
	public String encypt(String content, String publicKeyParam) throws Exception {
		byte[] keyBytes = base64.decrypt2Byte(publicKeyParam);
		
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicKey = keyFactory.generatePublic(x509KeySpec);  
  
        // 对数据加密  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
  
        byte[] result = cipher.doFinal(content.getBytes());
        return base64.encypt(result);
	}

	
	public String decrypt(String content, String privateKeyParam) throws Exception {
		byte[] keyBytes = base64.decrypt2Byte(privateKeyParam);
		// 取得私钥  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        
        return new String(cipher.doFinal(base64.decrypt2Byte(content)));
	}


	public String encypt(String content) throws Exception {
		throw new BizException().setMsg("RSA必需秘钥");
	}


	public String decrypt(String content) throws Exception {
		throw new BizException().setMsg("RSA必需秘钥");
	}

}
