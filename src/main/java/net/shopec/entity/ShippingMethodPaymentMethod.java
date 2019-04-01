package net.shopec.entity;

import java.io.Serializable;

/**
 * <p>
 * 配送方式支付方式中间表
 * </p>
 *
 * @author 江南红衣
 * @since 2018-05-21
 */
public class ShippingMethodPaymentMethod implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 配送方式
	 */
	private Long shippingMethodsId;

	/**
	 * 支持支付方式
	 */
	private Long paymentMethodsId;

	public Long getShippingMethodsId() {
		return shippingMethodsId;
	}

	public void setShippingMethodsId(Long shippingMethodsId) {
		this.shippingMethodsId = shippingMethodsId;
	}

	public Long getPaymentMethodsId() {
		return paymentMethodsId;
	}

	public void setPaymentMethodsId(Long paymentMethodsId) {
		this.paymentMethodsId = paymentMethodsId;
	}

	@Override
	public String toString() {
		return "ShippingMethodPaymentMethod{" + "shippingMethodsId="
				+ shippingMethodsId + ", paymentMethodsId=" + paymentMethodsId
				+ "}";
	}
}
