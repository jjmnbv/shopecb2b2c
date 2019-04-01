package net.shopec.service;

import java.util.List;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.Area;
import net.shopec.entity.AreaFreightConfig;
import net.shopec.entity.ShippingMethod;
import net.shopec.entity.Store;

import com.baomidou.mybatisplus.service.IService;

/**
 * Service - 地区运费配置
 * 
 */
public interface AreaFreightConfigService extends IService<AreaFreightConfig> {

	/**
	 * 查找实体对象
	 * 
	 * @param id
	 *            ID
	 * @return 实体对象，若不存在则返回null
	 */
	AreaFreightConfig find(Long id);
	
	/**
	 * 查找实体对象集合
	 * 
	 * @param ids
	 *            ID
	 * @return 实体对象集合
	 */
	List<AreaFreightConfig> findList(Long... ids);
	
	/**
	 * 保存实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	AreaFreightConfig save(AreaFreightConfig areaFreightConfig);

	/**
	 * 更新实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	AreaFreightConfig update(AreaFreightConfig areaFreightConfig);
	
	/**
	 * 删除实体对象
	 * 
	 * @param ids
	 *            ID
	 */
	void delete(Long... ids);
	
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
	boolean exists(ShippingMethod shippingMethod, Store store, Area area);

	/**
	 * 判断运费配置是否唯一
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
	boolean unique(Long id, ShippingMethod shippingMethod, Store store, Area area);

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
	Page<AreaFreightConfig> findPage(ShippingMethod shippingMethod, Store store, Pageable pageable);

}