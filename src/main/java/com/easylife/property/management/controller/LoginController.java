package com.easylife.property.management.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easylife.property.management.bean.ApplicationUserContext;
import com.easylife.property.management.dao.HouseInfoDao;
import com.easylife.property.management.dao.UserAndHouseDao;
import com.easylife.property.management.model.EndUserInfo;
import com.easylife.property.management.model.GroupUserAndHouseModel;
import com.easylife.property.management.model.HouseInfoModel;
import com.easylife.property.management.model.ResidentialModel;
import com.easylife.property.management.service.ResidentialService;
import com.easylife.property.management.service.SmsService;
import com.easylife.property.management.service.UserService;
import com.easylife.property.management.utils.MD5Encrypt;
import com.easylife.property.management.utils.PhoneNoValidator;
import com.easylife.property.management.vo.HouseInfoVo;

import net.rubyeye.xmemcached.MemcachedClient;

@Controller
public class LoginController {
	private final Logger log = Logger.getLogger(LoginController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private MemcachedClient memcachedClient;
	@Autowired
	private ResidentialService residentialService;
	@Autowired
	private HouseInfoDao houseInfoDao;
	@Autowired
	private UserAndHouseDao userAndHouseDao;
	
	@RequestMapping("/toLogin")
	public String toLoginPage(Map<Object,Object> model){
		return "login/login";
	}
	
	@RequestMapping(value="/getVerificationCode",method=RequestMethod.POST)
	@ResponseBody
	//获取验证码
	public String getVerificationCode(@RequestParam String phoneNo){
		if(StringUtils.isEmpty(phoneNo)){
			return "1";//phoneNo is empty
		}
		//是否是正确的手机号码
		if(!PhoneNoValidator.isMobile(phoneNo)){
			return "4";
		}
		//检查用户是否存在
		if(!userService.isUserExists(phoneNo)){
			return "2";//user is not exits
		}
		//发验证码
		Long nowTime = new Date().getTime();
		Long oldTime = null;
		try {
			oldTime = memcachedClient.get(phoneNo + "time");
		} catch (Exception e) {
			log.error("获取缓存时间失败" , e);
		}
		//间隔小于30s
		if(oldTime != null && (nowTime - oldTime)/1000 < 30){
			return "5";//时间间隔小于30s
		}
		
		//验证码短信：同一个手机号1分钟内不能超过2条，30分钟内不能超过3条，一天不能超过6条。
		if(!smsService.sendSmsToUser(phoneNo)){
			return "3";//send message fail
		}
		return "0";//查收验证码
	}
	
	@RequestMapping("/login")
	@ResponseBody
	public String login(@RequestParam String phoneNo,@RequestParam String verificationCode,
			HttpServletResponse response){
		if(StringUtils.isEmpty(phoneNo) || StringUtils.isEmpty(verificationCode)){
			return "1";//必填参数为空
		}
		//1.查询用户
		EndUserInfo user = userService.getUserByPhone(phoneNo);
		if(user == null){
			return "2";//用户不存在
		}
		//2.用缓存校验短信验证码
		String code = null;
		try {
			code = memcachedClient.get(phoneNo).toString();
		} catch (Exception e) {
			log.error("缓存中获取短信验证码出错", e);
		}
		if(code == null || !verificationCode.equals(code)){
			return "3";//验证码错误
		}
		//3.缓存登录信息
		String mcKey = MD5Encrypt.md5(user.getId().toString());
		try {
			memcachedClient.set(mcKey, 24*60*60, user);
		} catch (Exception e) {
			log.error("登录信息放入缓存出错",e);
		}
		//设置账户cookie
		Cookie uid = new Cookie("uid", mcKey);
		uid.setPath(".123wuye.com");
		response.addCookie(uid);
		return "0";
	}
	
	@RequestMapping("/loginOut")
	@ResponseBody
	public String loginOut(@RequestParam String phoneNo){
		//1.查询用户
		EndUserInfo user = userService.getUserByPhone(phoneNo);
		if(user == null){
			return "1";//用户不存在
		}
		//3.清空缓存登录信息
		String mcKey = MD5Encrypt.md5(user.getId().toString());
		try {
			memcachedClient.set(mcKey, 1, "");
		} catch (Exception e) {
			log.error("清空登录信息放入缓存出错",e);
		}
		return "0";
	}

	@RequestMapping("/toAddUser")
	public String toAddUser(Model model){
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区信息
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		model.addAttribute("residentialName", residentialInfo.getResidentialName());
		model.addAttribute("userName", user.getUserName());
		model.addAttribute("phoneNo", user.getUserPhone());
		model.addAttribute("roleId", user.getRoleId());
		return "add_user_page";
	}
	
	@RequestMapping(value="/addUser", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public String addUser(@ModelAttribute EndUserInfo endUser){
		EndUserInfo user = ApplicationUserContext.getUser();
		if(user.getRoleId() != 5){
			return "1";//用户非管理员，没有权限
		}
		//考虑能否添加后台管理员账号
		ResidentialModel residentialModel = residentialService.getResidentialInfoByUserId(user.getId());
		if(residentialModel == null){
			return "2";//获取用户所在小区失败
		}
		if(userService.insertUser(endUser, residentialModel.getResidentialId())){
			return "0";//添加成功
		}else{
			return "3";//添加失败
		}
	}
	
	@RequestMapping(value="/toUnbindWeixin")
	public String toUnbindWeixin(Model model){
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区信息
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		model.addAttribute("residentialName", residentialInfo.getResidentialName());
		model.addAttribute("userName", user.getUserName());
		model.addAttribute("phoneNo", user.getUserPhone());
		model.addAttribute("roleId", user.getRoleId());
		//查询小区所有楼栋
		model.addAttribute("buildingIdList",houseInfoDao.getBuildingByResidential(residentialInfo.getResidentialId()));
		return "unbind_weixin_page";
	}
	
	@RequestMapping(value="/queryUserBindInfo", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> queryUserBindInfo(@RequestParam String buildingId, 
			@RequestParam String roomId){
		Map<String, Object> resultMap = new HashMap<>();
		if(StringUtils.isEmpty(buildingId)){
			resultMap.put("code", "1");//楼栋为空
			return resultMap;
		}
		if(StringUtils.isEmpty(roomId)){
			resultMap.put("code", "2");
			return resultMap;//房间号为空
		}
		EndUserInfo user = ApplicationUserContext.getUser();
		if(user.getRoleId() != 5){
			resultMap.put("code", "3");
			return resultMap;//用户非管理员，没有权限
		}
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		HouseInfoVo houseInfoVo = new HouseInfoVo();
		houseInfoVo.setResidentialId(residentialInfo.getResidentialId());
		houseInfoVo.setBuildingId(buildingId);
		houseInfoVo.setRoomId(roomId);
		List<HouseInfoModel> houseList = houseInfoDao.getHouseInfo(houseInfoVo);
		if(CollectionUtils.isEmpty(houseList)){
			resultMap.put("code", "4");
			return resultMap;//管理的小区没有此房间信息
		}
		Map<String ,Object> paramMap = new HashMap<>();
		paramMap.put("building", buildingId);
		paramMap.put("houseNumber", roomId);
		List<GroupUserAndHouseModel> userAndHouseList = userAndHouseDao.getHouseWeixinBindInfo(paramMap);
		resultMap.put("code", "0");
		resultMap.put("userList", userAndHouseList);
		return resultMap;
	}
	
	@RequestMapping(value="/unbindUser", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public String queryUserBindInfo(@RequestParam Long id){
		Map<String, Object> resultMap = new HashMap<>();
		if(id == null){
			return "1";//必穿参数为空
		}
		EndUserInfo user = ApplicationUserContext.getUser();
		if(user.getRoleId() != 5){
			return "2";//用户非管理员，没有权限
		}
		//解绑
		Integer resultNum = userAndHouseDao.unbindWeixinFromHouse(id);
		if(resultNum == 1){
			return "0";
		}else{
			return "3";//解绑失败
		}
	}
}
