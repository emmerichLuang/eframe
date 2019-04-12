package com.module.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.dto.BaseDto;
import com.base.dto.PageResult;
import com.base.dto.Pager;
import com.module.employee.dao.AdminRoleSetDao;
import com.module.employee.entity.AdminRoleSet;

/**
 * by E.E's code Generator
 * @author liangrl
 * @date   2019-04-10
 *
 */
@Service
public class AdminRoleSetService {

	@Autowired
	private AdminRoleSetDao dao;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public AdminRoleSet get(String id) throws Exception{
		return dao.get(AdminRoleSet.class, id);
	}
	
	/**
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public String create(AdminRoleSet entity) throws Exception{
		return dao.create(entity);
	}	

	/**
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public Integer delete(AdminRoleSet entity) throws Exception{
		return dao.delete(entity);
	}

	/**
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public Integer update(AdminRoleSet entity){
		return dao.update(entity);
	}

	/**
	 * 
	 * @param entity
	 * @param dto
	 * @param pager
	 * @return
	 */
	public List<AdminRoleSet> getList(AdminRoleSet entity, BaseDto dto, Pager pager){
		return dao.getList(entity, dto, pager);
	}
	
	/**
	 * 
	 * @param entity
	 * @param dto
	 * @param pager
	 * @return
	 */
	public PageResult<AdminRoleSet> getPage(AdminRoleSet entity, BaseDto dto, Pager pager){
		return dao.getPage(entity, dto, pager);
	}
	
}