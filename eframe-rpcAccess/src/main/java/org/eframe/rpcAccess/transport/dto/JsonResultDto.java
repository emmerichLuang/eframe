package org.eframe.rpcAccess.transport.dto;

import java.util.Map;

/**
 * 
 * @author liangrl
 * @date   2016年7月15日
 *
 */
public class JsonResultDto {
	
	public JsonResultDto(){
		super();
	}

	public JsonResultDto(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	
	private String appId;
	
	private String id;	//如果有就和请求的id一样。
	
	private Integer code;
	private String msg;
	
	private Map<String, ?> extendInfo;
	private Object data;
	
	
	public String getId() {
		return id;
	}
	public JsonResultDto setId(String id) {
		this.id = id;
		return this;
	}
	public Integer getCode() {
		return code;
	}
	public JsonResultDto setCode(Integer code) {
		this.code = code;
		return this;
	}
	public String getMsg() {
		return msg;
	}
	public JsonResultDto setMsg(String msg) {
		this.msg = msg;
		return this;
	}
	public Map<String, ?> getExtendInfo() {
		return extendInfo;
	}
	public JsonResultDto setExtendInfo(Map<String, ?> extendInfo) {
		this.extendInfo = extendInfo;
		return this;
	}
	public Object getData() {
		return data;
	}
	public JsonResultDto setData(Object data) {
		this.data = data;
		return this;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
}
