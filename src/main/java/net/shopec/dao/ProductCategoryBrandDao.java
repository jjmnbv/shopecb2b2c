package net.shopec.dao;

import java.util.List;

import net.shopec.entity.ProductCategoryBrand;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品分类品牌中间表 Mapper 接口
 * </p>
 *
 * @author 江南红衣
 * @since 2018-04-26
 */
public interface ProductCategoryBrandDao extends BaseDao<ProductCategoryBrand> {

	/**
	 * 批量保存
	 * @param adminRoles
	 * @return
	 */
	int batchSave(@Param("productCategoryBrands")List<ProductCategoryBrand> productCategoryBrands);
	
}
