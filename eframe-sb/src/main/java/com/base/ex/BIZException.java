package com.base.ex;

import java.util.HashMap;

public class BIZException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private Integer code;
	
	private Object data;
	
	
	public BIZException(HashMap<Object, Object> r){
		super((String)r.get("message"));
		String code = (String)r.get("code");
		this.code = Integer.parseInt(code);

		this.data = r;
	}
	
	public BIZException(String message,Integer code, Object data) {
		super(message);
		this.code = code;
		this.data = data;
	}
	
	public BIZException(String message,Integer code) {
		super(message);
		this.code = code;
	}
	
	/**
	 * 408响应码的情况
	 * @param message
	 * @return
	 * @param
	 */
	public static BIZException inValid(String message){
		BIZException biz = new BIZException(message, 408);
		return biz;
	}
	
	/**
	 * 
	 * @param message
	 */
	public BIZException(String message) {
		super(message);
	}
	

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
