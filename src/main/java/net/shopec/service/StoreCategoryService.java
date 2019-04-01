package net.shopec.service;

import net.shopec.entity.StoreCategory;

/**
 * Service - 店铺分类
 * 
 */
public interface StoreCategoryService extends BaseService<StoreCategory> {

	/**
	 * 判断名称是否存在
	 * 
	 * @param name
	 *            名称(忽略大小写)
	 * @return 名称是否存在
	 */
	boolean nameExists(String name);

}