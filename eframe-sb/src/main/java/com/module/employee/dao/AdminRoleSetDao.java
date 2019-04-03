package com.module.employee.dao;

import com.base.dao.AbstractDao;
import com.base.util.GUID;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.module.employee.entity.AdminRoleSet;
import com.base.dto.BaseDto;
import com.base.dto.PageResult;
import com.base.dto.Pager;
import com.base.dto.WhereCond;

import java.util.ArrayList;
import java.util.List;

/**
 * by E.E's code Generator
 * @author liangrl
 * @date   2019-04-03
 *
 */
@Component
public class AdminRoleSetDao extends AbstractDao{

	
	/**
	 * 返回主键
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public String create(AdminRoleSet entity) throws Exception{
		if(StringUtils.isEmpty(entity.getId())){
			entity.setId(GUID.nextUUID());
		}
		super.insert(AdminRoleSet.class, entity, false);
		
		return entity.getId();
	}

	/**
	 * delete
	 * @param entity
	 * @throws Exception
	 */
	public void delete(AdminRoleSet entity) throws Exception{
		super.del(AdminRoleSet.class, entity.getId());
	}
	
	/**
	 * 
	 * @param entity
	 * @param dto
	 * @param pager
	 * @return
	 */
	public PageResult<AdminRoleSet> getPage(AdminRoleSet entity, BaseDto dto, Pager pager){
		PageResult<AdminRoleSet> result = new PageResult<AdminRoleSet>();
		
		Integer currentPageNo = null;
		if(dto.currentPageNo==null){
			currentPageNo=1;
		}else{
			currentPageNo = Integer.parseInt(dto.currentPageNo);
		}
		if(currentPageNo<1){
			currentPageNo=1;
		}
		pager.setPageNo(currentPageNo);
		pager.setPageSize(BaseDto.defaultPageSize);
		
		List<AdminRoleSet> list= this.getList(entity, dto, pager);
		Integer count = this.getCount(entity, dto);
		
		result.setCurrentPageNo(currentPageNo);
		result.setList(list);
		result.setTotalRecord(count);
		result.setPageSize(BaseDto.defaultPageSize);
		
		return result;
	}
	
	public Integer update(AdminRoleSet entity){
		//TODO:
		return 0;
	}
	
	public Integer getCount(AdminRoleSet entity, BaseDto dto){
		this.appendWhereCond(entity, dto);
		//TODO:
		return 0;
	}
	
	public List<AdminRoleSet> getList(AdminRoleSet entity, BaseDto dto, Pager pager){
		this.appendWhereCond(entity, dto);
		//Integer offset = pager.getPageNo()==1?0:(pager.getPageNo()-1)*BaseDto.defaultPageSize;
		
		//TODO:
		return null;
	}
	
	private WhereCond appendWhereCond(AdminRoleSet entity, BaseDto dto){
		StringBuilder where = new StringBuilder();
		
		List<Object> params = new ArrayList<Object>();
		
		WhereCond wc = new WhereCond(where.toString());
		wc.setParams(params);
		return wc;
	}
	
}