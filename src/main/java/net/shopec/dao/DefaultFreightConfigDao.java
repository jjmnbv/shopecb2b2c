package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Area;
import net.shopec.entity.DefaultFreightConfig;
import net.shopec.entity.ShippingMethod;
import net.shopec.entity.Store;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 默认运费配置
 * 
 */
public interface DefaultFreightConfigDao extends BaseDao<DefaultFreightConfig> {

	/**
	 * 判断运费配置是否存在
	 * 
	 * @param shippingMethod
	 *            配送方式
	 * @param area
	 *            地区
	 * @return 运费配置是否存在
	 */
	boolean exists(@Param("shippingMethod")ShippingMethod shippingMethod, @Param("area")Area area);

	/**
	 * 查找默认运费配置分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页信息
	 * @return 运费配置分页
	 */
	List<DefaultFreightConfig> findPage(RowBounds rowBounds, @Param("ew")Wrapper<DefaultFreightConfig> wrapper, @Param("store")Store store);

	/**
	 * 查找默认运费配置
	 * 
	 * @param shippingMethod
	 *            配送方式
	 * @param store
	 *            店铺
	 * @return 默认运费配置
	 */
	DefaultFreightConfig findDefault(@Param("shippingMethod")ShippingMethod shippingMethod, @Param("store")Store store);

}