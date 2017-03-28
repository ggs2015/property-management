package com.easylife.property.management.dao;

import java.util.List;

import com.easylife.property.management.model.PaymentDetailModel;

public interface PaymentDetailDao {

	Long insertPaymentDetail(PaymentDetailModel paymentDetailModel);
	
	Long batchInsertPaymentDetail(List<PaymentDetailModel> paymentDetailModelList);
}
