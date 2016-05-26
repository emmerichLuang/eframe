package org.eframe.rpcAccess.transport.dto;

/**
 * 
 * @author liangrl
 * @date   2016年5月18日
 *
 */
public class ResponseDto {

	/**
	 * 请求唯一标识。简单的话用uuid
	 */
	private String id;
	
	private Integer code;
	
	private Object data;

	public ResponseDto(Integer code, Object data){
		this.code = code;
		this.data = data;
	}
	
	public String getId() {
		return id;
	}

	public Integer getCode() {
		return code;
	}

	public Object getData() {
		return data;
	}

	public ResponseDto setId(String id) {
		this.id = id;
		return this;
	}

	public ResponseDto setCode(Integer code) {
		this.code = code;
		return this;
	}

	public ResponseDto setData(Object data) {
		this.data = data;
		return this;
	}
}
