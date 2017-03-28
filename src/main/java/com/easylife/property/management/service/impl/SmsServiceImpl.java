package com.easylife.property.management.service.impl;

import java.util.Date;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easylife.property.management.service.SmsService;
import com.easylife.property.management.sms.client.AbsRestClient;
import com.easylife.property.management.sms.client.JsonReqClient;
import com.easylife.property.management.sms.client.XmlReqClient;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

public class SmsServiceImpl implements SmsService {

	private MemcachedClient memcachedClient;
	private final Logger log = Logger.getLogger(SmsServiceImpl.class);
	
	@Override
	public Boolean sendSmsToUser(String phoneNo) {
		try {
			//缓存时间单位是秒
			memcachedClient.set(phoneNo + "time", 2*60, new Date().getTime());
		}  catch (Exception e) {
			log.error("从缓存取值发短信间隔时间失败：" , e);
		}
		//6位随机码
		Integer code = (int) ((Math.random()*9+1)*100000);
		try {
			memcachedClient.set(phoneNo, 5*60, code);
			sendSms(phoneNo, code.toString());
		} catch (Exception e) {
			log.error("短信验证码发送失败：", e);
			return false;
		}
		return true;
	}
	
	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	private boolean sendSms(String phoneNo, String verificationCode){
		boolean json=true;
		String accountSid = "5a7568156d8c87a504449f7e52f54d76";
		String authToken = "b7e67a09b9e634211ea03574cfa44e5b";
		String appId = "3070415766274901a31f9a186ead3ecd";
		String templateId = "29495";
		String to = phoneNo;
		String param = verificationCode;
		try {
			String result=InstantiationRestAPI(json).templateSMS(accountSid, authToken, appId, templateId, to, param);
			JSONObject resultObj = (JSONObject) JSON.parse(result);
			if("000000".equals(resultObj.getJSONObject("resp").getString("respCode"))){
				return true;
			}else{
				log.error("短信验证码发送出错：" + result);
				return false;
			}
		} catch (Exception e) {
			log.error("发短信验证码异常" , e);
			return false;
		}
	}
	
	static AbsRestClient InstantiationRestAPI(boolean enable) {
		if(enable) {
			return new JsonReqClient();
		} else {
			return new XmlReqClient();
		}
	}
}
