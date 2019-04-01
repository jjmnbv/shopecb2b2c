package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Area;
import net.shopec.entity.AreaFreightConfig;
import net.shopec.entity.ShippingMethod;
import net.shopec.entity.Store;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 地区运费配置
 * 
 */
public interface AreaFreightConfigDao extends BaseDao<AreaFreightConfig> {

	/**
	 * 判断运费配置是否存在
	 * 
	 * @param shippingMethod
	 *            配送方式
	 * @param store
	 *            店铺
	 * @param area
	 *            地区
	 * @return 运费配置是否存在
	 */
	boolean exists(@Param("shippingMethod")ShippingMethod shippingMethod, @Param("store")Store store, @Param("area")Area area);

	/**
	 * 判断运费配置是否存在
	 * 
	 * @param id
	 *            ID
	 * @param shippingMethod
	 *            配送方式
	 * @param area
	 *            地区
	 * @param store
	 *            店铺
	 * @return 运费配置是否唯一
	 */
	boolean unique(@Param("id")Long id, @Param("shippingMethod")ShippingMethod shippingMethod, @Param("store")Store store, @Param("area")Area area);

	/**
	 * 查找运费配置分页
	 * 
	 * @param shippingMethod
	 *            配送方式
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页信息
	 * @return 运费配置分页
	 */
	List<AreaFreightConfig> findPage(RowBounds rowBounds, @Param("ew")Wrapper<AreaFreightConfig> wrapper, @Param("shippingMethod")ShippingMethod shippingMethod, @Param("store")Store store);

}