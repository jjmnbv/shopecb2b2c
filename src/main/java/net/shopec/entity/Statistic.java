package net.shopec.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IEnum;

/**
 * Entity - 统计
 * 
 */
public class Statistic extends BaseEntity<Statistic> {

	private static final long serialVersionUID = 2022131337300482638L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum {

		/**
		 * 会员注册数
		 */
		registerMemberCount(0),

		/**
		 * 订单创建数
		 */
		createOrderCount(1),

		/**
		 * 订单完成数
		 */
		completeOrderCount(2),

		/**
		 * 订单创建金额
		 */
		createOrderAmount(3),

		/**
		 * 订单完成金额
		 */
		completeOrderAmount(4);

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
	 * 周期
	 */
	public enum Period {

		/**
		 * 年
		 */
		year,

		/**
		 * 月
		 */
		month,

		/**
		 * 日
		 */
		day
	}

	/**
	 * 类型
	 */
	private Statistic.Type type;

	/**
	 * 年
	 */
	private Integer year;

	/**
	 * 月
	 */
	private Integer month;

	/**
	 * 日
	 */
	private Integer day;

	/**
	 * 值
	 */
	private BigDecimal value;

	/**
	 * 店铺
	 */
	@TableField(exist = false)
	private Store store;

	/**
	 * 构造方法
	 */
	public Statistic() {
	}

	/**
	 * 构造方法
	 * 
	 * @param type
	 *            类型
	 * @param year
	 *            年
	 * @param value
	 *            值
	 */
	public Statistic(Statistic.Type type, Integer year, BigDecimal value) {
		this.type = type;
		this.year = year;
		this.value = value;
	}

	/**
	 * 构造方法
	 * 
	 * @param type
	 *            类型
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param value
	 *            值
	 */
	public Statistic(Statistic.Type type, Integer year, Integer month, BigDecimal value) {
		this.type = type;
		this.year = year;
		this.month = month;
		this.value = value;
	}

	/**
	 * 构造方法
	 * 
	 * @param type
	 *            类型
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @param value
	 *            值
	 */
	public Statistic(Statistic.Type type, Integer year, Integer month, Integer day, BigDecimal value) {
		this.type = type;
		this.year = year;
		this.month = month;
		this.day = day;
		this.value = value;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public Statistic.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Statistic.Type type) {
		this.type = type;
	}

	/**
	 * 获取年
	 * 
	 * @return 年
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * 设置年
	 * 
	 * @param year
	 *            年
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * 获取月
	 * 
	 * @return 月
	 */
	public Integer getMonth() {
		return month;
	}

	/**
	 * 设置月
	 * 
	 * @param month
	 *            月
	 */
	public void setMonth(Integer month) {
		this.month = month;
	}

	/**
	 * 获取日
	 * 
	 * @return 日
	 */
	public Integer getDay() {
		return day;
	}

	/**
	 * 设置日
	 * 
	 * @param day
	 *            日
	 */
	public void setDay(Integer day) {
		this.day = day;
	}

	/**
	 * 获取值
	 * 
	 * @return 值
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * 设置值
	 * 
	 * @param value
	 *            值
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * 获取店铺
	 * 
	 * @return 店铺
	 */
	public Store getStore() {
		return store;
	}

	/**
	 * 设置店铺
	 * 
	 * @param store
	 *            店铺
	 */
	public void setStore(Store store) {
		this.store = store;
	}

	/**
	 * 获取日期
	 * 
	 * @return 日期
	 */
	@Transient
	public Date getDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, getYear() != null ? getYear() : 0);
		calendar.set(Calendar.MONTH, getMonth() != null ? getMonth() : 0);
		calendar.set(Calendar.DAY_OF_MONTH, getDay() != null ? getDay() : 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

}