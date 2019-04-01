package net.shopec.entity;

import java.io.Serializable;

/**
 * <p>
 * 促销优惠券中间表
 * </p>
 *
 * @author 江南红衣
 * @since 2018-05-16
 */
public class PromotionCoupon implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 促销
	 */
	private Long promotionsId;

	/**
	 * 赠送优惠券
	 */
	private Long couponsId;

	public Long getPromotionsId() {
		return promotionsId;
	}

	public void setPromotionsId(Long promotionsId) {
		this.promotionsId = promotionsId;
	}

	public Long getCouponsId() {
		return couponsId;
	}

	public void setCouponsId(Long couponsId) {
		this.couponsId = couponsId;
	}

	@Override
	public String toString() {
		return "PromotionCoupon{" + "promotionsId=" + promotionsId + ", couponsId=" + couponsId + "}";
	}
}
