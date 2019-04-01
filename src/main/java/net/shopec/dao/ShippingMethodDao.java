package net.shopec.dao;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.ShippingMethod;

/**
 * Dao - 配送方式
 * 
 */
public interface ShippingMethodDao extends BaseDao<ShippingMethod> {

	/**
	 * 查找配送方式分页
	 * 
	 * @param pageable
	 *            分页
	 * @return 配送方式分页
	 */
	Page<ShippingMethod> findPage(Pageable pageable);
}