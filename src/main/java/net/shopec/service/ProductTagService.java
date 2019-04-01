package net.shopec.service;

import java.util.List;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.entity.ProductTag;

/**
 * Service - 商品标签
 * 
 */
public interface ProductTagService extends BaseService<ProductTag> {

	/**
	 * 查找商品标签
	 * 
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 商品标签
	 */
	List<ProductTag> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

}