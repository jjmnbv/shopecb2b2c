package net.shopec.service;

import java.util.List;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.Store;
import net.shopec.entity.StoreProductTag;

/**
 * Service - 店铺商品标签
 * 
 */
public interface StoreProductTagService extends BaseService<StoreProductTag> {

	/**
	 * 查找店铺商品标签
	 * 
	 * @param store
	 *            店铺
	 * @param isEnabled
	 *            是否启用
	 * @return 店铺商品标签
	 */
	List<StoreProductTag> findList(Store store, Boolean isEnabled);

	/**
	 * 查找店铺商品标签分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 店铺商品标签分页
	 */
	Page<StoreProductTag> findPage(Store store, Pageable pageable);

}