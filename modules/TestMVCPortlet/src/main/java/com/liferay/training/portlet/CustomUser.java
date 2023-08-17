package com.liferay.training.portlet;

import java.util.ArrayList;
import java.util.List;

public class CustomUser {
	private Long userId;
	private String fullName;
	private String email;
	private boolean isMale;
	private java.util.Date birthday;
	private List<String> position;
	private List<String> phones;
	private List<String> organizations;
	
	@Override
	public String toString() {
		return "CustomUser [userId=" + userId + ", fullName=" + fullName + ", email=" + email + ", isMale=" + isMale
				+ ", birthday=" + birthday + ", position=" + position + ", phones=" + phones + ", organizations="
				+ organizations + "]";
	}

	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setSex(boolean sex) {
		this.isMale = sex;
	}
	
	public boolean isMale() {
		return isMale;
	}
		
	public java.util.Date getBirthday() {
		return birthday;
	}
	
	public void setBirthday(java.util.Date birthday) {
		this.birthday = birthday;
	}
	
	public List<String> getPosition() {
		if(position == null) new ArrayList<>();
		return position;
	}
	
	public void setPosition(List<String> position) {
		this.position = position;
	}
	
	public List<String> getPhones() {
		if(phones == null) new ArrayList<>();
		return phones;
	}
	
	public void setPhones(List<String> phones) {
		this.phones = phones;
	}
	
	public List<String> getOrganizations() {
		if(organizations == null) new ArrayList<>();
		return organizations;
	}
	
	public void setOrganizations(List<String> organizations) {
		this.organizations = organizations;
	}
}
