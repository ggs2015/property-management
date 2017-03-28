package com.easylife.property.management.model;

public class UserAndResidentialModel {
	private Long id;
	private Long endUserId;
	private Long residentialId;
	public Long getEndUserId() {
		return endUserId;
	}
	public void setEndUserId(Long endUserId) {
		this.endUserId = endUserId;
	}
	public Long getResidentialId() {
		return residentialId;
	}
	public void setResidentialId(Long residentialId) {
		this.residentialId = residentialId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
