package com.easylife.property.management.model;

import java.io.Serializable;

public class EndUserInfo implements Serializable{
	/**  
	 * @Fields serialVersionUID : 序列化 
	 */ 
	private static final long serialVersionUID = -7220743848700678241L;
	private String userName;
	private Long id;
	/**3：物业工作人员，4：物业管理人员，5：后台管理人员*/
	private Integer roleId;
	private String userPhone;
	/**身份证号*/
	private String identityCardId;
	/**0：女，1：男*/
	private Integer sex;
	private String weixinNo;
	/**微信昵称*/
	private String nickName;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getIdentityCardId() {
		return identityCardId;
	}
	public void setIdentityCardId(String identityCardId) {
		this.identityCardId = identityCardId;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getWeixinNo() {
		return weixinNo;
	}
	public void setWeixinNo(String weixinNo) {
		this.weixinNo = weixinNo;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
}
