package net.shopec.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;


/**
 * Entity - 序列号
 * 
 */
public class Sn extends BaseEntity<Sn> {

	private static final long serialVersionUID = -2330598144835706164L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum {

		/**
		 * 商品
		 */
		product(0),

		/**
		 * 订单
		 */
		order(1),

		/**
		 * 订单支付
		 */
		orderPayment(2),

		/**
		 * 订单退款
		 */
		orderRefunds(3),

		/**
		 * 订单发货
		 */
		orderShipping(4),

		/**
		 * 订单退货
		 */
		orderReturns(5),

		/**
		 * 支付事务
		 */
		paymentTransaction(6),

		/**
		 * 平台服务
		 */
		platformService(7);

		private int value;

		Type(final int value) {
			this.value = value;
		}
		
		@Override
		public Serializable getValue() {
			return this.value;
		}
	}

	/**
	 * 类型
	 */
	private Sn.Type type;

	/**
	 * 末值
	 */
	private Long lastValue;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public Sn.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Sn.Type type) {
		this.type = type;
	}

	/**
	 * 获取末值
	 * 
	 * @return 末值
	 */
	public Long getLastValue() {
		return lastValue;
	}

	/**
	 * 设置末值
	 * 
	 * @param lastValue
	 *            末值
	 */
	public void setLastValue(Long lastValue) {
		this.lastValue = lastValue;
	}

}