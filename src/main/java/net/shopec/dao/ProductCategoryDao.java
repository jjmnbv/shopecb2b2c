package net.shopec.dao;

import java.util.List;

import net.shopec.entity.ProductCategory;
import net.shopec.entity.Store;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 商品分类
 * 
 */
public interface ProductCategoryDao extends BaseDao<ProductCategory> {

	/**
	 * 查找商品分类
	 * 
	 * @param store
	 *            店铺
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 商品分类
	 */
	List<ProductCategory> findList(@Param("store")Store store, @Param("ew") Wrapper<ProductCategory> wrapper);

	/**
	 * 查找顶级商品分类
	 * 
	 * @param count
	 *            数量
	 * @return 顶级商品分类
	 */
	List<ProductCategory> findRoots(@Param("count")Integer count);

	/**
	 * 查找上级商品分类
	 * 
	 * @param productCategory
	 *            商品分类
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 上级商品分类
	 */
	List<ProductCategory> findParents(@Param("productCategory")ProductCategory productCategory, @Param("recursive")boolean recursive, @Param("count")Integer count);

	/**
	 * 查找下级商品分类
	 * 
	 * @param productCategory
	 *            商品分类
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 下级商品分类
	 */
	List<ProductCategory> findChildren(@Param("productCategory")ProductCategory productCategory, @Param("recursive")boolean recursive, @Param("count")Integer count);

}