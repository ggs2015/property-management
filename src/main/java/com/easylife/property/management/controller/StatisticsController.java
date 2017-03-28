package com.easylife.property.management.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.easylife.property.management.dao.ManagementFeeDetailDao;
import com.easylife.property.management.dao.ManagementFeeInfoDao;
import com.easylife.property.management.dao.UserDao;
import com.easylife.property.management.model.EndUserInfo;
import com.easylife.property.management.model.HouseInfoModel;
import com.easylife.property.management.model.ManagementFeeDetailModel;
import com.easylife.property.management.model.ManagementFeeInfoModel;
import com.easylife.property.management.model.RepairModel;
import com.easylife.property.management.model.ResidentialModel;
import com.easylife.property.management.service.ResidentialService;
import com.easylife.property.management.vo.HistoryPaidInfoVo;
import com.easylife.property.management.vo.HouseInfoVo;
import com.easylife.property.management.vo.MonthFeeStatisticsVo;
import com.easylife.property.management.vo.PaidStatisticsVo;
import com.easylife.property.management.vo.UnpaidStatisticsVo;

@Controller
public class StatisticsController {

	@Autowired
	private ResidentialService residentialService;
	@Autowired
	private HouseInfoDao houseInfoDao;
	@Autowired
	private ManagementFeeDetailDao managementFeeDetailDao;
	@Autowired
	private ManagementFeeInfoDao managementFeeInfoDao;
	@Autowired
	private UserDao userDao;
	private final static Logger log = Logger.getLogger(StatisticsController.class);
	
	@RequestMapping("/toShowHistoryPayRecord")
	public String toShowHistoryPayRecord(Model model){
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		model.addAttribute("residentialName", residentialInfo.getResidentialName());
		model.addAttribute("roleId", user.getRoleId());
		model.addAttribute("userName", user.getUserName());
		model.addAttribute("phoneNo", user.getUserPhone());
		if(user.getRoleId() != 3 && user.getRoleId() != 4
				&& user.getRoleId() != 5){
			return "no_authority";//没有权限
		}
		//查询小区所有楼栋
		model.addAttribute("buildingIdList",houseInfoDao.getBuildingByResidential(residentialInfo.getResidentialId()));
		return "history_paid_record_page";
	}
	
	@RequestMapping("/historyPayRecord")
	@ResponseBody
	public Map<String , Object> historyPayRecord(@RequestParam String buildingId, 
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
		//查询房间缴费信息
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("houseId", houseList.get(0).getHouseId());
		paramMap.put("isPaid", 0);//已经支付的信息
		List<HistoryPaidInfoVo> paidInfoList = managementFeeDetailDao.getHistoryPaidInfo(paramMap);
		if(!CollectionUtils.isEmpty(paidInfoList)){
			List<Long> userIdList = new ArrayList<>();
			//格式化日期
			for(HistoryPaidInfoVo vo : paidInfoList){
				userIdList.add(vo.getEndUserId());
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				vo.setTimeStr(sf.format(vo.getCreateTime()));
			}
			List<EndUserInfo> userList = userDao.getUserByIds(userIdList);
			if(!CollectionUtils.isEmpty(userList)){
				Map<Long, EndUserInfo> userMap = new HashMap<>();
				for(EndUserInfo userInfo : userList){
					userMap.put(userInfo.getId(), userInfo);
				}
				for(HistoryPaidInfoVo historyPaidInfoVo : paidInfoList){
					Long userId = historyPaidInfoVo.getEndUserId();
					EndUserInfo endUserInfo = userMap.get(userId);
					if(endUserInfo != null){
						historyPaidInfoVo.setUserPhone(endUserInfo.getUserPhone());
						historyPaidInfoVo.setUserName(endUserInfo.getUserName());
					}
				}
			}
			
		}
		resultMap.put("paidInfoList", paidInfoList);
		resultMap.put("code", "0");
		return resultMap;
	}
	//缴费全量
	@RequestMapping("/totalInfo")
	public String totalInfo(Model model){
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		model.addAttribute("residentialName", residentialInfo.getResidentialName());
		model.addAttribute("roleId", user.getRoleId());
		model.addAttribute("userName", user.getUserName());
		model.addAttribute("phoneNo", user.getUserPhone());
		//查询所有户数
		List<HouseInfoModel> houseList = houseInfoDao.countHouseByResidentialId(residentialInfo.getResidentialId());
		model.addAttribute("houseCount", houseList.size());
		//查询每月应收款
		BigDecimal totalFeePerMonth = BigDecimal.ZERO;
		ManagementFeeInfoModel managementFeeInfo = managementFeeInfoDao.getUnitPriceByResidentialId(residentialInfo.getResidentialId());
		List<Long> houseIdList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(houseList)){
			BigDecimal totalArea = BigDecimal.ZERO;
			for(HouseInfoModel houseInfo : houseList){
				houseIdList.add(houseInfo.getHouseId());
				totalArea = totalArea.add(houseInfo.getHouseArea()).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			totalFeePerMonth = totalArea.multiply(managementFeeInfo.getUnitPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
			model.addAttribute("totalFeePerMonth", totalFeePerMonth);
		}
		//今年1月-今天
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		Integer year = c.get(Calendar.YEAR);
		Integer month = c.get(Calendar.MONTH) + 1;
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		//应收物业费，实收物业费，待收物业费，完成率
		List<ManagementFeeDetailModel> feeList = null;
		if(!CollectionUtils.isEmpty(houseIdList)){
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("houseIds", houseIdList);
			paramMap.put("year", year);//只统计当年的
			feeList = managementFeeDetailDao.getManagementFeeDetailByHouseIds(paramMap);
		}
		//应收物业费
		BigDecimal needGetFee = totalFeePerMonth.multiply(new BigDecimal(month)).setScale(2, BigDecimal.ROUND_HALF_UP);
		model.addAttribute("needGetFee", needGetFee);
		BigDecimal paidFee = BigDecimal.ZERO;
		if(!CollectionUtils.isEmpty(feeList)){
			for(ManagementFeeDetailModel feeInfo : feeList){
				if(feeInfo.getIsPaid() == 0 && feeInfo.getMonth() <= month){
					paidFee = paidFee.add(feeInfo.getNeedPayManagementFee()).setScale(2, BigDecimal.ROUND_HALF_UP);
				}
			}
		}
		model.addAttribute("paidFee", paidFee);
		BigDecimal needToFee = needGetFee.subtract(paidFee).setScale(2, BigDecimal.ROUND_HALF_UP);
		model.addAttribute("needToFee", needToFee);
		BigDecimal payRate = paidFee.divide(needGetFee,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
		model.addAttribute("payRate", payRate);
		//按月份统计信息
		List<MonthFeeStatisticsVo> monthFeeStatisticsVoList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(feeList)){
			for(int i=1; i<= month; i++){
				MonthFeeStatisticsVo statisticVo = new MonthFeeStatisticsVo();
				statisticVo.setTotalToPay(totalFeePerMonth);
				statisticVo.setYearAndMonth(year + "." + i);
				BigDecimal paidFeeByMonth = BigDecimal.ZERO;
				BigDecimal weiChartPaid = BigDecimal.ZERO;
				BigDecimal cashPaid = BigDecimal.ZERO;
				BigDecimal needToPay = BigDecimal.ZERO;
				BigDecimal monthPayRate = BigDecimal.ZERO;
				for(ManagementFeeDetailModel feeDetail : feeList){
					if(feeDetail.getMonth() == i && feeDetail.getIsPaid() == 0){
						paidFeeByMonth = paidFeeByMonth.add(feeDetail.getNeedPayManagementFee()).setScale(2, BigDecimal.ROUND_HALF_UP);
						if(feeDetail.getPayMethodType() == null){
							//缴费方式不祥
							continue;
						}
						if(feeDetail.getPayMethodType() == 1){//微信支付
							weiChartPaid = weiChartPaid.add(feeDetail.getNeedPayManagementFee()).setScale(2, BigDecimal.ROUND_HALF_UP);
						}
						if(feeDetail.getPayMethodType() == 2){//现金支付
							cashPaid = cashPaid.add(feeDetail.getNeedPayManagementFee()).setScale(2, BigDecimal.ROUND_HALF_UP);
						}
					}
				}
				needToPay = totalFeePerMonth.subtract(paidFeeByMonth).setScale(2, BigDecimal.ROUND_HALF_UP);
				monthPayRate = paidFeeByMonth.divide(totalFeePerMonth,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
				statisticVo.setCashPaid(cashPaid);
				statisticVo.setWeiChartPaid(weiChartPaid);
				statisticVo.setPayRate(monthPayRate);
				statisticVo.setNeedToPay(needToPay);
				statisticVo.setPaidFee(paidFeeByMonth);
				monthFeeStatisticsVoList.add(statisticVo);
			}
		}
		model.addAttribute("monthFeeStatisticsVoList", monthFeeStatisticsVoList);
		return "statistic/month_pay_statistic_page";
	}
	
	//缴费明细
	@RequestMapping("/toStatisticsPaidInfo")
	public String toStatisticsPaidInfo(Model model){
		//权限
		//角色是否有权限
		EndUserInfo user = ApplicationUserContext.getUser();
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		model.addAttribute("residentialName", residentialInfo.getResidentialName());
		model.addAttribute("roleId", user.getRoleId());
		model.addAttribute("userName", user.getUserName());
		model.addAttribute("phoneNo", user.getUserPhone());
		if(user.getRoleId() != 3 && user.getRoleId() != 4
				&& user.getRoleId() != 5){
			return "no_authority";//没有权限
		}
		 return "statistic/pay_detail_statistic_page";
	}
	
	@RequestMapping("/statisticsPaidInfo")
	@ResponseBody
	public Map<String, Object> statisticsPaidInfo(@RequestParam String dateStr, 
			@RequestParam Integer payMethodType){
		Map<String, Object> resultMap = new HashMap<>();
		EndUserInfo user = ApplicationUserContext.getUser();
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		//角色是否有权限
		if(user.getRoleId() != 3 && user.getRoleId() != 4
				&& user.getRoleId() != 5){
			resultMap.put("code", 4);
			return resultMap;//没有权限
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] date = null;
		if(StringUtils.isEmpty(dateStr)){
			resultMap.put("code", 1);//时间参数为空
			return resultMap;
		}else{
			dateStr = dateStr.replaceAll(" - ", "%");
			date = dateStr.split("%");
			Date startTime = null;
			Date endTime = null;
			try {
				startTime = format.parse(date[0]);
				endTime = format.parse(date[1]);
			} catch (ParseException e) {
				log.error("时间参数不对" , e);
				resultMap.put("code", 2);//时间参数不对
				return resultMap;
			}
			if(startTime == null || endTime == null){
				resultMap.put("code", 2);//时间参数不对
				return resultMap;
			}
			long intervalMilli = endTime.getTime() - startTime.getTime();
			if(intervalMilli / (24 * 60 * 60 * 1000) > 7){
				if(startTime == null || endTime == null){
					resultMap.put("code", 3);//时间差超过7天
					return resultMap;
				}
			}
		}
		//查询一段时间内的支付情况
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("residentialAreaId", residentialInfo.getResidentialId());
		paramMap.put("startTime", date[0]);
		paramMap.put("endTime", date[1] + " 59:59:59");
		if(payMethodType != null){
			paramMap.put("payMethodType", payMethodType);
		}
		List<PaidStatisticsVo> paidList = managementFeeDetailDao.getStatisticsPaidInfo(paramMap);
		//将id转换成人名
		if(!CollectionUtils.isEmpty(paidList)){
			List<Long> endUserIdList = new ArrayList<>();
			for(PaidStatisticsVo paidVo : paidList){
				endUserIdList.add(paidVo.getEndUserId());
			}
			List<EndUserInfo> userList = userDao.getUserByIds(endUserIdList);
			Map<Long,String> userMap = new HashMap<>();
			for(EndUserInfo userInfo : userList){
				userMap.put(userInfo.getId(), userInfo.getUserName());
			}
			for(PaidStatisticsVo paidVo : paidList){
				paidVo.setEndUserName(userMap.get(paidVo.getEndUserId()));
				paidVo.setPaidTime(formatTime.format(paidVo.getCreateTime()));
			}
		}
		paramMap.put("paidList", paidList);
		paramMap.put("code", 0);
		return paramMap;
	}
	
	//欠费明细
	@RequestMapping("/statisticsUnpaidInfo")
	public String statisticsUnpaidInfo(Model model){
		EndUserInfo user = ApplicationUserContext.getUser();
		if(user.getRoleId() != 3 && user.getRoleId() != 4
				&& user.getRoleId() != 5){
			return "no_authority";//没有权限
		}
		//查询小区名称
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		model.addAttribute("residentialName", residentialInfo.getResidentialName());
		model.addAttribute("roleId", user.getRoleId());
		model.addAttribute("userName", user.getUserName());
		model.addAttribute("phoneNo", user.getUserPhone());
		//今年1月-今天
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		Integer year = c.get(Calendar.YEAR);
		Integer month = c.get(Calendar.MONTH) + 1;
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		Map<String, Object> paramMap =  new HashMap<>();
		paramMap.put("residentialAreaId", residentialInfo.getResidentialId());
		paramMap.put("year", year);
		paramMap.put("month", month);
		List<UnpaidStatisticsVo> unpaidList = managementFeeDetailDao.getStatisticsUnpaidInfo(paramMap);
		//按房间号统计
		Map<String, UnpaidStatisticsVo> totalMap = new HashMap<>();
		if(!CollectionUtils.isEmpty(unpaidList)){
			for(UnpaidStatisticsVo unpaid : unpaidList){
				String key = unpaid.getBuilding() + "-" + unpaid.getHouseNumber();
				UnpaidStatisticsVo unpaidVo = totalMap.get(key);
				if(unpaidVo != null){
					Integer totalMonths = unpaidVo.getMonths();
					unpaidVo.setMonths(totalMonths+1);
					BigDecimal needToPay = unpaidVo.getNeedPayManagementFee();
					needToPay = needToPay.add(unpaid.getNeedPayManagementFee()).setScale(2, BigDecimal.ROUND_HALF_UP);
					unpaidVo.setNeedPayManagementFee(needToPay);
				}else{
					unpaid.setMonths(1);
					totalMap.put(key, unpaid);
				}
			}
		}
		model.addAttribute("unpaidListMap", totalMap);
		model.addAttribute("userTotal", totalMap.size());
		BigDecimal totalUnpaid = BigDecimal.ZERO;
		if(totalMap.size() > 0){
			for(Entry<String, UnpaidStatisticsVo> entry : totalMap.entrySet()){
				totalUnpaid = totalUnpaid.add(entry.getValue().getNeedPayManagementFee()).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		}
		model.addAttribute("totalUnpaid", totalUnpaid);
		return "statistic/unpaid_detail_statistic_page";
	}
}
