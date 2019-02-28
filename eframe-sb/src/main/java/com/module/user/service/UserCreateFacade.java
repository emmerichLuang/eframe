package com.module.user.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.module.auth.service.LocalAuthService;
import com.module.user.entity.UserInfo;
import com.module.user.entity.UserInfoExtend;

/**
 * 外观方法，创建用户
 * @Date 2019年2月26日
 * @author E.E.
 *
 */
@Service
public class UserCreateFacade {
	
	@Autowired
	private LocalAuthService localAuthService;
	
	@Autowired
	private UserExtendService userExtendService;
	
	@Autowired
	private UserService userService;
	
	private String default_pwd="112233~";
	
	/**
	 * 
	 */
	public void save(String id, String account, String nickName, String email, String mobile){
		try{
			UserInfo info = new UserInfo();
			info.setId(id);
			info.setEmail(email);
			info.setMobile(mobile);
			info.setState(1);
			info.setUserName(account);
			userService.create(info);
			
			UserInfoExtend exInfo = new UserInfoExtend();
			exInfo.setCreateTime(new Date());
			exInfo.setIsEmailCheck(1);
			exInfo.setIsMobileCheck(1);
			exInfo.setNickName(nickName);
			exInfo.setUserId(id);
			userExtendService.create(exInfo);
			
			localAuthService.createAuth(id, default_pwd);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
