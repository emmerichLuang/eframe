package org.eframe.rpcAccess.transport.dto;

import java.util.Map;
import java.util.UUID;

/**
 * 请求通讯类
 * @author liangrl
 * @date   2016年5月18日
 *
 */
public class RSARequestDto extends BaseRequest{


	/**
	 * 请求哪一个服务（方法）
	 */
	private String service;
	
	/**
	 * 扩展数据，这是不加密的数据
	 */
	private Map<String, String> extendData;
	
	/**
	 * 加密数据
	 */
	private Object data;

	/**
	 * 必须要3个参数
	 * @param caller
	 * @param service
	 * @param data
	 */
	public RSARequestDto(String caller, String service, Object data){
		super();
		this.setAppId(caller);
		this.service = service;
		this.data = data;
		this.setId(UUID.randomUUID().toString());
	}
	

	public Map<String, String> getExtendData() {
		return extendData;
	}

	public RSARequestDto setExtendData(Map<String, String> extendData) {
		this.extendData = extendData;
		return this;
	}

	public Object getData() {
		return data;
	}

	public RSARequestDto setData(Object data) {
		this.data = data;
		return this;
	}


	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
}
