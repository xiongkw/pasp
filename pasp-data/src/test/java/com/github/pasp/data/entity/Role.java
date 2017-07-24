package com.github.pasp.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.github.pasp.core.Entity;
@Table(name="T_ROLE")
public class Role extends Entity<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ROLE_ID")
	private Long roleId;

	@Column(name="ROLE_CODE")
	private String roleCode;

	@Column(name="ROLE_NAME")
	private String roleName;
	
	@Column(name="LAST_UPDATE")
	private Date lastUpdate;

	@Override
	public Long getId() {
		return this.roleId;
	}

	@Override
	public void setId(Long id) {
		this.roleId = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
