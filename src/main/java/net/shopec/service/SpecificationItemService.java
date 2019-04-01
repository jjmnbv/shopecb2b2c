package net.shopec.service;

import java.util.List;

import net.shopec.entity.SpecificationItem;

/**
 * Service - 规格项
 * 
 */
public interface SpecificationItemService {

	/**
	 * 规格项过滤
	 * 
	 * @param specificationItems
	 *            规格项
	 */
	void filter(List<SpecificationItem> specificationItems);

}