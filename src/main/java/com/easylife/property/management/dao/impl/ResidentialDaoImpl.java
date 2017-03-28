package com.easylife.property.management.dao.impl;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.easylife.property.management.dao.ResidentialDao;
import com.easylife.property.management.model.ResidentialModel;

public class ResidentialDaoImpl implements ResidentialDao {

	private SqlMapClientTemplate sqlMapClientTemplate;
	
	@Override
	public ResidentialModel getResidentialInfoByUserId(Long endUserId) {
		return (ResidentialModel) sqlMapClientTemplate.queryForObject("getResidentialByUserId", endUserId);
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

}
