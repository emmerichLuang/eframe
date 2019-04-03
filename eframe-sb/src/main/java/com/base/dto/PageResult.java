package com.base.dto;

import java.util.List;

public class PageResult<T> {

	private Integer pageSize;			//一页多少条
	
	private Integer totalPages;				//一共多少页
	private Integer totalRecords;		//一共多少条数据
	private Integer currentPageNo;		//当前第几页
	
	
	private List<T> list;

	public Integer getPageSize() {
		return pageSize;
	}

	public PageResult<T> setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public PageResult<T> setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
		return this;
	}

	public List<T> getList() {
		return list;
	}

	public PageResult<T> setList(List<T> list) {
		this.list = list;
		return this;
	}

	public Integer getTotalRecord() {
		return totalRecords;
	}

	public PageResult<T> setTotalRecord(Integer totalRecords) {
		this.totalRecords = totalRecords;
		return this;
	}

	public Integer getCurrentPageNo() {
		return currentPageNo;
	}

	public PageResult<T> setCurrentPageNo(Integer currentPage) {
		this.currentPageNo = currentPage;
		return this;
	}
}
