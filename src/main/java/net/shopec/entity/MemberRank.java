package net.shopec.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.PreRemove;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * Entity - 会员等级
 * 
 */
//@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class MemberRank extends BaseEntity<MemberRank> {

	private static final long serialVersionUID = 3599029355500655209L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 优惠比例
	 */
	private Double scale;

	/**
	 * 消费金额
	 */
	private BigDecimal amount;

	/**
	 * 是否默认
	 */
	private Boolean isDefault;

	/**
	 * 是否特殊
	 */
	private Boolean isSpecial;

	/**
	 * 会员
	 */
	@TableField(exist = false)
	private Set<Member> members = new HashSet<>();

	/**
	 * 促销
	 */
	@TableField(exist = false)
	private Set<Promotion> promotions = new HashSet<>();

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取优惠比例
	 * 
	 * @return 优惠比例
	 */
	public Double getScale() {
		return scale;
	}

	/**
	 * 设置优惠比例
	 * 
	 * @param scale
	 *            优惠比例
	 */
	public void setScale(Double scale) {
		this.scale = scale;
	}

	/**
	 * 获取消费金额
	 * 
	 * @return 消费金额
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置消费金额
	 * 
	 * @param amount
	 *            消费金额
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获取是否默认
	 * 
	 * @return 是否默认
	 */
	public Boolean getIsDefault() {
		return isDefault;
	}

	/**
	 * 设置是否默认
	 * 
	 * @param isDefault
	 *            是否默认
	 */
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * 获取是否特殊
	 * 
	 * @return 是否特殊
	 */
	public Boolean getIsSpecial() {
		return isSpecial;
	}

	/**
	 * 设置是否特殊
	 * 
	 * @param isSpecial
	 *            是否特殊
	 */
	public void setIsSpecial(Boolean isSpecial) {
		this.isSpecial = isSpecial;
	}

	/**
	 * 获取会员
	 * 
	 * @return 会员
	 */
	public Set<Member> getMembers() {
		return members;
	}

	/**
	 * 设置会员
	 * 
	 * @param members
	 *            会员
	 */
	public void setMembers(Set<Member> members) {
		this.members = members;
	}

	/**
	 * 获取促销
	 * 
	 * @return 促销
	 */
	public Set<Promotion> getPromotions() {
		return promotions;
	}

	/**
	 * 设置促销
	 * 
	 * @param promotions
	 *            促销
	 */
	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
		Set<Promotion> promotions = getPromotions();
		if (promotions != null) {
			for (Promotion promotion : promotions) {
				promotion.getMemberRanks().remove(this);
			}
		}
	}

}