package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Store;
import net.shopec.entity.StoreProductTag;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 店铺商品标签
 * 
 */
public interface StoreProductTagDao extends BaseDao<StoreProductTag> {

	/**
	 * 查找店铺商品标签
	 * 
	 * @param store
	 *            店铺
	 * @param isEnabled
	 *            是否启用
	 * @return 店铺商品标签
	 */
	List<StoreProductTag> findList(@Param("store")Store store, @Param("isEnabled")Boolean isEnabled);

	/**
	 * 查找店铺商品标签分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 店铺商品标签分页
	 */
	List<StoreProductTag> findPage(RowBounds rowBounds, @Param("ew")Wrapper<StoreProductTag> wrapper, @Param("store")Store store);

}