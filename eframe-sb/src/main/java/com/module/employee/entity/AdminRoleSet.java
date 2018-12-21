package com.module.employee.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

/**
 * 
 * @author liangrl
 * @date 2018-12-18
 *
 */
@Component
@Table(name = "user_admin_roleset")
public class AdminRoleSet implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "role_set_name")
	private String roleSetName;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "creator")
	private String creator;

	/************ getter and setter **************/

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleSetName() {
		return this.roleSetName;
	}

	public void setRoleSetName(String roleSetName) {
		this.roleSetName = roleSetName;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

}