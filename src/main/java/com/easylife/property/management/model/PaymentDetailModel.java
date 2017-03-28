package com.easylife.property.management.model;

public class PaymentDetailModel {

	private Long id;
	private Long endUserId;
	private Integer payMethodType;
	private Long managementFeeDetailId;
	private Integer isSuccess;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEndUserId() {
		return endUserId;
	}
	public void setEndUserId(Long endUserId) {
		this.endUserId = endUserId;
	}
	public Integer getPayMethodType() {
		return payMethodType;
	}
	public void setPayMethodType(Integer payMethodType) {
		this.payMethodType = payMethodType;
	}
	public Long getManagementFeeDetailId() {
		return managementFeeDetailId;
	}
	public void setManagementFeeDetailId(Long managementFeeDetailId) {
		this.managementFeeDetailId = managementFeeDetailId;
	}
	public Integer getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}
	
}
