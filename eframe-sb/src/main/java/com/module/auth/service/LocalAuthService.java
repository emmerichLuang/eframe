package com.module.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.util.EncyptUtil;
import com.module.auth.dao.LocalAuthDao;

@Service
public class LocalAuthService {

	@Autowired
	private LocalAuthDao authDao;
	
	public void createAuth(String userId, String password) throws Exception{
		String encyptPwd = EncyptUtil.md5Encypt(password);
		authDao.createAuth(userId, encyptPwd);
	}
}
