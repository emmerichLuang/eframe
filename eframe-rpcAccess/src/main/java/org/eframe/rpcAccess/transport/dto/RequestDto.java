package org.eframe.rpcAccess.transport.dto;

import java.util.Map;
import java.util.UUID;

/**
 * 请求通讯类
 * @author liangrl
 * @date   2016年5月18日
 *
 */
public class RequestDto {

	/**
	 * 请求唯一标识。简单的话用uuid
	 */
	private String id;
	
	/**
	 * 代表业务方
	 */
	private String caller;
	
	/**
	 * 版本
	 */
	private String version ="v1";

	/**
	 * 扩展数据，这是不加密的数据
	 */
	private Map<String, String> extendData;
	
	/**
	 * 加密数据
	 */
	private Object data;

	/**
	 * 必须要这2个参数的
	 * @param caller
	 * @param data
	 */
	public RequestDto(String caller, Object data){
		this.caller = caller;
		this.data = data;
		this.id = UUID.randomUUID().toString();
	}
	
	public String getCaller() {
		return caller;
	}

	public RequestDto setCaller(String caller) {
		this.caller = caller;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public RequestDto setVersion(String version) {
		this.version = version;
		return this;
	}

	public Map<String, String> getExtendData() {
		return extendData;
	}

	public RequestDto setExtendData(Map<String, String> extendData) {
		this.extendData = extendData;
		return this;
	}

	public Object getData() {
		return data;
	}

	public RequestDto setData(Object data) {
		this.data = data;
		return this;
	}

	public String getId() {
		return id;
	}

	public RequestDto setId(String id) {
		this.id = id;
		return this;
	}
}
