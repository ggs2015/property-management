package com.easylife.property.management.dao;

import java.util.List;

import com.easylife.property.management.model.HouseInfoModel;
import com.easylife.property.management.vo.HouseInfoVo;

public interface HouseInfoDao {

	/**
	 * @Title: getBuildingByResidential  
	 * @Description: 获取小区下所有的楼栋
	 * @author liujun
	 * @param residentialId
	 * @return
	 */
	List<String> getBuildingByResidential(Long residentialId);
	
	/**
	 * @Title: getHouseInfo  
	 * @Description: 获取房间的面积
	 * @author liujun
	 * @param houseInfoVo
	 * @return
	 */
	List<HouseInfoModel> getHouseInfo(HouseInfoVo houseInfoVo);
	
	/**
	 * @Title: countHouseByResidentialId  
	 * @Description: 汇总小区所有的户数，考虑分页
	 * @author liujun
	 * @param residentialId
	 * @return
	 */
	List<HouseInfoModel> countHouseByResidentialId(Long residentialId);
}
