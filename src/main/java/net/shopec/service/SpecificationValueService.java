package net.shopec.service;

import java.util.List;

import net.shopec.entity.SpecificationItem;
import net.shopec.entity.SpecificationValue;

/**
 * Service - 规格值
 * 
 */
public interface SpecificationValueService {

	/**
	 * 规格值验证
	 * 
	 * @param specificationItems
	 *            规格项
	 * @param specificationValues
	 *            规格值
	 * @return 验证结果
	 */
	boolean isValid(List<SpecificationItem> specificationItems, List<SpecificationValue> specificationValues);

}