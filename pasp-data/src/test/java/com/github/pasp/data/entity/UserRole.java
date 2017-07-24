package com.github.pasp.data.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.github.pasp.core.Entity;
@Table(name="T_USER_ROLE")
public class UserRole extends Entity<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="USER_ID")
	private Long userId;
	
	@Column(name="ROLE_ID")
	private Long roleId;
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
