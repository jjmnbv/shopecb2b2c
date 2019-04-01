package net.shopec.service;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.Store;
import net.shopec.entity.StoreAdImage;

/**
 * Service - 店铺广告图片
 * 
 */
public interface StoreAdImageService extends BaseService<StoreAdImage> {

	/**
	 * 查找店铺广告图片分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页信息
	 * @return 店铺广告图片分页
	 */
	Page<StoreAdImage> findPage(Store store, Pageable pageable);

}