package com.easylife.property.management.vo;

import java.math.BigDecimal;
import java.util.Date;

public class PaidStatisticsVo {

	private String building;
	private String houseNumber;
	/**付款时间*/
	private Date createTime;
	private String paidTime;
	private Long endUserId;
	private String endUserName;
	private Integer payMethodType;
	//现金支付时的支付金额
	private BigDecimal needPayManagementFee;
	//非现金支付的支付金额
	private BigDecimal payAmount;
	
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	public String getEndUserName() {
		return endUserName;
	}
	public void setEndUserName(String endUserName) {
		this.endUserName = endUserName;
	}
	public BigDecimal getNeedPayManagementFee() {
		return needPayManagementFee;
	}
	public void setNeedPayManagementFee(BigDecimal needPayManagementFee) {
		this.needPayManagementFee = needPayManagementFee;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public String getPaidTime() {
		return paidTime;
	}
	public void setPaidTime(String paidTime) {
		this.paidTime = paidTime;
	}
	
}
