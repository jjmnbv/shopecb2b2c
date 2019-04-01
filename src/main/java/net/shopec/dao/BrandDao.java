package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Brand;
import net.shopec.entity.ProductCategory;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 品牌
 * 
 */
public interface BrandDao extends BaseDao<Brand> {

	/**
	 * 查找品牌
	 * 
	 * @param productCategory
	 *            商品分类
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 品牌
	 */
	List<Brand> findList(@Param("ew") Wrapper<Brand> wrapper, @Param("productCategory")ProductCategory productCategory);

}