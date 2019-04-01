package net.shopec.service;

import java.util.List;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.Member;
import net.shopec.entity.Product;
import net.shopec.entity.ProductFavorite;

/**
 * Service - 商品收藏
 * 
 */
public interface ProductFavoriteService extends BaseService<ProductFavorite> {

	/**
	 * 判断商品收藏是否存在
	 * 
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @return 商品收藏是否存在
	 */
	boolean exists(Member member, Product product);

	/**
	 * 查找商品收藏
	 * 
	 * @param member
	 *            会员
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 商品收藏
	 */
	List<ProductFavorite> findList(Member member, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找商品收藏
	 * 
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 商品收藏
	 */
	List<ProductFavorite> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

	/**
	 * 查找商品收藏分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 商品收藏分页
	 */
	Page<ProductFavorite> findPage(Member member, Pageable pageable);

	/**
	 * 查找商品收藏数量
	 * 
	 * @param member
	 *            会员
	 * @return 商品收藏数量
	 */
	Long count(Member member);

}