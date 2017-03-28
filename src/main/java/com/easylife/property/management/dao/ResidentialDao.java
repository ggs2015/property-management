package com.easylife.property.management.dao;

import com.easylife.property.management.model.ResidentialModel;

public interface ResidentialDao {

	/**
	 * @Title: getResidentialInfoByUserId  
	 * @Description: 获取用户所在小区信息
	 * @author liujun
	 * @param endUserId
	 * @return
	 */
	ResidentialModel getResidentialInfoByUserId(Long endUserId);
}
