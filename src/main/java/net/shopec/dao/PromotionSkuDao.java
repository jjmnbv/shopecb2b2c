package net.shopec.dao;

import java.util.List;

import net.shopec.entity.PromotionSku;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 促销SKU中间表 Mapper 接口
 * </p>
 *
 * @author 江南红衣
 * @since 2018-05-16
 */
public interface PromotionSkuDao extends BaseDao<PromotionSku> {

	/**
	 * 批量保存
	 * @param promotionSkus
	 * @return
	 */
	int batchSave(@Param("promotionSkus")List<PromotionSku> promotionSkus);
	
}
