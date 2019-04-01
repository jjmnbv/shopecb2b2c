package net.shopec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.shopec.entity.OrderItem;

/**
 * Dao - 订单项
 * 
 */
public interface OrderItemDao extends BaseDao<OrderItem> {

	/**
	 * 批量保存
	 * @param orderItems
	 * @return
	 */
	int batchSave(@Param("orderItems") List<OrderItem> orderItems);
	
}