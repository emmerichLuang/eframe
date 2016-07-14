package org.eframe.rpcAccess.transport.dto;

import java.util.Map;
import java.util.UUID;

/**
 * 请求通讯类
 * @author liangrl
 * @date   2016年5月18日
 *
 */
public class MD5RequestDto extends BaseRequest{


	private String finger;
	
	/**
	 * 请求哪一个服务（方法）
	 */
	private String service;
	
	/**
	 * 扩展数据，这是不加密的数据
	 */
	private Map<String, Object> extendData;
	
	/**
	 * 加密数据
	 */
	private Map<String,Object> params;

	/**
	 * 必须要3个参数
	 * @param caller
	 * @param service
	 * @param params
	 */
	public MD5RequestDto(String caller, String service, Map<String,Object> params){
		super();
		this.setAppId(caller);
		this.service = service;
		this.params = params;
		this.setId(UUID.randomUUID().toString());
	}
	

	public Map<String, Object> getExtendData() {
		return extendData;
	}

	public MD5RequestDto setExtendData(Map<String, Object> extendData) {
		this.extendData = extendData;
		return this;
	}

	public Object getParams() {
		return params;
	}

	public MD5RequestDto setParams(Map<String,Object> params) {
		this.params = params;
		return this;
	}


	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}


	public String getFinger() {
		return finger;
	}


	public void setFinger(String finger) {
		this.finger = finger;
	}
}
