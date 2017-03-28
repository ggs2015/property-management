package com.easylife.property.management.dao;

import java.util.List;
import java.util.Map;

import com.easylife.property.management.model.GroupUserAndHouseModel;

public interface UserAndHouseDao {

	/**
	 * @Title: getHouseWeixinBindInfo  
	 * @Description: 获取房间绑定的所有用户
	 * @author liujun
	 * @param paramMap
	 * @return
	 */
	List<GroupUserAndHouseModel> getHouseWeixinBindInfo(Map<String,Object> paramMap);
	
	/**
	 * @Title: unbindWeixinFromHouse  
	 * @Description: 解绑
	 * @author liujun
	 * @param id
	 * @return
	 */
	Integer unbindWeixinFromHouse(Long id);
	
	/**
	 * @Title: unbindUserFromHouse  
	 * @Description: 将用户id解绑所有房间 
	 * @author liujun
	 * @param userId
	 * @return
	 */
	Integer unbindUserFromHouse(Long userId);
	
	/**
	 * @Title: getHouseInfoByUserId  
	 * @Description: 根据用户id查询用户房间信息
	 * @author liujun
	 * @param userIdList
	 * @return
	 */
	List<GroupUserAndHouseModel> getHouseInfoByUserId(List<Long> userIdList);
	
	/**
	 * 
	 * @Title: getHouseUserInfo  
	 * @Description: 查询房间绑定的用户信息 
	 * @author liujun
	 * @param paramMap
	 * @return
	 */
	List<GroupUserAndHouseModel> getHouseUserInfo(Map<String,Object> paramMap);
}
