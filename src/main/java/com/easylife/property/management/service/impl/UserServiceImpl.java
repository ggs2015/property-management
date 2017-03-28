package com.easylife.property.management.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.easylife.property.management.dao.UserAndResidentialDao;
import com.easylife.property.management.dao.UserDao;
import com.easylife.property.management.model.EndUserInfo;
import com.easylife.property.management.model.UserAndResidentialModel;
import com.easylife.property.management.service.UserService;
import com.easylife.property.management.utils.IdcardValidator;
import com.easylife.property.management.utils.PhoneNoValidator;

public class UserServiceImpl implements UserService {

	private final Logger log = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	private TransactionTemplate transactionTemplate;
	private UserAndResidentialDao userAndResidentialDao;
	@Override
	public Boolean isUserExists(String phoneNo) {
		EndUserInfo user = userDao.getUserByPhone(phoneNo);
		if(user == null){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public EndUserInfo getUserByPhone(String phoneNo) {
		return userDao.getUserByPhone(phoneNo);
	}
	
	@Override
	public Boolean insertUser(final EndUserInfo user, final Long residentialId) {
		//1.字段校验
		if(user == null){
			return false;
		}
		if(StringUtils.isEmpty(user.getUserName())){
			return false;
		}
		if(user.getRoleId() == null){
			return false;
		}
		if(user.getSex() == null){
			return false;
		}
		//2.身份证校验
		if(StringUtils.isEmpty(user.getIdentityCardId())
				|| !IdcardValidator.isValidatedAllIdcard(user.getIdentityCardId())){
			return false;
		}
		//3.电话校验
		if(StringUtils.isEmpty(user.getUserPhone())
				|| !PhoneNoValidator.isMobile(user.getUserPhone())){
			return false;
		}
		//4.插入
		Boolean isSuccess = transactionTemplate.execute(new TransactionCallback<Boolean>() {
			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				try {
					Long endUserId = userDao.insertUser(user);
					if(endUserId == null){
						status.setRollbackOnly();//回滚事务
						return false;
					}
					UserAndResidentialModel userAndResidentialModel = new UserAndResidentialModel();
					userAndResidentialModel.setEndUserId(endUserId);
					userAndResidentialModel.setResidentialId(residentialId);
					//插入用户和小区的关系
					userAndResidentialDao.insertGroupUserAndResidential(userAndResidentialModel);
				} catch (Exception e) {
					log.error("添加用户，插入数据库失败：", e);
					status.setRollbackOnly();//回滚事务
					return false;
				}
				return true;
			}
		});
		if(isSuccess){
			return true;
		}else{
			return false;
		}
	}
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public void setUserAndResidentialDao(UserAndResidentialDao userAndResidentialDao) {
		this.userAndResidentialDao = userAndResidentialDao;
	}

}
