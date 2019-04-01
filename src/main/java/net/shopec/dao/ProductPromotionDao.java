package net.shopec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.shopec.entity.ProductPromotion;

/**
 * <p>
 * 商品促销中间表 Mapper 接口
 * </p>
 *
 * @author 江南红衣
 * @since 2018-08-13
 */
public interface ProductPromotionDao extends BaseDao<ProductPromotion> {

	/**
	 * 批量保存
	 * @param productCategoryPromotions
	 * @return
	 */
	int batchSave(@Param("productPromotions")List<ProductPromotion> productPromotions);
	
}
