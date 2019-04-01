package net.shopec.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IEnum;

/**
 * Entity - 提现
 * 
 */
public class Cash extends BaseEntity<Cash> {

	private static final long serialVersionUID = -1129619429301847081L;

	/**
	 * 状态
	 */
	public enum Status implements IEnum{

		/**
		 * 等待审核
		 */
		pending(0),

		/**
		 * 审核通过
		 */
		approved(1),

		/**
		 * 审核失败
		 */
		failed(2);

		private int value;

		Status(final int value) {
			this.value = value;
		}
		
		@Override
		public Serializable getValue() {
			return this.value;
		}
	}

	/**
	 * 状态
	 */
	private Cash.Status status;

	/**
	 * 金额
	 */
	private BigDecimal amount;

	/**
	 * 收款银行
	 */
	private String bank;

	/**
	 * 收款账号
	 */
	private String account;

	/**
	 * 商家
	 */
	@TableField(exist = false)
	private Business business;

	/**
	 * 获取状态
	 * 
	 * @return 状态
	 */
	public Cash.Status getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * 
	 * @param status
	 *            状态
	 */
	public void setStatus(Cash.Status status) {
		this.status = status;
	}

	/**
	 * 获取金额
	 * 
	 * @return 金额
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置金额
	 * 
	 * @param amount
	 *            金额
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获取收款银行
	 * 
	 * @return 收款银行
	 */
	public String getBank() {
		return bank;
	}

	/**
	 * 设置收款银行
	 * 
	 * @param bank
	 *            收款银行
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}

	/**
	 * 获取收款账号
	 * 
	 * @return 收款账号
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * 设置收款账号
	 * 
	 * @param account
	 *            收款账号
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * 获取商家
	 * 
	 * @return 商家
	 */
	public Business getBusiness() {
		return business;
	}

	/**
	 * 设置商家
	 * 
	 * @param business
	 *            商家
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}

}