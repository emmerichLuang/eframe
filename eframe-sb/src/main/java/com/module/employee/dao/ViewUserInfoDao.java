package com.module.employee.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.base.dao.AbstractDao;
import com.base.dto.BaseDto;
import com.base.dto.PageResult;
import com.base.dto.Pager;
import com.base.dto.WhereCond;
import com.base.util.SidGenerator;
import com.module.employee.entity.ViewUserInfo;

/**
 * by E.E's code Generator. 
 * @author liangrl
 * @date   2019-04-16
 *
 */
@Component
public class ViewUserInfoDao extends AbstractDao{

	
	/**
	 * return primary key
	 * @param entity
	 * @throws Exception
	 */
	public String create(ViewUserInfo entity) throws Exception{
		if(StringUtils.isEmpty(entity.getId())){
			entity.setId(SidGenerator.getId());
		}	
		super.insert(ViewUserInfo.class, entity, false);
		
		return entity.getId(); 
	}

	/**
	 * delete by primary key
	 * @param entity
	 * @throws Exception
	 */
	public Integer delete(ViewUserInfo entity) throws Exception{
		return super.del(ViewUserInfo.class, entity.getId());
	}

		
	public PageResult<ViewUserInfo> getPage(ViewUserInfo entity, BaseDto dto, Pager pager) throws Exception{
		PageResult<ViewUserInfo> result = new PageResult<ViewUserInfo>();
		
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
		
		List<ViewUserInfo> list= this.getList(entity, dto, pager);
		Integer count = this.getCount(entity, dto);
		
		result.setCurrentPageNo(currentPageNo);
		result.setList(list);
		result.setTotalRecord(count);
		result.setPageSize(BaseDto.defaultPageSize);
		
		return result;
	}
	
	public Integer update(ViewUserInfo entity){
		//TODO:
		return 0;
	}
	
	/**
	 * 
	 * @param entity
	 * @param dto
	 * @throws Exception
	 */
	public Integer getCount(ViewUserInfo entity, BaseDto dto){
	
		String sql = "select count(*) from view_user_info where 1=1 ";
		WhereCond wc = this.appendWhereCond(entity, dto);
		
		sql += wc.getWhereCond();
		
		return this.getCount(sql, wc.getParams().toArray());
	}
	
	
	/**
	 * 
	 * @param entity
	 * @param dto
	 * @param pager
	 * @throws Exception
	 */	
	public List<ViewUserInfo> getList(ViewUserInfo entity, BaseDto dto, Pager pager) throws Exception{
		
		String sql = "select * from view_user_info where 1=1 ";
		WhereCond wc = this.appendWhereCond(entity, dto);
		
		sql += wc.getWhereCond();
		if(pager==null){
			return this.getList(ViewUserInfo.class, sql, wc.getParams().toArray());
		}else{
			Integer offset = pager.getPageNo()==1?0:(pager.getPageNo()-1)*BaseDto.defaultPageSize;
			sql += String.format("limits %s, %s", offset, pager.getPageSize());
			
			return this.getList(ViewUserInfo.class, sql, wc.getParams().toArray());
		}
	}
	

	private WhereCond appendWhereCond(ViewUserInfo entity, BaseDto dto){
		StringBuilder where = new StringBuilder();
		
		List<Object> params = new ArrayList<Object>();
		
		//TODO: condition...
		
		WhereCond wc = new WhereCond(where.toString());
		wc.setParams(params);
		return wc;
	}
	
}