package net.shopec.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import net.shopec.entity.SpecificationItem;
import net.shopec.entity.SpecificationValue;
import net.shopec.service.SpecificationValueService;

/**
 * Service - 规格值
 * 
 */
@Service
public class SpecificationValueServiceImpl implements SpecificationValueService {

	public boolean isValid(List<SpecificationItem> specificationItems, List<SpecificationValue> specificationValues) {
		Assert.notEmpty(specificationItems, "notEmpty");
		Assert.notEmpty(specificationValues, "notEmpty");

		if (specificationValues.size() != specificationValues.size()) {
			return false;
		}
		for (int i = 0; i < specificationValues.size(); i++) {
			SpecificationItem specificationItem = specificationItems.get(i);
			SpecificationValue specificationValue = specificationValues.get(i);
			if (specificationItem == null || specificationValue == null || !specificationItem.isValid(specificationValue)) {
				return false;
			}
		}
		return true;
	}

}