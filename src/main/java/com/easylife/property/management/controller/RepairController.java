package com.easylife.property.management.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easylife.property.management.bean.ApplicationUserContext;
import com.easylife.property.management.dao.HouseInfoDao;
import com.easylife.property.management.dao.RepairDao;
import com.easylife.property.management.dao.UserAndHouseDao;
import com.easylife.property.management.dao.UserDao;
import com.easylife.property.management.model.EndUserInfo;
import com.easylife.property.management.model.GroupUserAndHouseModel;
import com.easylife.property.management.model.RepairModel;
import com.easylife.property.management.model.ResidentialModel;
import com.easylife.property.management.service.ResidentialService;

@Controller
public class RepairController {

	@Autowired
	private RepairDao repairDao;
	@Autowired
	private ResidentialService residentialService;
	@Autowired
	private HouseInfoDao houseInfoDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserAndHouseDao userAndHouseDao;
	
	@RequestMapping("/toRepair")
	public String toRepair(Model model){
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		model.addAttribute("residentialName", residentialInfo.getResidentialName());
		model.addAttribute("roleId", user.getRoleId());
		model.addAttribute("userName", user.getUserName());
		model.addAttribute("phoneNo", user.getUserPhone());
		//查询小区所有楼栋
		model.addAttribute("buildingIdList",houseInfoDao.getBuildingByResidential(residentialInfo.getResidentialId()));
		return "repair_page";
	}
	
	@ResponseBody
	@RequestMapping("/queryRepairInfo")
	public Map<String, Object> queryRepairInfo(@RequestParam Integer status){
		Map<String, Object> resultMap = new HashMap<>();
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("status", status);
		List<RepairModel> repairInfoList = repairDao.queryRepairInfo(paramMap);
		if(!CollectionUtils.isEmpty(repairInfoList)){
			List<Long> idList = new ArrayList<>();
			List<Long> userIdList = new ArrayList<>();
			for(RepairModel repairModel : repairInfoList){
				idList.add(repairModel.getOperatorId());
				userIdList.add(repairModel.getUserId());
			}
			List<EndUserInfo> userList = userDao.getUserByIds(idList);
			if(!CollectionUtils.isEmpty(userList)){
				Map<Long, EndUserInfo> userMap = new HashMap<>();
				for(EndUserInfo userInfo : userList){
					userMap.put(userInfo.getId(), userInfo);
				}
				for(RepairModel repairModel : repairInfoList){
					Long userId = repairModel.getOperatorId();
					EndUserInfo endUserInfo = userMap.get(userId);
					if(endUserInfo != null){
						repairModel.setOperatorPhone(endUserInfo.getUserPhone());
						repairModel.setOperator(endUserInfo.getUserName());
					}
				}
			}
			List<GroupUserAndHouseModel> userHouseInfoList = userAndHouseDao.getHouseInfoByUserId(userIdList);
			if(!CollectionUtils.isEmpty(userHouseInfoList)){
				Map<Long, GroupUserAndHouseModel> userHouseMap = new HashMap<>();
				for(GroupUserAndHouseModel userHouse : userHouseInfoList){
					userHouseMap.put(userHouse.getEndUserId(), userHouse);
				}
				for(RepairModel repairModel : repairInfoList){
					Long userId = repairModel.getUserId();
					GroupUserAndHouseModel userHouse = userHouseMap.get(userId);
					if(userHouse != null){
						repairModel.setBuilding(userHouse.getBuilding());
						repairModel.setRoomNo(userHouse.getHouseNumber());
					}
				}
			}
		}
		resultMap.put("code", 0);
		resultMap.put("status", status);
		resultMap.put("repairList", repairInfoList);
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping("/repair")
	public String repair(@RequestParam Long id){
		EndUserInfo user = ApplicationUserContext.getUser();
		Map<String ,Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		paramMap.put("operatorId", user.getId());
		repairDao.updateRepairInfo(paramMap);
		return "0";
	}
}
