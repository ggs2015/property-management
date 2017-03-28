package com.easylife.property.management.service;

import com.easylife.property.management.model.ResidentialModel;

public interface ResidentialService {

	ResidentialModel getResidentialInfoByUserId(Long endUserId);
}
