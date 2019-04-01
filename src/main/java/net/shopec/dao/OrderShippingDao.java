package net.shopec.dao;

import org.apache.ibatis.annotations.Param;

import net.shopec.entity.Order;
import net.shopec.entity.OrderShipping;

/**
 * Dao - 订单发货
 * 
 */
public interface OrderShippingDao extends BaseDao<OrderShipping> {

	/**
	 * 查找最后一条订单发货
	 * 
	 * @param order
	 *            订单
	 * @return 订单发货，若不存在则返回null
	 */
	OrderShipping findLast(@Param("order")Order order);

}