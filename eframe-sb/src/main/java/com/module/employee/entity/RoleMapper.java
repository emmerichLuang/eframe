package com.module.employee.entity;

import java.io.Serializable;

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
@Table(name = "role_mapper")
public class RoleMapper implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "role_id")
	private String roleId;

	@Column(name = "role_set_id")
	private String roleSetId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleSetId() {
		return roleSetId;
	}

	public void setRoleSetId(String roleSetId) {
		this.roleSetId = roleSetId;
	}

}