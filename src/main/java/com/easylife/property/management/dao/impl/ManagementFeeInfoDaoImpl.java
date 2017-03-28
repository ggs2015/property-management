package com.easylife.property.management.dao.impl;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.easylife.property.management.dao.ManagementFeeInfoDao;
import com.easylife.property.management.model.ManagementFeeInfoModel;

public class ManagementFeeInfoDaoImpl implements ManagementFeeInfoDao {

	private SqlMapClientTemplate sqlMapClientTemplate;
	
	@Override
	public ManagementFeeInfoModel getUnitPriceByResidentialId(Long residentialId) {
		return (ManagementFeeInfoModel) sqlMapClientTemplate.queryForObject("getUnitPriceByResidentialId", residentialId);
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

}
