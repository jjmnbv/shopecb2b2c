package net.shopec.entity;

import java.io.Serializable;

/**
 * <p>
 * 订单优惠券中间表
 * </p>
 *
 * @author 江南红衣
 * @since 2018-05-21
 */
public class OrdersCoupon implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 订单
	 */
	private Long ordersId;

	/**
	 * 赠送优惠券
	 */
	private Long couponsId;

	public Long getOrdersId() {
		return ordersId;
	}

	public void setOrdersId(Long ordersId) {
		this.ordersId = ordersId;
	}

	public Long getCouponsId() {
		return couponsId;
	}

	public void setCouponsId(Long couponsId) {
		this.couponsId = couponsId;
	}

	@Override
	public String toString() {
		return "OrdersCoupon{" + "ordersId=" + ordersId + ", couponsId="
				+ couponsId + "}";
	}
}
