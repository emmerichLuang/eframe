package com.module.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.module.user.dao.UserDao;
import com.module.user.entity.UserInfo;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	public void create(UserInfo info) throws Exception{
		this.userDao.create(info);
	}
}
