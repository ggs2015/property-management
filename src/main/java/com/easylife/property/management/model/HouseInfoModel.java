package com.easylife.property.management.model;

import java.math.BigDecimal;

public class HouseInfoModel {
	/**房子id*/
	private Long houseId;
	/**小区id*/
	private Long residentialId;
	/**栋*/
	private String buildingId;
	/**楼层*/
	private Integer floor;
	/**门号*/
	private String houseNumber;
	/**房间面积*/
	private BigDecimal houseArea;
	
	public Long getHouseId() {
		return houseId;
	}
	public void setHouseId(Long houseId) {
		this.houseId = houseId;
	}
	public Long getResidentialId() {
		return residentialId;
	}
	public void setResidentialId(Long residentialId) {
		this.residentialId = residentialId;
	}
	public Integer getFloor() {
		return floor;
	}
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public BigDecimal getHouseArea() {
		return houseArea;
	}
	public void setHouseArea(BigDecimal houseArea) {
		this.houseArea = houseArea;
	}
	
}
