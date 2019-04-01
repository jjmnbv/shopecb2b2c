package net.shopec.dao;

import java.util.List;

import net.shopec.entity.DeliveryTemplate;
import net.shopec.entity.Store;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 快递单模板
 * 
 */
public interface DeliveryTemplateDao extends BaseDao<DeliveryTemplate> {

	/**
	 * 查找默认快递单模板
	 * 
	 * @param store
	 *            店铺
	 * @return 默认快递单模板，若不存在则返回null
	 */
	DeliveryTemplate findDefault(@Param("store")Store store);

	/**
	 * 查找快递单模板
	 * 
	 * @param store
	 *            店铺
	 * @return 快递单模板
	 */
	List<DeliveryTemplate> findList(@Param("store")Store store);

	/**
	 * 查找快递单模板分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页信息
	 * @return 快递单模板分页
	 */
	List<DeliveryTemplate> findPage(RowBounds rowBounds, @Param("ew")Wrapper<DeliveryTemplate> wrapper, @Param("store")Store store);

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
	 *            排除快递单模板
	 */
	void clearDefaultExclude(@Param("exclude")DeliveryTemplate exclude);

}