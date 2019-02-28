package com.module.user.dao;

import org.springframework.stereotype.Component;

import com.base.dao.AbstractDao;
import com.module.user.entity.UserInfoExtend;

@Component
public class UserExtendDao extends AbstractDao{
	
	public void create(UserInfoExtend info) throws Exception{
		super.insert(UserInfoExtend.class, info, false);
	}
}
