package com.easylife.property.management.dao;

import java.util.List;
import java.util.Map;

import com.easylife.property.management.model.ManagementFeeDetailModel;
import com.easylife.property.management.vo.HistoryPaidInfoVo;
import com.easylife.property.management.vo.PaidStatisticsVo;
import com.easylife.property.management.vo.UnpaidStatisticsVo;

public interface ManagementFeeDetailDao {

	/**
	 * @Title: getManagementFeePaidInfo  
	 * @Description: 查询房间交物业费情况
	 * @author liujun
	 * @return
	 */
	List<ManagementFeeDetailModel> getManagementFeePaidInfo(Map<String,Object> paramMap);
	
	/**
	 * @Title: updateManagementFeePaidInfo  
	 * @Description: 根据缴费详情id缴纳物业费
	 * @author liujun
	 * @param id
	 * @return
	 */
	Integer updateManagementFeePaidInfo(Long id);
	
	/**
	 * @Title: updateManagementFeePaidInfoByIds  
	 * @Description: 批量缴费
	 * @author liujun
	 * @param id
	 * @return
	 */
	Integer updateManagementFeePaidInfoByIds(List<Long> ids);
	
	/**
	 * @Title: wroteReceipt  
	 * @Description: 开发票
	 * @author liujun
	 * @param paramMap
	 * @return
	 */
	Integer wroteReceipt(Map<String, Object> paramMap);
	
	/**
	 * @Title: getHistoryPaidInfo  
	 * @Description: 获取历史支付信息
	 * @author liujun
	 * @param paramMap
	 * @return
	 */
	List<HistoryPaidInfoVo> getHistoryPaidInfo(Map<String, Object> paramMap);
	
	/**
	 * @Title: getManagementFeeDetailByHouseIds  
	 * @Description: 获取小区所有缴费情况
	 * @author liujun
	 * @param paramMap
	 * @return
	 */
	List<ManagementFeeDetailModel> getManagementFeeDetailByHouseIds(Map<String,Object> paramMap);
	
	/**
	 * @Title: getStatisticsPaidInfo  
	 * @Description: 统计支付情况
	 * @author liujun
	 * @param paramMap
	 * @return
	 */
	List<PaidStatisticsVo> getStatisticsPaidInfo(Map<String, Object> paramMap);
	
	/**
	 * @Title: getStatisticsUnpaidInfo  
	 * @Description: 未付款的订单记录
	 * @author liujun
	 * @param paramMap
	 * @return
	 */
	List<UnpaidStatisticsVo> getStatisticsUnpaidInfo(Map<String, Object> paramMap);
	
	/**
	 * @Title: getUnpaidInfoByHouseId  
	 * @Description: 查询未付款月份的信息，正排序
	 * @author liujun
	 * @param paramMap
	 * @return
	 */
	List<ManagementFeeDetailModel> getUnpaidInfoByHouseId(Map<String,Object> paramMap);
}
