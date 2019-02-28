package com.module.user.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

/**
 * 
 * @author liangrl
 * @date 2019-02-26
 *
 */
@Component
@Table(name = "user_info_extend")
public class UserInfoExtend implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "user_id")
	private String userId;

	@Column(name = "is_mobile_check")
	private Integer isMobileCheck;

	@Column(name = "is_email_check")
	private Integer isEmailCheck;

	@Column(name = "nick_name")
	private String nickName;

	@Column(name = "create_time")
	private Date createTime;

	/************ getter and setter **************/

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getIsMobileCheck() {
		return this.isMobileCheck;
	}

	public void setIsMobileCheck(Integer isMobileCheck) {
		this.isMobileCheck = isMobileCheck;
	}

	public Integer getIsEmailCheck() {
		return this.isEmailCheck;
	}

	public void setIsEmailCheck(Integer isEmailCheck) {
		this.isEmailCheck = isEmailCheck;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}