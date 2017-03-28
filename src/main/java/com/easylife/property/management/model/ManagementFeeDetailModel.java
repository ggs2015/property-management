package com.easylife.property.management.model;

import java.math.BigDecimal;

public class ManagementFeeDetailModel {

	private Long id;
	/**houseid*/
	private Long houseId;
	private Integer year;
	private Integer month;
	/**应交物业费*/
	private BigDecimal needPayManagementFee;
	/**实缴物业费*/
	private BigDecimal realPayManagementFee;
	/**是否缴费：0是，1否*/
	private Integer isPaid;
	/**是否开发票：0是，1否*/
	private Integer isWroteReceipt;
	/**缴费方式：0：支付宝；1：微信；2：现金*/
	private Integer payMethodType;
	
	public Long getHouseId() {
		return houseId;
	}
	public void setHouseId(Long houseId) {
		this.houseId = houseId;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public BigDecimal getNeedPayManagementFee() {
		return needPayManagementFee;
	}
	public void setNeedPayManagementFee(BigDecimal needPayManagementFee) {
		this.needPayManagementFee = needPayManagementFee;
	}
	public BigDecimal getRealPayManagementFee() {
		return realPayManagementFee;
	}
	public void setRealPayManagementFee(BigDecimal realPayManagementFee) {
		this.realPayManagementFee = realPayManagementFee;
	}
	public Integer getIsPaid() {
		return isPaid;
	}
	public void setIsPaid(Integer isPaid) {
		this.isPaid = isPaid;
	}
	public Integer getIsWroteReceipt() {
		return isWroteReceipt;
	}
	public void setIsWroteReceipt(Integer isWroteReceipt) {
		this.isWroteReceipt = isWroteReceipt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getPayMethodType() {
		return payMethodType;
	}
	public void setPayMethodType(Integer payMethodType) {
		this.payMethodType = payMethodType;
	}
	
}
