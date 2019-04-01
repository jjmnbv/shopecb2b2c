package net.shopec.dao;

import java.util.List;

import net.shopec.entity.DeliveryCenter;
import net.shopec.entity.Store;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 发货点
 * 
 */
public interface DeliveryCenterDao extends BaseDao<DeliveryCenter> {

	/**
	 * 查找默认发货点
	 * 
	 * @param store
	 *            店铺
	 * @return 默认发货点，若不存在则返回null
	 */
	DeliveryCenter findDefault(@Param("store")Store store);

	/**
	 * 清除默认
	 * 
	 * @param store
	 *            店铺
	 */
	void clearDefault(@Param("store")Store store);

	/**
	 * 清除默认
	 * 
	 * @param exclude
	 *            排除发货点
	 */
	void clearDefaultExclude(@Param("exclude")DeliveryCenter exclude);

	/**
	 * 查找发货点分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 发货点分页
	 */
	List<DeliveryCenter> findPage(RowBounds rowBounds, @Param("ew")Wrapper<DeliveryCenter> wrapper, @Param("store")Store store);

	/**
	 * 查找发货点
	 * 
	 * @param store
	 *            店铺
	 * @return 发货点
	 */
	List<DeliveryCenter> findAll(@Param("store")Store store);

}