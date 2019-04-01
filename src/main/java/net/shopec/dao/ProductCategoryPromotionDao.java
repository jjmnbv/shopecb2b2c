package net.shopec.dao;

import java.util.List;

import net.shopec.entity.ProductCategoryPromotion;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品分类促销中间表 Mapper 接口
 * </p>
 *
 * @author 江南红衣
 * @since 2018-04-26
 */
public interface ProductCategoryPromotionDao extends BaseDao<ProductCategoryPromotion> {

	/**
	 * 批量保存
	 * @param productCategoryPromotions
	 * @return
	 */
	int batchSave(@Param("productCategoryPromotions")List<ProductCategoryPromotion> productCategoryPromotions);
	
}
