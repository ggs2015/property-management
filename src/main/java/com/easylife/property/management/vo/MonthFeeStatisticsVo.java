package com.easylife.property.management.vo;

import java.math.BigDecimal;

public class MonthFeeStatisticsVo {

	/**时间*/
	private String yearAndMonth;
	/**每月应缴*/
	private BigDecimal totalToPay;
	/**实收*/
	private BigDecimal paidFee;
	/**微信支付*/
	private BigDecimal weiChartPaid;
	/**现金支付*/
	private BigDecimal cashPaid;
	/**待收*/
	private BigDecimal needToPay;
	/**完成率*/
	private BigDecimal payRate;
	
	public String getYearAndMonth() {
		return yearAndMonth;
	}
	public void setYearAndMonth(String yearAndMonth) {
		this.yearAndMonth = yearAndMonth;
	}
	public BigDecimal getTotalToPay() {
		return totalToPay;
	}
	public void setTotalToPay(BigDecimal totalToPay) {
		this.totalToPay = totalToPay;
	}
	public BigDecimal getPaidFee() {
		return paidFee;
	}
	public void setPaidFee(BigDecimal paidFee) {
		this.paidFee = paidFee;
	}
	public BigDecimal getWeiChartPaid() {
		return weiChartPaid;
	}
	public void setWeiChartPaid(BigDecimal weiChartPaid) {
		this.weiChartPaid = weiChartPaid;
	}
	public BigDecimal getCashPaid() {
		return cashPaid;
	}
	public void setCashPaid(BigDecimal cashPaid) {
		this.cashPaid = cashPaid;
	}
	public BigDecimal getNeedToPay() {
		return needToPay;
	}
	public void setNeedToPay(BigDecimal needToPay) {
		this.needToPay = needToPay;
	}
	public BigDecimal getPayRate() {
		return payRate;
	}
	public void setPayRate(BigDecimal payRate) {
		this.payRate = payRate;
	}
	
}
