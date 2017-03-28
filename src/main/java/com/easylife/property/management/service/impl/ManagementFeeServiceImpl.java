package com.easylife.property.management.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import com.easylife.property.management.bean.ApplicationUserContext;
import com.easylife.property.management.dao.HouseInfoDao;
import com.easylife.property.management.dao.ManagementFeeDetailDao;
import com.easylife.property.management.dao.ManagementFeeInfoDao;
import com.easylife.property.management.dao.PaymentDetailDao;
import com.easylife.property.management.model.EndUserInfo;
import com.easylife.property.management.model.HouseInfoModel;
import com.easylife.property.management.model.ManagementFeeDetailModel;
import com.easylife.property.management.model.ManagementFeeInfoModel;
import com.easylife.property.management.model.PaymentDetailModel;
import com.easylife.property.management.model.ResidentialModel;
import com.easylife.property.management.service.ManagementFeeService;
import com.easylife.property.management.service.ResidentialService;
import com.easylife.property.management.utils.DateUtils;
import com.easylife.property.management.vo.HouseInfoVo;

public class ManagementFeeServiceImpl implements ManagementFeeService {

	private final Logger log = Logger.getLogger(ManagementFeeServiceImpl.class);
	private ManagementFeeInfoDao managementFeeInfoDao;
	private ResidentialService residentialService;
	private HouseInfoDao houseInfoDao;
	private ManagementFeeDetailDao managementFeeDetailDao;
	private TransactionTemplate transactionTemplate;
	private PaymentDetailDao paymentDetailDao;
	
	@Override
	public Map<String, Object> getHouseManagementFeeInfo(HouseInfoVo houseInfoVo) {
		Map<String, Object> resultMap = new HashMap<>();
		//用户房间号：多少栋多少号
		resultMap.put("buildingId", houseInfoVo.getBuildingId());
		resultMap.put("roomId", houseInfoVo.getRoomId());
		//查询小区名称
		EndUserInfo user = ApplicationUserContext.getUser();
		resultMap.put("roleId", user.getRoleId());
		ResidentialModel residentialInfo = residentialService.getResidentialInfoByUserId(user.getId());
		resultMap.put("residentialName", residentialInfo.getResidentialName());
		//房间面积：平米
		houseInfoVo.setResidentialId(residentialInfo.getResidentialId());
		List<HouseInfoModel> houseInfoModelList = houseInfoDao.getHouseInfo(houseInfoVo);
		if(CollectionUtils.isEmpty(houseInfoModelList)){
			resultMap.put("code", "3");//房间不存在
			return resultMap;
		}
		resultMap.put("houseArea", houseInfoModelList.get(0).getHouseArea());
		//物业费单价：多少元/平
		ManagementFeeInfoModel managementFeeInfo = managementFeeInfoDao.getUnitPriceByResidentialId(residentialInfo.getResidentialId());
		resultMap.put("unitPrice", managementFeeInfo.getUnitPrice());
		//未缴费时间：年月-年月
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("houseId", houseInfoModelList.get(0).getHouseId());
		paramMap.put("isPaid", 1);//未缴费
		List<ManagementFeeDetailModel> feeList = managementFeeDetailDao.getManagementFeePaidInfo(paramMap);
		if(!CollectionUtils.isEmpty(feeList)){
			if(DateUtils.monthBeforeNow(feeList.get(0).getYear(), feeList.get(0).getMonth())){
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				StringBuilder sb = new StringBuilder();
				sb.append(feeList.get(0).getYear())
				.append("年")
				.append(feeList.get(0).getMonth())
				.append("月-")
				.append(c.get(Calendar.YEAR))
				.append("年")
				.append(c.get(Calendar.MONTH) + 1)
				.append("月");
				resultMap.put("time", sb.toString());
				//未缴费用：元
				BigDecimal totalAmount = BigDecimal.ZERO;
				for(ManagementFeeDetailModel fee : feeList){
					//本月之前的物业费累加
					if(DateUtils.monthBeforeNow(fee.getYear(), fee.getMonth())){
						totalAmount = totalAmount.add(fee.getNeedPayManagementFee())
						.setScale(2, BigDecimal.ROUND_HALF_UP);
					}
					//本月的物业费
					if(fee.getYear().intValue() == c.get(Calendar.YEAR)
							&& fee.getMonth().intValue() == c.get(Calendar.MONTH) + 1){
						totalAmount = totalAmount.add(fee.getNeedPayManagementFee())
						.setScale(2, BigDecimal.ROUND_HALF_UP);
					}
				}
				resultMap.put("needPay", totalAmount);
			}else{
				//到今天的物业费已经交齐
			}
		}
		resultMap.put("code", "0");
		return resultMap;
	}

	
	/*************************set*********************************/
	public void setManagementFeeInfoDao(ManagementFeeInfoDao managementFeeInfoDao) {
		this.managementFeeInfoDao = managementFeeInfoDao;
	}

	public void setResidentialService(ResidentialService residentialService) {
		this.residentialService = residentialService;
	}

	public void setHouseInfoDao(HouseInfoDao houseInfoDao) {
		this.houseInfoDao = houseInfoDao;
	}

	public void setManagementFeeDetailDao(ManagementFeeDetailDao managementFeeDetailDao) {
		this.managementFeeDetailDao = managementFeeDetailDao;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public void setPaymentDetailDao(PaymentDetailDao paymentDetailDao) {
		this.paymentDetailDao = paymentDetailDao;
	}


	@Override
	public Boolean payManagementFee(Long residentialId, String buildingId, String roomId, String[] monthArray) {
		//操作人信息
		EndUserInfo user = ApplicationUserContext.getUser();
		//获取要缴费记录id
		HouseInfoVo houseInfoVo = new HouseInfoVo();
		houseInfoVo.setBuildingId(buildingId);
		houseInfoVo.setResidentialId(residentialId);
		houseInfoVo.setRoomId(roomId);
		List<HouseInfoModel> houseInfoList = houseInfoDao.getHouseInfo(houseInfoVo);
		Long houseId = null;
		if(CollectionUtils.isEmpty(houseInfoList)){
			return false;
		}else{
			houseId = houseInfoList.get(0).getHouseId();
//			List<YearAndMonthVo> monthList = new ArrayList<>();
//			for(String yearAndMonth : monthArray){
//				YearAndMonthVo mmonthVo = new YearAndMonthVo();
//				String[] yam = yearAndMonth.split("-");
//				mmonthVo.setYearAndMonthVo(yearAndMonth);
//				mmonthVo.setYear(Integer.parseInt(yam[0]));
//				mmonthVo.setMonth(Integer.parseInt(yam[1]));
//			}
			
		}
		Map<String, Object> paramMap = new HashMap<>();
		String[] yam = monthArray[0].split("-");
		paramMap.put("isPaid", 1);
		paramMap.put("year", Integer.parseInt(yam[0]));
		paramMap.put("month", Integer.parseInt(yam[1]));
		paramMap.put("houseId", houseId);
		List<ManagementFeeDetailModel> feeDetailModelList = managementFeeDetailDao.getManagementFeePaidInfo(paramMap);
		if(CollectionUtils.isEmpty(feeDetailModelList) || feeDetailModelList.size() > 1){
			return false;//数据不对
		}
		//构造入参
		final Long managementFeeDetailId = feeDetailModelList.get(0).getId();
		final Long userId = user.getId();//收钱人的id
		//开启事务，更新缴费，日志management_fee_detail,payment_detail
		Boolean isSuccess = transactionTemplate.execute(new TransactionCallback<Boolean>() {
			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				try {
					Integer updateNum = managementFeeDetailDao.updateManagementFeePaidInfo(managementFeeDetailId);
					if(updateNum != 1){
						status.setRollbackOnly();//回滚事务
						return false;
					}
					PaymentDetailModel paymentDetailModel = new PaymentDetailModel();
					paymentDetailModel.setEndUserId(userId);
					paymentDetailModel.setIsSuccess(0);
					paymentDetailModel.setManagementFeeDetailId(managementFeeDetailId);
					paymentDetailModel.setPayMethodType(2);//现金支付
					Long paymentDetailId = paymentDetailDao.insertPaymentDetail(paymentDetailModel);
					if(paymentDetailId == null){
						status.setRollbackOnly();//回滚事务
						return false;
					}
				} catch (Exception e) {
					log.error("添加用户，插入数据库失败：", e);
					status.setRollbackOnly();//回滚事务
					return false;
				}
				return true;
			}
		});
		if(isSuccess){
			return true;
		}else{
			return false;
		}
		
	}

	@Override
	public Boolean writeReceipt(Long residentialId, String buildingId, String roomId, String[] monthArray) {
		//获取要缴费记录id
		HouseInfoVo houseInfoVo = new HouseInfoVo();
		houseInfoVo.setBuildingId(buildingId);
		houseInfoVo.setResidentialId(residentialId);
		houseInfoVo.setRoomId(roomId);
		List<HouseInfoModel> houseInfoList = houseInfoDao.getHouseInfo(houseInfoVo);
		Long houseId = null;
		if(CollectionUtils.isEmpty(houseInfoList)){
			return false;
		}else{
			houseId = houseInfoList.get(0).getHouseId();
			for(String yearAndMonth : monthArray){
				String[] yam = yearAndMonth.split("-");
				Map<String , Object> paramMap = new HashMap<>();
				paramMap.put("houseId", houseId);
				paramMap.put("year", Integer.parseInt(yam[0]));
				paramMap.put("month", Integer.parseInt(yam[1]));
				managementFeeDetailDao.wroteReceipt(paramMap);
			}
			
		}
		return true;
	}


	@Override
	public Boolean payManagementFeeByMonthes(Long residentialId, String buildingId, String roomId, Integer monthes) {
		//操作人信息
		EndUserInfo user = ApplicationUserContext.getUser();
		//获取要缴费记录id
		HouseInfoVo houseInfoVo = new HouseInfoVo();
		houseInfoVo.setBuildingId(buildingId);
		houseInfoVo.setResidentialId(residentialId);
		houseInfoVo.setRoomId(roomId);
		List<HouseInfoModel> houseInfoList = houseInfoDao.getHouseInfo(houseInfoVo);
		Long houseId = null;
		if(CollectionUtils.isEmpty(houseInfoList)){
			return false;
		}else{
			houseId = houseInfoList.get(0).getHouseId();
		}
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("isPaid", 1);
		paramMap.put("size", monthes);
		paramMap.put("houseId", houseId);
		List<ManagementFeeDetailModel> feeDetailModelList = managementFeeDetailDao.getUnpaidInfoByHouseId(paramMap);
		if(CollectionUtils.isEmpty(feeDetailModelList) || feeDetailModelList.size() != monthes){
			return false;//月数不对
		}
		//构造入参
		final List<Long> managementFeeDetailIdList = new ArrayList<>();
		for(ManagementFeeDetailModel feeModel : feeDetailModelList){
			managementFeeDetailIdList.add(feeModel.getId());
		}
		final Long userId = user.getId();//收钱人的id
		//开启事务，更新缴费，日志management_fee_detail,payment_detail
		Boolean isSuccess = transactionTemplate.execute(new TransactionCallback<Boolean>() {
			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				try {
					Integer updateNum = managementFeeDetailDao.updateManagementFeePaidInfoByIds(managementFeeDetailIdList);
					if(updateNum != managementFeeDetailIdList.size()){
						status.setRollbackOnly();//回滚事务
						return false;
					}
					List<PaymentDetailModel> paymentDetailModelList = new ArrayList<>();
					for(Long managementFeeDetailId : managementFeeDetailIdList){
						PaymentDetailModel paymentDetailModel = new PaymentDetailModel();
						paymentDetailModel.setEndUserId(userId);
						paymentDetailModel.setIsSuccess(0);
						paymentDetailModel.setManagementFeeDetailId(managementFeeDetailId);
						paymentDetailModel.setPayMethodType(2);//现金支付
						paymentDetailModelList.add(paymentDetailModel);
					}
					//记录缴费日志明细
					paymentDetailDao.batchInsertPaymentDetail(paymentDetailModelList);
				} catch (Exception e) {
					log.error("添加用户，插入数据库失败：", e);
					status.setRollbackOnly();//回滚事务
					return false;
				}
				return true;
			}
		});
		if(isSuccess){
			return true;
		}else{
			return false;
		}
	}
	
}
