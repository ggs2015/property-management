package com.easylife.property.management.dao;

import com.easylife.property.management.model.UserAndResidentialModel;

public interface UserAndResidentialDao {
	/**
	 * @Title: insertGroupUserAndResidential  
	 * @Description: 插入用户和小区的对应关系
	 * @author liujun
	 * @param model
	 * @return
	 */
	Long insertGroupUserAndResidential(UserAndResidentialModel model);
}
