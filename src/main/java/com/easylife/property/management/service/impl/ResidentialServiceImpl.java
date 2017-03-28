package com.easylife.property.management.service.impl;

import org.apache.log4j.Logger;

import com.easylife.property.management.dao.ResidentialDao;
import com.easylife.property.management.model.ResidentialModel;
import com.easylife.property.management.service.ResidentialService;

import net.rubyeye.xmemcached.MemcachedClient;

public class ResidentialServiceImpl implements ResidentialService {

	private final static Logger log = Logger.getLogger(ResidentialServiceImpl.class);
	private ResidentialDao residentialDao;
	private MemcachedClient memcachedClient;
	private final static String CACHE_PREFIX = "resident_info_"; 
	
	@Override
	public ResidentialModel getResidentialInfoByUserId(Long endUserId) {
		ResidentialModel model = null;
		try {
			model = memcachedClient.get(CACHE_PREFIX + endUserId);
		} catch (Exception e) {
			log.error("从缓存获取用户所在小区信息失败", e);
		}
		if(model == null){
			model = residentialDao.getResidentialInfoByUserId(endUserId);
			if(model != null){
				try {
					//如果第二次add的key相同，则存储失败，而set方法允许key相同，如果相同，则替换该key对应的value。
					memcachedClient.set(CACHE_PREFIX + endUserId, 60*60, model);
				} catch (Exception e) {
					log.error("将用户小区信息存入缓存失败",e);
				} 
			}
		}
		return model;
	}
	
	public void setResidentialDao(ResidentialDao residentialDao) {
		this.residentialDao = residentialDao;
	}
	
	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}
	
}
