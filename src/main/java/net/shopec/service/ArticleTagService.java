package net.shopec.service;

import java.util.List;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.entity.ArticleTag;

/**
 * Service - 文章标签
 * 
 */
public interface ArticleTagService extends BaseService<ArticleTag> {

	/**
	 * 查找文章标签
	 * 
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 文章标签
	 */
	List<ArticleTag> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

}