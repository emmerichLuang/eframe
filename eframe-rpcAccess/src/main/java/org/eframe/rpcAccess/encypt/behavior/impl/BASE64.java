package org.eframe.rpcAccess.encypt.behavior.impl;

import org.eframe.core.exception.BizException;
import org.eframe.rpcAccess.encypt.behavior.IEncoder;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * md5的方式生成指纹。
 * 生成md5的时候， 是key+content的形式， key作为salt了。
 * @author liangrl
 * @date   2016年5月17日
 *
 */
public class BASE64 implements IEncoder {

	public String encypt(String content) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(content.getBytes());
	}

	public String encypt(byte[] content) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(content);
	}	
	
	public String decrypt(String content) throws Exception {
		byte[] temp = new BASE64Decoder().decodeBuffer(content);
		return new String(temp);
	}
	
	public byte[] decrypt2Byte(String content) throws Exception {
		byte[] temp = new BASE64Decoder().decodeBuffer(content);
		return temp;
	}
	
	public String encypt(String content, String key) throws Exception {
		throw new BizException().setMsg("base64无秘钥");
	}	
	
	public String decrypt(String content, String key) {
		throw new BizException().setMsg("base64无秘钥");
	}

}
