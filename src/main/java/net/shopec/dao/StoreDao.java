package net.shopec.dao;

import java.util.List;

import net.shopec.entity.CategoryApplication;
import net.shopec.entity.ProductCategory;
import net.shopec.entity.Store;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 店铺
 * 
 */
public interface StoreDao extends BaseDao<Store> {

	/**
	 * 查找店铺
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param isEnabled
	 *            是否启用
	 * @param hasExpired
	 *            是否过期
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @return 店铺
	 */
	List<Store> findList(@Param("ew") Wrapper<Store> wrapper, @Param("type")Store.Type type, @Param("status")Store.Status status, @Param("isEnabled")Boolean isEnabled, @Param("hasExpired")Boolean hasExpired);

	/**
	 * 查找经营分类
	 * 
	 * @param store
	 *            店铺
	 * @param status
	 *            状态
	 * @return 经营分类
	 */
	List<ProductCategory> findProductCategoryList(@Param("store")Store store, @Param("status")CategoryApplication.Status status);

	/**
	 * 查找店铺分页
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param isEnabled
	 *            是否启用
	 * @param hasExpired
	 *            是否过期
	 * @param pageable
	 *            分页信息
	 * @return 店铺分页
	 */
	List<Store> findPage(@Param("type")Store.Type type, @Param("status")Store.Status status, @Param("isEnabled")Boolean isEnabled, @Param("hasExpired")Boolean hasExpired, RowBounds rowBounds, @Param("ew")Wrapper<Store> wrapper);

}