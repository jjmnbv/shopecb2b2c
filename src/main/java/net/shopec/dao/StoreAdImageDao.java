package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Store;
import net.shopec.entity.StoreAdImage;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 店铺广告图片
 * 
 */
public interface StoreAdImageDao extends BaseDao<StoreAdImage> {

	/**
	 * 查找店铺广告图片分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页信息
	 * @return 店铺广告图片分页
	 */
	List<StoreAdImage> findPage(RowBounds rowBounds, @Param("ew")Wrapper<StoreAdImage> wrapper, @Param("store")Store store);

}