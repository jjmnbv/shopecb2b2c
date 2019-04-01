package net.shopec.dao;

import java.util.List;

import net.shopec.entity.CategoryApplication;
import net.shopec.entity.ProductCategory;
import net.shopec.entity.Store;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 经营分类申请
 * 
 */
public interface CategoryApplicationDao extends BaseDao<CategoryApplication> {

	/**
	 * 查找经营分类申请
	 * 
	 * @param store
	 *            店铺
	 * @param productCategory
	 *            经营分类
	 * @param status
	 *            状态
	 * @return 经营分类申请
	 */
	List<CategoryApplication> findList(@Param("store")Store store, @Param("productCategory")ProductCategory productCategory, @Param("status")CategoryApplication.Status status);

	/**
	 * 查找经营分类申请分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 经营分类申请分页
	 */
	List<CategoryApplication> findPage(RowBounds rowBounds, @Param("ew")Wrapper<CategoryApplication> wrapper, @Param("store")Store store);

}