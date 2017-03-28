package com.easylife.property.management.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.easylife.property.management.dao.UserAndHouseDao;
import com.easylife.property.management.model.GroupUserAndHouseModel;

public class UserAndHouseDaoImpl implements UserAndHouseDao {

	private SqlMapClientTemplate sqlMapClientTemplate;
	
	@Override
	public List<GroupUserAndHouseModel> getHouseWeixinBindInfo(Map<String, Object> paramMap) {
		return sqlMapClientTemplate.queryForList("getHouseWeixinBindInfo", paramMap);
	}

	@Override
	public Integer unbindWeixinFromHouse(Long id) {
		return sqlMapClientTemplate.update("unbindWeixinFromHouse", id);
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@Override
	public List<GroupUserAndHouseModel> getHouseInfoByUserId(List<Long> userIdList) {
		return sqlMapClientTemplate.queryForList("getHouseInfoByUserId", userIdList);
	}

	@Override
	public List<GroupUserAndHouseModel> getHouseUserInfo(Map<String, Object> paramMap) {
		return sqlMapClientTemplate.queryForList("getHouseUserInfo", paramMap);
	}

	@Override
	public Integer unbindUserFromHouse(Long userId) {
		return sqlMapClientTemplate.update("unbindUserFromHouse", userId);
	}

}
