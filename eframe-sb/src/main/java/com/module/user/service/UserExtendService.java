package com.module.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.module.user.dao.UserExtendDao;
import com.module.user.entity.UserInfoExtend;

@Service
public class UserExtendService {

	@Autowired
	private UserExtendDao exDao;
	
	public void create(UserInfoExtend exInfo) throws Exception{
		exDao.create(exInfo);
	}
}
