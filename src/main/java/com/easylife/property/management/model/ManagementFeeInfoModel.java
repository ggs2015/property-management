package com.easylife.property.management.model;

import java.math.BigDecimal;

public class ManagementFeeInfoModel {

	/**小区id*/
	private Long residentialId;
	/**物业费单价，每平米*/
	private BigDecimal unitPrice;
	
	public Long getResidentialId() {
		return residentialId;
	}
	public void setResidentialId(Long residentialId) {
		this.residentialId = residentialId;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	
}
