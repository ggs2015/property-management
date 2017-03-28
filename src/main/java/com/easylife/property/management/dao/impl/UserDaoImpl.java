package com.easylife.property.management.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.easylife.property.management.model.EndUserInfo;

public class UserDaoImpl implements com.easylife.property.management.dao.UserDao {

	private SqlMapClientTemplate sqlMapClientTemplate;
	
	@Override
	public EndUserInfo getUserByPhone(String phoneNo) {
		return (EndUserInfo) sqlMapClientTemplate.queryForObject("getUserByPhoneNo", phoneNo);
	}
	
	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@Override
	public Long insertUser(EndUserInfo user) {
		return (Long) sqlMapClientTemplate.insert("insertUser", user);
	}

	@Override
	public List<EndUserInfo> getUserByIds(List<Long> ids) {
		return sqlMapClientTemplate.queryForList("getUserByIds", ids);
	}

	@Override
	public List<EndUserInfo> queryBlackUser() {
		return sqlMapClientTemplate.queryForList("queryBlackUser");
	}

	@Override
	public Integer addBlack(Map<String, Object> paramMap) {
		return sqlMapClientTemplate.update("addBlack", paramMap);
	}

	@Override
	public Integer removeBlack(Map<String, Object> paramMap) {
		return sqlMapClientTemplate.update("removeBlack", paramMap);
	}

}
