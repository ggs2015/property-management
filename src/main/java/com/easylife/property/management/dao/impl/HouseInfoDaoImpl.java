package com.easylife.property.management.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.easylife.property.management.dao.HouseInfoDao;
import com.easylife.property.management.model.HouseInfoModel;
import com.easylife.property.management.vo.HouseInfoVo;

public class HouseInfoDaoImpl implements HouseInfoDao {

	private SqlMapClientTemplate sqlMapClientTemplate;
	
	@Override
	public List<String> getBuildingByResidential(Long residentialId) {
		return sqlMapClientTemplate.queryForList("getBuildingByResidential", residentialId);
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@Override
	public List<HouseInfoModel> getHouseInfo(HouseInfoVo houseInfoVo) {
		return sqlMapClientTemplate.queryForList("getHouseInfo", houseInfoVo);
	}

	@Override
	public List<HouseInfoModel> countHouseByResidentialId(Long residentialId) {
		return (List<HouseInfoModel>) sqlMapClientTemplate.queryForList("countHouseByResidentialId", residentialId);
	}

}
