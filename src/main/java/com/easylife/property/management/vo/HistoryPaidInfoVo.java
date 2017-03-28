package com.easylife.property.management.vo;

import java.util.Date;

public class HistoryPaidInfoVo {
	
	private String houseNumber;
	private Date createTime;
	/**缴费方式：0：支付宝；1：微信；2：现金*/
	private Integer payMethodType;
	private String year;
	private String month;
	private String timeStr;
	private Long endUserId;
	private String userName;
	private String userPhone;
	
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
	public Integer getPayMethodType() {
		return payMethodType;
	}
	public void setPayMethodType(Integer payMethodType) {
		this.payMethodType = payMethodType;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getTimeStr() {
		return timeStr;
	}
	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}
	public Long getEndUserId() {
		return endUserId;
	}
	public void setEndUserId(Long endUserId) {
		this.endUserId = endUserId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	
}
