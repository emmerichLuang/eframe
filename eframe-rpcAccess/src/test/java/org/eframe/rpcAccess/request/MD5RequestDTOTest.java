package org.eframe.rpcAccess.request;

import java.util.HashMap;
import java.util.Map;

import org.eframe.rpcAccess.encypt.algorithm.MD5;
import org.eframe.rpcAccess.transport.dto.MD5RequestDto;

import com.alibaba.fastjson.JSON;

public class MD5RequestDTOTest {

	static String appId = "oa";
	static String secret = "EAFLKYTRHLJLTRKGYUOJ54BNRF70HJU5NGF8";
	
	static String method = "oa.getDimissionList";
	
	
	
	public static void main(String[] args) {
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("startTime", "2015-03-20");
		data.put("endTime", "2015-04-20");
		data.put("pageNo", "1");
		data.put("pageSize", "20");
		
		MD5RequestDto dto = new MD5RequestDto(appId,method, data);
		
		Map<String,Object> extendData = new HashMap<String,Object>();
		extendData.put("OS", "ANDROID");
		extendData.put("UA", "CHROME");
		extendData.put("HEARTBEATTYPE", "LONG-PULL");		
		dto.setExtendData(extendData);
		
		MD5 md5 = new MD5();

		String finger = md5.generateFinger(data, appId, secret);
		System.err.println("finger:"+finger);
		
		dto.setFinger(finger);
		
		System.out.println(JSON.toJSONString(dto));
	}

}
