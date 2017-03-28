package com.easylife.property.management.dao;

import com.easylife.property.management.model.ManagementFeeInfoModel;

public interface ManagementFeeInfoDao {

	/**
	 * @Title: getUnitPriceByResidentialId  
	 * @Description: 获取小区物业费单价
	 * @author liujun
	 * @param residentialId
	 * @return
	 */
	ManagementFeeInfoModel getUnitPriceByResidentialId(Long residentialId);
}
