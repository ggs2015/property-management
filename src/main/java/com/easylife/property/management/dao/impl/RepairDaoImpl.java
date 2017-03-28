package com.easylife.property.management.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.easylife.property.management.dao.RepairDao;
import com.easylife.property.management.model.RepairModel;

public class RepairDaoImpl implements RepairDao {

	private SqlMapClientTemplate sqlMapClientTemplate;
	
	@Override
	public List<RepairModel> queryRepairInfo(Map<String, Object> paramMap) {
		return sqlMapClientTemplate.queryForList("queryRepairInfo", paramMap);
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@Override
	public Integer updateRepairInfo(Map<String, Object> paramMap) {
		return sqlMapClientTemplate.update("updateRepairInfo", paramMap);
	}

	
}
