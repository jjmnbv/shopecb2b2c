package net.shopec.entity;

import java.io.Serializable;

/**
 * <p>
 * 促销SKU中间表
 * </p>
 *
 * @author 江南红衣
 * @since 2018-05-16
 */
public class PromotionSku implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long giftPromotionsId;

	private Long giftsId;

	public Long getGiftPromotionsId() {
		return giftPromotionsId;
	}

	public void setGiftPromotionsId(Long giftPromotionsId) {
		this.giftPromotionsId = giftPromotionsId;
	}

	public Long getGiftsId() {
		return giftsId;
	}

	public void setGiftsId(Long giftsId) {
		this.giftsId = giftsId;
	}

	@Override
	public String toString() {
		return "PromotionSku{" + "giftPromotionsId=" + giftPromotionsId
				+ ", giftsId=" + giftsId + "}";
	}
}
