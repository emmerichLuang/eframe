package org.eframe.rpcAccess.icewee;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.eframe.rpcAccess.encypt.algorithm.RSA;
import org.eframe.rpcAccess.transport.dto.JsonResultDto;
import org.eframe.rpcAccess.transport.rpcAccess.HttpUtil;

import com.alibaba.fastjson.JSON;

public class MyPost {

	public static void main(String[] args) throws Exception {
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLb9pf3JpZD3hgZlCJmMPY0XZCxyq4JaaLlWkqS"
				+ "1O1O0c+0g/gfriRIUy+EQIUWd2pDhAupA5HJ/lEo+oXoXmQ7pIDZVdLfoJrGcxQ0SOvi4F+gSAMxSzqMpW3uzQ"
				+ "2wwpYEN+3p+A2F/volD2lpxGbmak8UxKsP+zbaDPVwr1dNQIDAQAB";

		/*
		 * "params":{ "startTime":"2016-01-11", "endTime":"2016-07-11",
		 * "currentPageNo":1 }
		 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", "2016-01-11");
		params.put("endTime", "2016-07-11");
		params.put("currentPageNo", 1);

		String jsonStr = JSON.toJSONString(params);

		byte[] bb = RSAUtils.encryptByPublicKey(jsonStr.getBytes(), publicKey);
		//String encyptJson = RSA.encyptByPublicKey(jsonStr, publicKey);
		String encyptJson = Base64Utils.encode(bb);	//rsa而且base64加密 请求
		
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("appId", "pay");
		req.put("service", "oa.getDimissionList");
		req.put("params", encyptJson);

		CloseableHttpClient client = HttpUtil.getHttpClient(false);

		String respStr = HttpUtil.postJson(client, "http://localhost/rsa",
				JSON.toJSONString(req));

		JsonResultDto respDto = JSON.parseObject(respStr, JsonResultDto.class);
		
		byte[] rr = Base64Utils.decode(respDto.getData().toString());
		
		byte[] ss = RSAUtils.decryptByPublicKey(rr, publicKey);
		//String decryptResult = RSA.decryptByPublic(Base64Utils.encode(rr), publicKey);
		String decryptResult = new String(ss,"utf-8");
		System.err.println(decryptResult);

	}

}
