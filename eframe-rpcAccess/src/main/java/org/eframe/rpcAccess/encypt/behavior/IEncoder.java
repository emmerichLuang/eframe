package org.eframe.rpcAccess.encypt.behavior;

/**
 * 加密接口
 * BASE64、MD5、RSA
 * @author liangrl
 * @date   2016年5月17日
 *
 */
public interface IEncoder {

	/**
	 * 加密
	 * @param content
	 * @return
	 * @param
	 */
	public String encypt(String content) throws Exception;
	
	/**
	 * 解密
	 * @param content
	 * @return
	 * @param
	 */
	public String decrypt(String content) throws Exception;	
	
	/**
	 * 加密
	 * @param content
	 * @param accessKey
	 * @return
	 * @param
	 */
	public String encypt(String content, String key) throws Exception;
	
	/**
	 * 解密
	 * @param content
	 * @param privateKey
	 * @return
	 * @param
	 */
	public String decrypt(String content, String key) throws Exception;
}
