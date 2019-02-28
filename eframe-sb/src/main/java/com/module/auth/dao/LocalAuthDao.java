package com.module.auth.dao;

import org.springframework.stereotype.Component;

import com.base.dao.AbstractDao;
import com.base.util.GUID;

@Component
public class LocalAuthDao extends AbstractDao{
	
	/**
	 * 注意， 这里的密码应该是被加密后的
	 * @param userId
	 * @param encyptPwd
	 */
	public void createAuth(String userId, String encyptPwd){
		String sql = "insert into user_auth_local(id, user_id, password) values(?,?,?) ";
		
		this.update(sql, GUID.nextUUID(), userId, encyptPwd);
	}
}
