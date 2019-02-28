package com.module.auth.entity;

import java.io.Serializable;

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
@Table(name = "user_auth_local")
public class UserAuthLocal implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "password")
	private String password;

	/************ getter and setter **************/

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}