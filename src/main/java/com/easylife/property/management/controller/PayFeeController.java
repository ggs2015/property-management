package com.easylife.property.management.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class PayFeeController {

	@Autowired
	private ResidentialService residentialService;
	@Autowired
	private HouseInfoDao houseInfoDao;
	@Autowired
	private ManagementFeeDetailDao managementFeeDetailDao;
	@Autowired
	private ManagementFeeService managementFeeService;
	
	@RequestMapping("/toUnpaidInfo")
	public String toUnpaidInfo(Model model){
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		model.addAttribute("residentialName", residentialInfo.getResidentialName());
		model.addAttribute("roleId", user.getRoleId());
		model.addAttribute("userName", user.getUserName());
		model.addAttribute("phoneNo", user.getUserPhone());
		//查询小区所有楼栋
		model.addAttribute("buildingIdList",houseInfoDao.getBuildingByResidential(residentialInfo.getResidentialId()));
		return "pay_fee_page";
	}
	
	@RequestMapping("/getHouseIdByBuilding")
	@ResponseBody
	public List<String> getHouseIdByBuilding(@RequestParam String buildingId){
		List<String> roomIdList = new ArrayList<>();
		if(StringUtils.isEmpty(buildingId)){
			return roomIdList;
		}
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		HouseInfoVo houseInfoVo = new HouseInfoVo();
		houseInfoVo.setResidentialId(residentialInfo.getResidentialId());
		houseInfoVo.setBuildingId(buildingId);
		List<HouseInfoModel> houseInfoModelList = houseInfoDao.getHouseInfo(houseInfoVo);
		if(!CollectionUtils.isEmpty(houseInfoModelList)){
			for(HouseInfoModel house : houseInfoModelList){
				roomIdList.add(house.getHouseNumber());
			}
		}
		return roomIdList;
	}
	
	@RequestMapping("/unpaidInfo")
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
		paramMap.put("isPaid", 1);//未支付的信息
		//今年1月-今天
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		Integer year = c.get(Calendar.YEAR);
		Integer month = c.get(Calendar.MONTH) + 1;
		paramMap.put("year", year);//未支付的信息
		paramMap.put("compareMonth", month);//未支付的信息
		List<ManagementFeeDetailModel> unpaidFeeList = managementFeeDetailDao.getManagementFeePaidInfo(paramMap);
		if(CollectionUtils.isEmpty(unpaidFeeList)){
			resultMap.put("code", "4");
			return resultMap;//已经交清
		}
		List<String> dateStrList = new ArrayList<>();
		List<BigDecimal> monthUnpaidFeeList = new ArrayList<>();
		for(ManagementFeeDetailModel feeDetail : unpaidFeeList){
			dateStrList.add(DateUtils.fromatDate(feeDetail.getYear(), feeDetail.getMonth()));
			monthUnpaidFeeList.add(feeDetail.getNeedPayManagementFee());
		}
		resultMap.put("code", "0");
		resultMap.put("dateMonth", dateStrList);
		resultMap.put("monthUnpaidFeeList", monthUnpaidFeeList);
		return resultMap;
	}
	
	@RequestMapping("/payFee")
	@ResponseBody
	public String payFee(@RequestParam String buildingId, @RequestParam String roomId, 
			@RequestParam(value = "dateMonthArray[]") String[] dateMonthArray){
		//支付需要严格校验
		if(StringUtils.isEmpty(buildingId)){
			return "1";//楼栋为空
		}
		if(StringUtils.isEmpty(roomId)){
			return "2";//房间号为空
		}
		if(dateMonthArray == null || dateMonthArray.length == 0
				|| dateMonthArray.length > 1){//目前只能一个月交一次
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
		//更新，事务控制，记录日志
		if(managementFeeService.payManagementFee(residentialInfo.getResidentialId(),
				buildingId, roomId, dateMonthArray)){
			return "0";//成功
		}else{
			return "6";//更新失败
		}
	}
	
	
	@RequestMapping("/payFeeByMonthes")
	@ResponseBody
	public String payFeeByMonthes(@RequestParam String buildingId, @RequestParam String roomId, 
			@RequestParam Integer monthes){
		//支付需要严格校验
		if(StringUtils.isEmpty(buildingId)){
			return "1";//楼栋为空
		}
		if(StringUtils.isEmpty(roomId)){
			return "2";//房间号为空
		}
		if(monthes == null || monthes <= 0
				|| monthes > 12){//一次最多只能交12个月
			return "3";//月数错误
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
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("houseId", houseList.get(0).getHouseId());
		paramMap.put("isPaid", 1);//未支付的信息
		//今年1月-今天
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		Integer year = c.get(Calendar.YEAR);
		Integer month = c.get(Calendar.MONTH) + 1;
		paramMap.put("year", year);//未支付的信息
		paramMap.put("compareMonth", month);//未支付的信息
		List<ManagementFeeDetailModel> unpaidFeeList = managementFeeDetailDao.getManagementFeePaidInfo(paramMap);
		if(CollectionUtils.isEmpty(unpaidFeeList)){
			return "7";//已经交清
		}else if(monthes > unpaidFeeList.size()){
			return "8";//月份太多
		}
				
		//更新，事务控制，记录日志
		if(managementFeeService.payManagementFeeByMonthes(residentialInfo.getResidentialId(),
				buildingId, roomId, monthes)){
			return "0";//成功
		}else{
			return "6";//更新失败
		}
	}
}
