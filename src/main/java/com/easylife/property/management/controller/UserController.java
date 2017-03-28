package com.easylife.property.management.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easylife.property.management.bean.ApplicationUserContext;
import com.easylife.property.management.dao.HouseInfoDao;
import com.easylife.property.management.dao.UserAndHouseDao;
import com.easylife.property.management.dao.UserDao;
import com.easylife.property.management.model.EndUserInfo;
import com.easylife.property.management.model.GroupUserAndHouseModel;
import com.easylife.property.management.model.ResidentialModel;
import com.easylife.property.management.service.ResidentialService;
import com.easylife.property.management.utils.MD5Encrypt;

import net.rubyeye.xmemcached.MemcachedClient;

@Controller
public class UserController {

	private final Logger log = Logger.getLogger(UserController.class);
	@Autowired
	private ResidentialService residentialService;
	@Autowired
	private HouseInfoDao houseInfoDao;
	@Autowired
	private UserAndHouseDao userAndHouseDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private MemcachedClient memcachedClient;
	
	@RequestMapping(value="/toBlack")
	public String toBlack(Model model){
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		model.addAttribute("residentialName", residentialInfo.getResidentialName());
		model.addAttribute("roleId", user.getRoleId());
		model.addAttribute("userName", user.getUserName());
		model.addAttribute("phoneNo", user.getUserPhone());
		//查询小区所有楼栋
		model.addAttribute("buildingIdList",houseInfoDao.getBuildingByResidential(residentialInfo.getResidentialId()));
		return "black_page";
	}
	
	@ResponseBody
	@RequestMapping(value="/queryHouseUser")
	public Map<String,Object> queryHouseUser(@RequestParam String building,
			@RequestParam String houseNumber){
		Map<String,Object> resultMap = new HashMap<>();
		if(StringUtils.isEmpty(houseNumber) || StringUtils.isEmpty(building)){
			resultMap.put("code", "2");//必填参数为空
			return resultMap;
		}
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		if(residentialInfo == null){
			resultMap.put("code", "1");//没有权限
			return resultMap;
		}
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("residentialId", residentialInfo.getResidentialId());
		paramMap.put("building", building);
		paramMap.put("houseNumber", houseNumber);
		List<GroupUserAndHouseModel> houseUserList = userAndHouseDao.getHouseUserInfo(paramMap);
		resultMap.put("code", "0");
		resultMap.put("houseUserList", houseUserList);
		return resultMap;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/queryBlackUser")
	public Map<String,Object> queryBlackUser(){
		Map<String,Object> resultMap = new HashMap<>();
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		if(residentialInfo == null){
			resultMap.put("code", "1");//没有权限
			return resultMap;
		}
		List<EndUserInfo> blackUserList = userDao.queryBlackUser();
		resultMap.put("code", "0");
		resultMap.put("blackUserList", blackUserList);
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value="/addBlack")
	public String addBlack(@RequestParam Long userId){
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		if(residentialInfo == null){
			//没有权限
			return "1";
		}
		//查询用户
		List<Long> userIdList = new ArrayList<>();
		userIdList.add(userId);
		List<EndUserInfo> userList = userDao.getUserByIds(userIdList);
		if(CollectionUtils.isEmpty(userList)){
			return "2";//用户不存在
		}
		//1.解绑
		userAndHouseDao.unbindUserFromHouse(userId);
		//2.加入黑名单
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		userDao.addBlack(paramMap);
		//3.清除缓存key:md5(openid)
		//用户的绑定的房间信息：wy.yaosafe.com.house.user.用户id
		String blackUserKey = MD5Encrypt.md5(userList.get(0).getWeixinNo());
		String bindKey = "wy.yaosafe.com.house.user." + userId;
		try {
			memcachedClient.set(blackUserKey, 1, "");
			memcachedClient.set(bindKey, 1, "");
		} catch (Exception e) {
			log.error("清空登录信息放入缓存出错",e);
		}
		return "0";
	}
	
	@ResponseBody
	@RequestMapping(value="/removeBlack")
	public String removeBlack(@RequestParam Long userId){
		if(userId == null){
			return "2";//必填参数为空
		}
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		if(residentialInfo == null){
			//没有权限
			return "1";
		}
		List<Long> userIdList = new ArrayList<>();
		userIdList.add(userId);
		List<EndUserInfo> userList = userDao.getUserByIds(userIdList);
		if(CollectionUtils.isEmpty(userList)){
			return "3";//用户不存在
		}
		//1.解除黑名单
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		userDao.removeBlack(paramMap);
		String blackUserKey = MD5Encrypt.md5(userList.get(0).getWeixinNo());
		try {
			memcachedClient.set(blackUserKey, 1, "");
		} catch (Exception e) {
			log.error("清空登录信息放入缓存出错",e);
		}
		return "0";
	}
}
