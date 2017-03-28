/**
 * 
 */
package com.easylife.property.management.bean;

import com.easylife.property.management.model.EndUserInfo;

/**
 * @author liujun
 *
 */
public class ApplicationUserContext {
	
	private static final ThreadLocal<EndUserInfo> threadLocalUser = new ThreadLocal<EndUserInfo>();

	public static EndUserInfo getUser() {
		return threadLocalUser.get();
	}

	public static void setUser(EndUserInfo user) {
		threadLocalUser.set(user);
	}
	public static void clear() {
		threadLocalUser.set(null);
	}

}
