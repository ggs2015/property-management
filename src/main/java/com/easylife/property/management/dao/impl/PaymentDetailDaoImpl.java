package com.easylife.property.management.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.easylife.property.management.dao.PaymentDetailDao;
import com.easylife.property.management.model.PaymentDetailModel;

public class PaymentDetailDaoImpl implements PaymentDetailDao {

	private SqlMapClientTemplate sqlMapClientTemplate;
	
	@Override
	public Long insertPaymentDetail(PaymentDetailModel paymentDetailModel) {
		return (Long) sqlMapClientTemplate.insert("insertPaymentDetail",paymentDetailModel);
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@Override
	public Long batchInsertPaymentDetail(List<PaymentDetailModel> paymentDetailModelList) {
		return (Long) sqlMapClientTemplate.insert("batchInsertPaymentDetail", paymentDetailModelList);
	}

}
