package com.duowan.viewSystem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.eframe.rpcAccess.transport.rpcAccess.HttpUtil;

import com.alibaba.fastjson.JSON;


/**
	
	MD5(key1=value1&key2=value2&key3=value3&signkey)
	^&*(*()%&*HNjsdfsd
	
	queryParams=urlcode下(json)
 * @author liangrl
 * @date   2016年7月21日
 *
 */
public class HuanjuBaoTest {

	public static String generateFinger(Map<String,Object> data, String secret) throws Exception{
		if(data==null){
			throw new RuntimeException("data 不能为空");
		}
		
		List<String> validParamsValue = new ArrayList<String>();
		
		//对象类型， double，decimal等类型都不做签名计算
		for(String key:data.keySet()){
			Object value = data.get(key);
			if(value instanceof String){
				validParamsValue.add((key+"="+value));
			}else if(value instanceof Integer){
				validParamsValue.add((key+"="+value));				
			}else if(value instanceof Long){				
				validParamsValue.add((key+"="+value));				
			}else if(value instanceof Boolean){
				validParamsValue.add((key+"="+value));				
			}
		}
		//排序
		java.util.Collections.sort(validParamsValue);
		
		//根据参数生成的字符串
		String preSign = StringUtils.join(validParamsValue, "&");
		preSign=preSign+"&"+secret;
		System.err.println("preSign:"+preSign);
		
		String aa = DigestUtils.md5Hex(preSign.getBytes("utf-8"));
		System.err.println("aa:"+aa);
		return aa;
	}
	
	private static String byteArrayToHex(byte[] byteArray) {
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
	
	public static void _main(String[] args) throws Exception {
		CloseableHttpClient httpClient = HttpUtil.getHttpClient(false);
		
		Map<String,Object> data = new HashMap<String,Object>();

		data.put("startLimit", "0");
		data.put("endLimit", "10");

		data.put("startTime", "2016-06-21 12: 03");
		data.put("endTime", "2016-07-21 22: 03");
        
		data.put("startAmount", "100.00");
		data.put("endAmount", "2000.00");

		data.put("orderId", "656546545");
		data.put("uid", "15012555");
		
		/*{
		    "sign": "1aaebfa5785f7aebe825ae9482270799",
		    
		    "startLimit": "10",		    
		    "endLimit": "30",
		    
		  	"startTime": "2016-07-21 12: 03",
		    "endTime": "2016-07-21 22: 03",

		    "startAmount": "100.00",		    
		    "endAmount": "2000.00",
		    
		    "orderId": "656546545",
		    "uid": "15012555"
		}*/		
		
		String secret = "^&*(*()%&*HNjsdfsd";
		
		String sign = generateFinger(data,secret);
		
		data.put("sign", sign);
		
		String json = JSON.toJSONString(data);
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("queryParams", URLEncoder.encode(json,"utf-8"));
		
		String content = HttpUtil.post(httpClient, "http://172.25.20.3/hjb/api/open/queryHJBOrder", params);
		
		System.err.println(content);
	}
	
	
	  public static void main(String[] args) throws Exception {

	        TreeMap<String, Object> queryParams = new TreeMap<String, Object>();
	        queryParams.put("startTime", "2016-07-21 12:03");
	        queryParams.put("endTime", "2016-07-21 22:03");
	        queryParams.put("startLimit", "0");
	        queryParams.put("endLimit", "20");
	        //queryParams.put("startAmount", "100.00");
	        //queryParams.put("endAmount", "2000.00");
	        //queryParams.put("uid", "15012555");
	        //queryParams.put("orderId", "656546545");
	        
			String secret = "^&*(*()%&*HNjsdfsd";
			String sign = generateFinger(queryParams,secret);	        
	        queryParams.put("sign", sign);
	        HttpClient httpClient = new DefaultHttpClient();
	        String queryUrl = "http://172.25.20.3/hjb/api/open/queryHJBOrder";
	        HttpPost post = new HttpPost(queryUrl);
	        List<org.apache.http.NameValuePair> params = new ArrayList<org.apache.http.NameValuePair>();
	        params.add(new BasicNameValuePair("queryParams", JSON.toJSONString(queryParams)));
	        post.setEntity(new UrlEncodedFormEntity((List<? extends org.apache.http.NameValuePair>) params, HTTP.UTF_8));
	        System.out.println(EntityUtils.toString(httpClient.execute(post).getEntity()));
	        
		  //String ogin = "endAmount=2000.00&endLimit=30&endTime=2016-07-21 22:03&orderId=656546545&startAmount=100.00&startLimit=10&startTime=2016-07-21 12:03&uid=15012555&^&*(*()%&*HNjsdfsd";
		  //String ogin = "endamount=2000.00&endlimit=30&endtime=2016-07-21 22:03&orderid=656546545&startamount=100.00&startlimit=10&starttime=2016-07-21 12:03&uid=15012555&^&*(*()%&*HNjsdfsd";
		  //System.out.println(DigestUtils.md5Hex(ogin.getBytes("utf-8")));
	    }

}
