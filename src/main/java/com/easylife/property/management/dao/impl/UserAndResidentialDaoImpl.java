package com.easylife.property.management.dao.impl;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.easylife.property.management.dao.UserAndResidentialDao;
import com.easylife.property.management.model.UserAndResidentialModel;

public class UserAndResidentialDaoImpl implements UserAndResidentialDao {

	private SqlMapClientTemplate sqlMapClientTemplate;
	
	@Override
	public Long insertGroupUserAndResidential(UserAndResidentialModel model) {
		return (Long) sqlMapClientTemplate.insert("insertGroupUserAndResidential", model);
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}
	
}
