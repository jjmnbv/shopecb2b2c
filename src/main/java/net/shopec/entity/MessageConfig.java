package net.shopec.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;


/**
 * Entity - 消息配置
 * 
 */
public class MessageConfig extends BaseEntity<MessageConfig> {

	private static final long serialVersionUID = -5214678967755261831L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum {

		/**
		 * 会员注册
		 */
		registerMember(0),

		/**
		 * 订单创建
		 */
		createOrder(1),

		/**
		 * 订单更新
		 */
		updateOrder(2),

		/**
		 * 订单取消
		 */
		cancelOrder(3),

		/**
		 * 订单审核
		 */
		reviewOrder(4),

		/**
		 * 订单收款
		 */
		paymentOrder(5),

		/**
		 * 订单退款
		 */
		refundsOrder(6),

		/**
		 * 订单发货
		 */
		shippingOrder(7),

		/**
		 * 订单退货
		 */
		returnsOrder(8),

		/**
		 * 订单收货
		 */
		receiveOrder(9),

		/**
		 * 订单完成
		 */
		completeOrder(10),

		/**
		 * 订单失败
		 */
		failOrder(11),

		/**
		 * 商家注册
		 */
		registerBusiness(12),

		/**
		 * 店铺审核成功
		 */
		approvalStore(13),

		/**
		 * 店铺审核失败
		 */
		failStore(14);

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
	private MessageConfig.Type type;

	/**
	 * 是否启用邮件
	 */
	private Boolean isMailEnabled;

	/**
	 * 是否启用短信
	 */
	private Boolean isSmsEnabled;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public MessageConfig.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(MessageConfig.Type type) {
		this.type = type;
	}

	/**
	 * 获取是否启用邮件
	 * 
	 * @return 是否启用邮件
	 */
	public Boolean getIsMailEnabled() {
		return isMailEnabled;
	}

	/**
	 * 设置是否启用邮件
	 * 
	 * @param isMailEnabled
	 *            是否启用邮件
	 */
	public void setIsMailEnabled(Boolean isMailEnabled) {
		this.isMailEnabled = isMailEnabled;
	}

	/**
	 * 获取是否启用短信
	 * 
	 * @return 是否启用短信
	 */
	public Boolean getIsSmsEnabled() {
		return isSmsEnabled;
	}

	/**
	 * 设置是否启用短信
	 * 
	 * @param isSmsEnabled
	 *            是否启用短信
	 */
	public void setIsSmsEnabled(Boolean isSmsEnabled) {
		this.isSmsEnabled = isSmsEnabled;
	}

}