package com.base.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javassist.Modifier;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.base.constant.RespCode;
import com.base.ex.BIZException;

public abstract class BaseController {

	public HttpServletRequest getRequest(){
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	private void setSuperFields(Class<?> clazz, Object object, HttpServletRequest request) throws Exception{
		
		for(Method method:clazz.getDeclaredMethods()){
			String methodName = method.getName();
			if(!StringUtils.startsWith(methodName.toLowerCase(), "set")){
				continue;
			}
			
			String fieldName = methodName.replaceFirst("set", "");
			//首字母小写
			fieldName = (new StringBuilder()).append(Character.toLowerCase(fieldName.charAt(0))).append(fieldName.substring(1)).toString();	
			
			String val = request.getParameter(fieldName);
			if(StringUtils.isEmpty(val)){
				continue;
			}	
			
			Class<?> paramType = method.getParameterTypes()[0];

			if(paramType==Integer.class){
				if(StringUtils.isEmpty(val)){
					continue;
				}
				method.invoke(object, Integer.parseInt(val));
			}else if(paramType==Date.class){
				if(val.length()==19){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					method.invoke(object, sdf.parse(val));
				}else if(val.length()==10){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					method.invoke(object, sdf.parse(val));
				}else{
					throw new BIZException("不支持的日期格式："+val,RespCode.FAIL);
				}
			}else if(paramType==java.sql.Date.class){
				if(val.length()==19){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					method.invoke(object, new java.sql.Date(sdf.parse(val).getTime()));
				}else if(val.length()==10){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					method.invoke(object, new java.sql.Date(sdf.parse(val).getTime()));
				}else{
					throw new BIZException("不支持的日期格式："+val,RespCode.FAIL);
				}				
			}else if(paramType==java.sql.Timestamp.class){
				if(val.length()==19){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					method.invoke(object, new Timestamp(sdf.parse(val).getTime()));
				}else if(val.length()==10){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					method.invoke(object, new Timestamp(sdf.parse(val).getTime()));
				}else{
					throw new BIZException("不支持的日期格式："+val,RespCode.FAIL);
				}
			}else if(paramType==java.lang.Double.class){
				method.invoke(object, Double.parseDouble(val));
			}else if(paramType==java.lang.Float.class){
				method.invoke(object, Float.parseFloat(val));
			}else if(paramType==BigDecimal.class){
				method.invoke(object, new BigDecimal(val));				
			}else{
				method.invoke(object, val);				
			}			
			
		}
		
		if(clazz.getSuperclass() != Object.class){
			setSuperFields(clazz.getSuperclass(), object, request);
		}
	}
	
	public <T> T getEntityFromReq(Class<T> clazz) throws Exception{
		HttpServletRequest request = getRequest();
		Object object = clazz.newInstance();
		
		for(Field f:clazz.getDeclaredFields()){
			
			if(Modifier.isStatic(f.getModifiers())){
				continue;
			}
			if(Modifier.isFinal(f.getModifiers())){
				continue;
			}
			
			f.setAccessible(true);
			
			String fieldName = f.getName();
			String val = request.getParameter(fieldName);
			if(StringUtils.isEmpty(val)){
				continue;
			}
			
			if(f.getType()==Integer.class){
				if(StringUtils.isEmpty(val)){
					continue;
				}
				f.set(object, Integer.parseInt(val));
			}else if(f.getType()==Date.class){
				if(val.length()==19){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					f.set(object, sdf.parse(val));
				}else if(val.length()==10){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					f.set(object, sdf.parse(val));
				}else{
					throw new BIZException("不支持的日期格式："+val,RespCode.FAIL);
				}
			}else if(f.getType()==java.sql.Date.class){
				if(val.length()==19){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					f.set(object, new java.sql.Date(sdf.parse(val).getTime()));
				}else if(val.length()==10){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					f.set(object, new java.sql.Date(sdf.parse(val).getTime()));
				}else{
					throw new BIZException("不支持的日期格式："+val,RespCode.FAIL);
				}				
			}else if(f.getType()==java.sql.Timestamp.class){
				if(val.length()==19){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					f.set(object, new Timestamp(sdf.parse(val).getTime()));
				}else if(val.length()==10){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					f.set(object, new Timestamp(sdf.parse(val).getTime()));
				}else{
					throw new BIZException("不支持的日期格式："+val,RespCode.FAIL);
				}
			}else if(f.getType()==java.lang.Double.class){
				f.set(object, Double.parseDouble(val));
			}else if(f.getType()==java.lang.Float.class){
				f.set(object, Float.parseFloat(val));
			}else if(f.getType()==BigDecimal.class){
				f.set(object, new BigDecimal(val));				
			}else{
				f.set(object, val);				
			}
			

		}
		//递归找父类
		if(clazz.getSuperclass() != Object.class){
			setSuperFields(clazz.getSuperclass(), object, request);
		}
		
		
		return (T) object;
	}
	
	
	@SuppressWarnings("rawtypes")
	protected Map<String, String> getParams(HttpServletRequest request){
		// 参数Map
	    Map<String, String[]> properties = request.getParameterMap();
	    // 返回值Map
	    Map<String, String> returnMap = new HashMap<String, String>();
	    Iterator<Entry<String, String[]>> entries = properties.entrySet().iterator();
	    Map.Entry entry;
	    String name = "";
	    String value = "";
	    while (entries.hasNext()) {
	        entry = (Map.Entry) entries.next();
	        name = (String) entry.getKey();
	        Object valueObj = entry.getValue();
	        if(null == valueObj){
	            value = "";
	        }else if(valueObj instanceof String[]){
	            String[] values = (String[])valueObj;
	            for(int i=0;i<values.length;i++){
	                value = values[i] + ",";
	            }
	            value = value.substring(0, value.length()-1);
	        }else{
	            value = valueObj.toString();
	        }
	        returnMap.put(name, value);
	    }
	    return returnMap;
	}
}
