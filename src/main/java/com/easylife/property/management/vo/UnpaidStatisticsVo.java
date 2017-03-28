package com.easylife.property.management.vo;

import java.math.BigDecimal;

public class UnpaidStatisticsVo {

	private String building;
	private String houseNumber;
	private BigDecimal needPayManagementFee;
	private Integer months;
	
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
	public BigDecimal getNeedPayManagementFee() {
		return needPayManagementFee;
	}
	public void setNeedPayManagementFee(BigDecimal needPayManagementFee) {
		this.needPayManagementFee = needPayManagementFee;
	}
	public Integer getMonths() {
		return months;
	}
	public void setMonths(Integer months) {
		this.months = months;
	}
	
}
