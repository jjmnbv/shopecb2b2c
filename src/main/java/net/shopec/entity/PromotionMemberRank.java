package net.shopec.entity;

import java.io.Serializable;

/**
 * <p>
 * 促销会员等级中间表
 * </p>
 *
 * @author 江南红衣
 * @since 2018-05-16
 */
public class PromotionMemberRank implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 促销
	 */
	private Long promotionsId;

	/**
	 * 允许参加会员等级
	 */
	private Long memberRanksId;

	public Long getPromotionsId() {
		return promotionsId;
	}

	public void setPromotionsId(Long promotionsId) {
		this.promotionsId = promotionsId;
	}

	public Long getMemberRanksId() {
		return memberRanksId;
	}

	public void setMemberRanksId(Long memberRanksId) {
		this.memberRanksId = memberRanksId;
	}

	@Override
	public String toString() {
		return "PromotionMemberRank{" + "promotionsId=" + promotionsId
				+ ", memberRanksId=" + memberRanksId + "}";
	}
}
