package com.module.user.dao;

import org.springframework.stereotype.Component;

import com.base.dao.AbstractDao;
import com.module.user.entity.UserInfo;

@Component
public class UserDao extends AbstractDao{
	
	public void create(UserInfo info) throws Exception{
		super.insert(UserInfo.class, info, false);
	}
}
