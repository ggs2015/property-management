package com.easylife.property.management.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easylife.property.management.bean.ApplicationUserContext;
import com.easylife.property.management.dao.HouseInfoDao;
import com.easylife.property.management.model.EndUserInfo;
import com.easylife.property.management.model.ResidentialModel;
import com.easylife.property.management.service.ManagementFeeService;
import com.easylife.property.management.service.ResidentialService;
import com.easylife.property.management.vo.HouseInfoVo;

@Controller
public class QueryFeeController {

	private final Logger log = Logger.getLogger(QueryFeeController.class);
	@Autowired
	private ResidentialService residentialService;
	@Autowired
	private HouseInfoDao houseInfoDao;
	@Autowired
	private ManagementFeeService managementFeeService;
	
	@RequestMapping("/queryFee")
	public String queryFee(Model model){
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		model.addAttribute("residentialName", residentialInfo.getResidentialName());
		model.addAttribute("roleId", user.getRoleId());
		model.addAttribute("userName", user.getUserName());
		model.addAttribute("phoneNo", user.getUserPhone());
		//查询小区所有楼栋
		model.addAttribute("buildingIdList",houseInfoDao.getBuildingByResidential(residentialInfo.getResidentialId()));
		return "query_page";
	}
	
	@RequestMapping("/queryInfo")
	@ResponseBody
	public Map<String,Object> queryInfo(@RequestParam String buildingId, @RequestParam String roomId){
		Map<String, Object> resultMap = new HashMap<>();
		if(StringUtils.isEmpty(buildingId)){
			resultMap.put("code", "1");
			return resultMap;//必填参数为空，楼栋
		}
		if(StringUtils.isEmpty(roomId)){
			resultMap.put("code", "2");
			return resultMap;//必填参数为空，房间号
		}
		HouseInfoVo houseInfoVo = new HouseInfoVo();
		houseInfoVo.setBuildingId(buildingId);
		houseInfoVo.setRoomId(roomId);
		resultMap.putAll(managementFeeService.getHouseManagementFeeInfo(houseInfoVo));
		return resultMap;
	}
}
