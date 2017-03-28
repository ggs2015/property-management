package com.easylife.property.management.model;

import java.io.Serializable;

public class ResidentialModel implements Serializable{
	/**  
	 * @Fields serialVersionUID : 序列化 
	 */ 
	private static final long serialVersionUID = 2629436390357111019L;
	/**小区id*/
	private Long residentialId;
	/**小区名称*/
	private String residentialName;
	private String province;
	private String city;
	private String area;
	
	public Long getResidentialId() {
		return residentialId;
	}
	public void setResidentialId(Long residentialId) {
		this.residentialId = residentialId;
	}
	public String getResidentialName() {
		return residentialName;
	}
	public void setResidentialName(String residentialName) {
		this.residentialName = residentialName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
}
