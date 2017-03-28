package com.easylife.property.management.service;

import com.easylife.property.management.model.EndUserInfo;

public interface UserService {

	Boolean isUserExists(String phoneNo);
	
	EndUserInfo getUserByPhone(String phoneNo);
	
	Boolean insertUser(EndUserInfo user, Long residentialId);
}
