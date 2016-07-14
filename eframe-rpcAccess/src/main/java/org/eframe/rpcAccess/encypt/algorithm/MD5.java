package org.eframe.rpcAccess.encypt.algorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * md5的方式生成指纹。
 * 生成md5的时候， 是key+content的形式， key作为salt了。
 * @author liangrl
 * @date   2016年5月17日
 *
 */
public class MD5{

	/**
	 * 生成信息摘要
	 * 这个md5 用 content+key 来计算的
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

	/**
	 * 无盐版。不建议外部使用这个方法。
	 * 给encypt(String content, String key) 这个方法使用
	 * @param content
	 * @return
	 * @throws Exception
	 * @param
	 */
	private String encypt(String content) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(content.getBytes());
		return byteArrayToHex(md5.digest());
	}

	
	public String generateFinger(Map<String,Object> data, String appId, String secret){
		if(data==null){
			throw new RuntimeException("data 不能为空");
		}
		
		List<String> validParamsValue = new ArrayList<String>();
		
		//对象类型， double，decimal等类型都不做签名计算
		for(String key:data.keySet()){
			Object value = data.get(key);
			if(value instanceof String){
				validParamsValue.add((key+"="+value).toLowerCase());
			}else if(value instanceof Integer){
				validParamsValue.add((key+"="+value).toLowerCase());				
			}else if(value instanceof Long){				
				validParamsValue.add((key+"="+value).toLowerCase());				
			}else if(value instanceof Boolean){
				validParamsValue.add((key+"="+value).toLowerCase());				
			}
		}
		//排序
		/*Collections.sort(validParamsValue, new Comparator<String>(){

			public int compare(String o1, String o2) {
				return 0;
			}

			
		});*/
		java.util.Collections.sort(validParamsValue);
		
		//根据参数生成的字符串
		String preSign = StringUtils.join(validParamsValue, "");
		preSign=appId+preSign+secret;
		System.err.println("preSign:"+preSign);
		
		MessageDigest md5 =null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md5.update(preSign.getBytes());
		return byteArrayToHex(md5.digest());
	}
}
