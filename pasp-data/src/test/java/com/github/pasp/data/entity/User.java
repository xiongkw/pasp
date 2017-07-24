package com.github.pasp.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.github.pasp.core.Entity;

@Table(name = "T_USER")
public class User extends Entity<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "GENDER")
	private String gender;
	
	@Column(name = "AGE")
	private Integer age;
	
	@Column(name = "IMAGE")
	private byte[] image;
	
	@Column(name = "REMARK")
	private String remark;
	
	@Version
	@Column(name = "LOCK")
	private Long lock;
	
	@Column(name = "LAST_UPDATE")
	private Date lastUpdate;
	
	@Column(name="REGION")
	private Long region;

	@Override
	public Long getId() {
		return this.userId;
	}

	@Override
	public void setId(Long id) {
		this.userId = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getLock() {
		return lock;
	}

	public void setLock(Long lock) {
		this.lock = lock;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Long getRegion() {
		return region;
	}

	public void setRegion(Long region) {
		this.region = region;
	}

}
