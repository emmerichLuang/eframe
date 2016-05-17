package org.eframe.core.exception;

/**
 * 业务异常
 * @author liangrl
 * @date   2016年5月17日
 *
 */
public class BizException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private Object data;
	private String code;
	private String msg;
	private boolean flag = false;
	
	public Object getData() {
		return data;
	}
	public BizException setData(Object data) {
		this.data = data;
		return this;
	}
	public String getCode() {
		return code;
	}
	public BizException setCode(String code) {
		this.code = code;
		return this;
	}
	public String getMsg() {
		return msg;
	}
	public BizException setMsg(String msg) {
		this.msg = msg;
		return this;
	}
	public boolean isFlag() {
		return flag;
	}
	public BizException setFlag(boolean flag) {
		this.flag = flag;
		return this;
	}
}
