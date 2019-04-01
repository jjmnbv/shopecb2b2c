package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Store;
import net.shopec.entity.StoreProductCategory;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 店铺商品分类
 * 
 */
public interface StoreProductCategoryDao extends BaseDao<StoreProductCategory> {

	/**
	 * 查找顶级店铺商品分类
	 * 
	 * @param store
	 *            店铺
	 * @param count
	 *            数量
	 * @return 顶级店铺商品分类
	 */
	List<StoreProductCategory> findRoots(@Param("store")Store store, @Param("count")Integer count);

	/**
	 * 查找上级店铺商品分类
	 * 
	 * @param storeProductCategory
	 *            店铺商品分类
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 上级店铺商品分类
	 */
	List<StoreProductCategory> findParents(@Param("storeProductCategory")StoreProductCategory storeProductCategory, @Param("recursive")boolean recursive, @Param("count")Integer count);

	/**
	 * 查找下级店铺商品分类
	 * 
	 * @param storeProductCategory
	 *            店铺商品分类
	 * @param store
	 *            店铺
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 下级店铺商品分类
	 */
	List<StoreProductCategory> findChildren(@Param("storeProductCategory")StoreProductCategory storeProductCategory, @Param("store")Store store, @Param("recursive")boolean recursive, @Param("count")Integer count);

	/**
	 * 查找店铺商品分类
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 店铺商品分类
	 */
	List<StoreProductCategory> findPage(RowBounds rowBounds, @Param("ew")Wrapper<StoreProductCategory> wrapper, @Param("store")Store store);
}