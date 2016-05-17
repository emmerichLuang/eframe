package org.eframe.rpcAccess.encypt.behavior.impl;

import java.security.MessageDigest;

import org.eframe.core.exception.BizException;
import org.eframe.rpcAccess.encypt.behavior.IEncoder;

/**
 * md5的方式生成指纹。
 * 生成md5的时候， 是key+content的形式， key作为salt了。
 * @author liangrl
 * @date   2016年5月17日
 *
 */
public class MD5 implements IEncoder {

	/**
	 * 生成信息摘要
	 * @param content 待加密的内容
	 * @param key 盐，混淆用。
	 */
	public String encypt(String content, String key) throws Exception {
		return encypt(content+key);
	}

	private String byteArrayToHex(byte[] byteArray) {
		// 首先初始化一个字符数组，用来存放每个16进制字符
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		// new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
		char[] resultCharArray = new char[byteArray.length * 2];
		// 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
		int index = 0;
		for (byte b : byteArray) {
			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b & 0xf];
		}
		// 字符数组组合成字符串返回
		return new String(resultCharArray);
	}

	public String encypt(String content) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(content.getBytes());
		return byteArrayToHex(md5.digest());
	}

	public String decrypt(String content) throws Exception {
		throw new BizException().setMsg("md5不支持解密");
	}
	
	public String decrypt(String content, String key) {
		throw new BizException().setMsg("md5不支持解密");
	}

}
