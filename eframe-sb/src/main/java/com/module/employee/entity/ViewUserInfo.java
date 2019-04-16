package com.module.employee.entity;

import java.io.Serializable;
import java.util.Date;
import com.base.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

/**
 * 
 * @author liangrl
 * @date 2019-04-16
 *
 */
@Component
@Table(name = "view_user_info")
public class ViewUserInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "state")
	private Integer state;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "email")
	private String email;

	@Column(name = "is_mobile_check")
	private Integer isMobileCheck;

	@Column(name = "is_email_check")
	private Integer isEmailCheck;

	@Column(name = "nick_name")
	private String nickName;

	@Column(name = "create_time")
	private Date createTime;

	/************ getter and setter **************/

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
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