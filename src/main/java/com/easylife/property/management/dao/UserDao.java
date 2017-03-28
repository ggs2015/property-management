package com.easylife.property.management.dao;

import java.util.List;
import java.util.Map;

import com.easylife.property.management.model.EndUserInfo;

public interface UserDao {

	/**
	 * @Title: getUserByPhone  
	 * @Description: 根据用户电话获取用户信息
	 * @author liujun
	 * @param phoneNo
	 * @return
	 */
	EndUserInfo getUserByPhone(String phoneNo);
	
	Long insertUser(EndUserInfo user);
	
	List<EndUserInfo> getUserByIds(List<Long> ids);
	
	List<EndUserInfo> queryBlackUser();
	
	Integer addBlack(Map<String,Object> paramMap);
	
	Integer removeBlack(Map<String,Object> paramMap);
}
