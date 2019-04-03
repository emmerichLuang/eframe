package com.base.dto;

import java.util.ArrayList;
import java.util.List;

public class WhereCond {

	
	public WhereCond(String whereCond) {
		super();
		this.whereCond = whereCond;
	}
	
	public WhereCond(String whereCond, List<Object> params) {
		super();
		this.whereCond = whereCond;
		this.params = params;
	}

	private String whereCond;
	
	private List<Object> params = new ArrayList<Object>();
	
	
	public String getWhereCond() {
		return whereCond;
	}

	public void setWhereCond(String whereCond) {
		this.whereCond = whereCond;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}
}
