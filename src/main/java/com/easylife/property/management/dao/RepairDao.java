package com.easylife.property.management.dao;

import java.util.List;
import java.util.Map;

import com.easylife.property.management.model.RepairModel;

public interface RepairDao {

	List<RepairModel> queryRepairInfo(Map<String, Object> paramMap);
	
	Integer updateRepairInfo(Map<String, Object> paramMap);
}
