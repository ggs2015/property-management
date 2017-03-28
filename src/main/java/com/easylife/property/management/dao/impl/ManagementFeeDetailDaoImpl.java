package com.easylife.property.management.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.easylife.property.management.dao.ManagementFeeDetailDao;
import com.easylife.property.management.model.ManagementFeeDetailModel;
import com.easylife.property.management.vo.HistoryPaidInfoVo;
import com.easylife.property.management.vo.PaidStatisticsVo;
import com.easylife.property.management.vo.UnpaidStatisticsVo;

public class ManagementFeeDetailDaoImpl implements ManagementFeeDetailDao {

	private SqlMapClientTemplate sqlMapClientTemplate;
	
	@Override
	public List<ManagementFeeDetailModel> getManagementFeePaidInfo(Map<String, Object> paramMap) {
		return sqlMapClientTemplate.queryForList("getManagementFeePaidInfo", paramMap);
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@Override
	public Integer updateManagementFeePaidInfo(Long id) {
		return sqlMapClientTemplate.update("updateFeeDetail", id);
	}

	@Override
	public Integer wroteReceipt(Map<String, Object> paramMap) {
		return sqlMapClientTemplate.update("wroteReceipt", paramMap);
	}

	@Override
	public List<HistoryPaidInfoVo> getHistoryPaidInfo(Map<String, Object> paramMap) {
		return sqlMapClientTemplate.queryForList("getHistoryPaidInfo", paramMap);
	}

	@Override
	public List<ManagementFeeDetailModel> getManagementFeeDetailByHouseIds(Map<String, Object> paramMap) {
		return sqlMapClientTemplate.queryForList("getManagementFeeDetailByHouseIds", paramMap);
	}

	@Override
	public List<PaidStatisticsVo> getStatisticsPaidInfo(Map<String, Object> paramMap) {
		return sqlMapClientTemplate.queryForList("getStatisticsPaidInfo", paramMap);
	}

	@Override
	public List<UnpaidStatisticsVo> getStatisticsUnpaidInfo(Map<String, Object> paramMap) {
		return sqlMapClientTemplate.queryForList("getStatisticsUnpaidInfo", paramMap);
	}

	@Override
	public List<ManagementFeeDetailModel> getUnpaidInfoByHouseId(Map<String, Object> paramMap) {
		return sqlMapClientTemplate.queryForList("getUnpaidInfoByHouseId", paramMap);
	}

	@Override
	public Integer updateManagementFeePaidInfoByIds(List<Long> ids) {
		return sqlMapClientTemplate.update("updateManagementFeePaidInfoByIds", ids);
	}

}
