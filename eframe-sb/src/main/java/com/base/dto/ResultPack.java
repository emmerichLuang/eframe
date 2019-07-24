package com.base.dto;

import com.base.constant.RespCode;

public class ResultPack {


	private Integer code;
	
	private String message;
	
	private Object data;

	private ResultPack() {
		super();
	}
	
	private ResultPack(Integer code, String message, Object data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public static ResultPack success(){
		ResultPack result = new ResultPack();
		result.setCode(RespCode.SUCCESS);
		result.setMessage("操作成功");
		return result;
	}
	
	public static ResultPack fail(Object data){
		ResultPack result = new ResultPack();
		result.setCode(RespCode.FAIL);
		result.setMessage("操作失败");
		result.setData(data);
		return result;
	}
	
	public Integer getCode() {
		return code;
	}

	public ResultPack setCode(Integer code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ResultPack setMessage(String message) {
		this.message = message;
		return this;
	}

	public Object getData() {
		return data;
	}

	public ResultPack setData(Object data) {
		this.data = data;
		return this;
	}
	
}
