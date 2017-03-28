package com.easylife.property.management.service;

import java.util.Map;

import com.easylife.property.management.vo.HouseInfoVo;

public interface ManagementFeeService {

	Map<String, Object> getHouseManagementFeeInfo(HouseInfoVo houseInfoVo);
	
	Boolean payManagementFee(Long residentialId, String buildingId, String roomId, String[] monthArray);
	
	Boolean writeReceipt(Long residentialId, String buildingId, String roomId, String[] monthArray);
	
	Boolean payManagementFeeByMonthes(Long residentialId, String buildingId, String roomId, Integer monthes);
}
