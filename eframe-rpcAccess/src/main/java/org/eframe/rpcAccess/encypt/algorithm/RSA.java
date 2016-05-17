package org.eframe.rpcAccess.encypt.algorithm;

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

/**
 * 非对称加密
 * @author liangrl
 * @date   2016年5月17日
 *
 */
public class RSA {
	
	public static final String PUBLIC_KEY = "RSAPublicKey";  
	public static final String PRIVATE_KEY = "RSAPrivateKey";
	
	public static final String KEY_ALGORITHM = "RSA";
	public static final String RSA_PADDING = "RSA/ECB/PKCS1PADDING";
	
	//公钥、私钥 本来是byte类型， 存储的时候用base64对公钥、私钥进行了base64编码，才能作为字符串存储
	private BASE64 base64 = new BASE64();
	
    /** 
     * 初始化密钥 
     *  
     * @param initSize 初始化大小。 建议1024/ 2048
     * @return 
     * @throws Exception 
     */  
    public static Map<String, Object> initKey(int initSize) throws Exception {  
        KeyPairGenerator keyPairGen = KeyPairGenerator  
                .getInstance(KEY_ALGORITHM);  
        keyPairGen.initialize(initSize);  
  
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
    
	/**
	 * 公钥加密
	 * @param content
	 * @param publicKeyParam
	 * @return
	 * @throws Exception
	 * @param
	 */
	public String encyptByPublicKey(String content, String publicKeyParam) throws Exception {
		byte[] keyBytes = base64.decrypt2Byte(publicKeyParam);
		
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicKey = keyFactory.generatePublic(x509KeySpec);  
  
        // 对数据加密  
        Cipher cipher = Cipher.getInstance(RSA_PADDING);  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
  
        byte[] result = cipher.doFinal(content.getBytes());
        return base64.encypt(result);
	}

	/**
	 * 私钥加密
	 * @param content
	 * @param privateKeyParam
	 * @return
	 * @throws Exception
	 * @param
	 */
    public String encryptByPrivateKey(String content, String privateKeyParam)  
            throws Exception {  
        // 对密钥解密  
        byte[] keyBytes = base64.decrypt2Byte(privateKeyParam);  
  
        // 取得私钥  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);  
  
        // 对数据加密  
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  
        byte[] result = cipher.doFinal(content.getBytes());
        return base64.encypt(result);
    }	
	
	/**
	 * 公钥解密
	 * @param content
	 * @param publicKeyParam
	 * @return
	 * @throws Exception
	 * @param
	 */
	public String decryptByPublic(String content, String publicKeyParam) throws Exception {
		byte[] keyBytes = base64.decrypt2Byte(publicKeyParam);
		// 取得公钥 
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        
        Cipher cipher = Cipher.getInstance(RSA_PADDING);  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        
        return new String(cipher.doFinal(base64.decrypt2Byte(content)));
	}	
	
	/**
	 * 私钥解密
	 * @param content
	 * @param privateKeyParam
	 * @return
	 * @throws Exception
	 * @param
	 */
	public String decryptByPrivate(String content, String privateKeyParam) throws Exception {
		byte[] keyBytes = base64.decrypt2Byte(privateKeyParam);
		// 取得私钥  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        
        Cipher cipher = Cipher.getInstance(RSA_PADDING);  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        
        return new String(cipher.doFinal(base64.decrypt2Byte(content)));
	}

}
