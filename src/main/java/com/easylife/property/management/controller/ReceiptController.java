package com.easylife.property.management.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easylife.property.management.bean.ApplicationUserContext;
import com.easylife.property.management.dao.HouseInfoDao;
import com.easylife.property.management.dao.ManagementFeeDetailDao;
import com.easylife.property.management.model.EndUserInfo;
import com.easylife.property.management.model.HouseInfoModel;
import com.easylife.property.management.model.ManagementFeeDetailModel;
import com.easylife.property.management.model.ResidentialModel;
import com.easylife.property.management.service.ManagementFeeService;
import com.easylife.property.management.service.ResidentialService;
import com.easylife.property.management.utils.DateUtils;
import com.easylife.property.management.vo.HouseInfoVo;

@Controller
public class ReceiptController {

	@Autowired
	private ResidentialService residentialService;
	@Autowired
	private HouseInfoDao houseInfoDao;
	@Autowired
	private ManagementFeeDetailDao managementFeeDetailDao;
	@Autowired
	private ManagementFeeService managementFeeService;
	
	@RequestMapping("/toWroteReceiptInfo")
	public String toWroteReceiptInfo(Model model){
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		model.addAttribute("residentialName", residentialInfo.getResidentialName());
		model.addAttribute("roleId", user.getRoleId());
		model.addAttribute("userName", user.getUserName());
		model.addAttribute("phoneNo", user.getUserPhone());
		//查询小区所有楼栋
		model.addAttribute("buildingIdList",houseInfoDao.getBuildingByResidential(residentialInfo.getResidentialId()));
		return "write_receipt_page";
	}
	
	@RequestMapping("/unwroteInfo")
	@ResponseBody
	public Map<String , Object> unpaidInfo(@RequestParam String buildingId, @RequestParam String roomId){
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
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		HouseInfoVo houseInfoVo = new HouseInfoVo();
		houseInfoVo.setResidentialId(residentialInfo.getResidentialId());
		houseInfoVo.setBuildingId(buildingId);
		houseInfoVo.setRoomId(roomId);
		List<HouseInfoModel> houseList = houseInfoDao.getHouseInfo(houseInfoVo);
		if(CollectionUtils.isEmpty(houseList)){
			resultMap.put("code", "3");
			return resultMap;//管理的小区没有此房间信息
		}
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("houseId", houseList.get(0).getHouseId());
		paramMap.put("isPaid", 0);//已经支付的信息
		paramMap.put("isWroteReceipt", 1);//未开票
		List<ManagementFeeDetailModel> unwroteList = managementFeeDetailDao.getManagementFeePaidInfo(paramMap);
		if(CollectionUtils.isEmpty(unwroteList)){
			resultMap.put("code", "4");
			return resultMap;//票已经全开
		}
		List<String> dateStrList = new ArrayList<>();
		for(ManagementFeeDetailModel feeDetail : unwroteList){
			dateStrList.add(DateUtils.fromatDate(feeDetail.getYear(), feeDetail.getMonth()));
		}
		resultMap.put("code", "0");
		resultMap.put("dateMonth", dateStrList);
		return resultMap;
	}
	
	@RequestMapping("/writeReceipt")
	@ResponseBody
	public String unpaidInfo(@RequestParam String buildingId, @RequestParam String roomId, 
			@RequestParam(value="dateMonthArray[]") String[] dateMonthArray){
		//支付需要严格校验
		if(StringUtils.isEmpty(buildingId)){
			return "1";//楼栋为空
		}
		if(StringUtils.isEmpty(roomId)){
			return "2";//房间号为空
		}
		if(dateMonthArray == null || dateMonthArray.length == 0){
			return "3";//月份为空
		}
		//角色是否有权限
		EndUserInfo user = ApplicationUserContext.getUser();
		if(user.getRoleId() != 3 && user.getRoleId() != 4
				&& user.getRoleId() != 5){
			return "4";//没有权限
		}
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		HouseInfoVo houseInfoVo = new HouseInfoVo();
		houseInfoVo.setResidentialId(residentialInfo.getResidentialId());
		houseInfoVo.setBuildingId(buildingId);
		houseInfoVo.setRoomId(roomId);
		List<HouseInfoModel> houseList = houseInfoDao.getHouseInfo(houseInfoVo);
		//房间是否存在
		if(CollectionUtils.isEmpty(houseList)){
			return "5";//管理的小区没有此房间信息
		}
		//更新，无事务控制，不记录日志
		if(managementFeeService.writeReceipt(residentialInfo.getResidentialId(),
				buildingId, roomId, dateMonthArray)){
			return "0";//成功
		}else{
			return "6";//更新失败
		}
	}
}
