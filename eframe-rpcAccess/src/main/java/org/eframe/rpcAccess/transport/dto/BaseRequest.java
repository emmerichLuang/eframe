package org.eframe.rpcAccess.transport.dto;

public abstract class BaseRequest {
	/**
	 * 请求唯一标识。简单的话用uuid
	 */
	private String id;
	
	/**
	 * 代表业务方
	 */
	private String appId;
	
	/**
	 * 版本
	 */
	private String version ="v1";
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String _appId) {
		this.appId = _appId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
