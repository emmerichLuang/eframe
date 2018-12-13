package com.base.util;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {
	private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);
	
    private static ObjectMapper objectMapper = new ObjectMapper();
    static{
    	objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    	//设置输入时忽略JSON字符串中存在而Java对象实际没有的属性 
    	//objectMapper.setDeserializationConfig(objectMapper.getDeserializationConfig().without(                  
    	//	       DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
    	
    	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }
    
	/**
	 * 使用对象进行json反序列化。
	 * @param json
	 * @param pojoClass
	 * @return
	 * @throws Exception
	 */
	public static Object decode(String json, Class<?> pojoClass) throws Exception{		
		return objectMapper.readValue(json, pojoClass);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> decode2Map(String json){
		Map<String, Object> result = new HashMap<String, Object>();
		result = (Map<String, Object>) catchDecode(json, result.getClass());
		
		return result;
	}

	public static Object catchDecode(String json, Class<?> pojoClass){		
		try {
			return decode(json, pojoClass);
		} catch (Exception e) {
			logger.error("error json:"+json,e);
		}
		return null;
	}
	
	public static String catchedEncode(Object o){
		try {
			return encode(o);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 将对象序列化。
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static String encode(Object o) throws Exception{
		return objectMapper.writeValueAsString(o);
	}
}
