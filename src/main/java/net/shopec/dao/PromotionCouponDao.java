package net.shopec.dao;

import java.util.List;

import net.shopec.entity.PromotionCoupon;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 促销优惠券中间表 Mapper 接口
 * </p>
 *
 * @author 江南红衣
 * @since 2018-05-16
 */
public interface PromotionCouponDao extends BaseDao<PromotionCoupon> {

	/**
	 * 批量保存
	 * @param promotionCoupons
	 * @return
	 */
	int batchSave(@Param("promotionCoupons")List<PromotionCoupon> promotionCoupons);
	
}
