package net.shopec.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 优惠码
 * 
 */
public class CouponCode extends BaseEntity<CouponCode> {

	private static final long serialVersionUID = -1812874037224306719L;

	/**
	 * 号码
	 */
	@JsonView(BaseView.class)
	private String code;

	/**
	 * 是否已使用
	 */
	@JsonView(BaseView.class)
	private Boolean isUsed;

	/**
	 * 使用日期
	 */
	@JsonView(BaseView.class)
	private Date usedDate;

	/**
	 * 优惠券
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private Coupon coupon;

	/**
	 * 会员
	 */
	@TableField(exist = false)
	private Member member;

	/**
	 * 订单
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private Order order;

	/**
	 * 获取号码
	 * 
	 * @return 号码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置号码
	 * 
	 * @param code
	 *            号码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取是否已使用
	 * 
	 * @return 是否已使用
	 */
	public Boolean getIsUsed() {
		return isUsed;
	}

	/**
	 * 设置是否已使用
	 * 
	 * @param isUsed
	 *            是否已使用
	 */
	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	/**
	 * 获取使用日期
	 * 
	 * @return 使用日期
	 */
	public Date getUsedDate() {
		return usedDate;
	}

	/**
	 * 设置使用日期
	 * 
	 * @param usedDate
	 *            使用日期
	 */
	public void setUsedDate(Date usedDate) {
		this.usedDate = usedDate;
	}

	/**
	 * 获取优惠券
	 * 
	 * @return 优惠券
	 */
	public Coupon getCoupon() {
		return coupon;
	}

	/**
	 * 设置优惠券
	 * 
	 * @param coupon
	 *            优惠券
	 */
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	/**
	 * 获取会员
	 * 
	 * @return 会员
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * 设置会员
	 * 
	 * @param member
	 *            会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * 设置订单
	 * 
	 * @param order
	 *            订单
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * 删除前处理
	 */
	public void preRemove() {
		if (getOrder() != null) {
			getOrder().setCouponCode(null);
		}
	}

}