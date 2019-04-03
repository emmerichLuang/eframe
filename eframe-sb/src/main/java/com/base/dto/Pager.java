package com.base.dto;

public class Pager {
	
	private Integer pageNo;
	
	private Integer pageSize;

	public Pager(Integer pageNo, Integer pageSize){
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
	
	public Integer getPageNo() {
		return pageNo;
	}

	public Pager setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
		return this;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Pager setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}
}
