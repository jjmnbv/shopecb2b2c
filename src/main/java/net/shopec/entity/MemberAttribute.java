package net.shopec.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IEnum;

/**
 * Entity - 会员注册项
 * 
 */
public class MemberAttribute extends OrderedEntity<MemberAttribute> {

	private static final long serialVersionUID = 4513705276569738136L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum {

		/**
		 * 姓名
		 */
		name(0),

		/**
		 * 性别
		 */
		gender(1),

		/**
		 * 出生日期
		 */
		birth(2),

		/**
		 * 地区
		 */
		area(3),

		/**
		 * 地址
		 */
		address(4),

		/**
		 * 邮编
		 */
		zipCode(5),

		/**
		 * 电话
		 */
		phone(6),

		/**
		 * 文本
		 */
		text(7),

		/**
		 * 单选项
		 */
		select(8),

		/**
		 * 多选项
		 */
		checkbox(9);

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
	 * 名称
	 */
	private String name;

	/**
	 * 类型
	 */
	private MemberAttribute.Type type;

	/**
	 * 配比
	 */
	private String pattern;

	/**
	 * 是否启用
	 */
	private Boolean isEnabled;

	/**
	 * 是否必填
	 */
	private Boolean isRequired;

	/**
	 * 属性序号
	 */
	private Integer propertyIndex;

	/**
	 * 可选项
	 */
	@TableField(exist = false)
	private List<String> options = new ArrayList<>();

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
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public MemberAttribute.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(MemberAttribute.Type type) {
		this.type = type;
	}

	/**
	 * 获取配比
	 * 
	 * @return 配比
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * 设置配比
	 * 
	 * @param pattern
	 *            配比
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * 获取是否启用
	 * 
	 * @return 是否启用
	 */
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	/**
	 * 设置是否启用
	 * 
	 * @param isEnabled
	 *            是否启用
	 */
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * 获取是否必填
	 * 
	 * @return 是否必填
	 */
	public Boolean getIsRequired() {
		return isRequired;
	}

	/**
	 * 设置是否必填
	 * 
	 * @param isRequired
	 *            是否必填
	 */
	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	/**
	 * 获取属性序号
	 * 
	 * @return 属性序号
	 */
	public Integer getPropertyIndex() {
		return propertyIndex;
	}

	/**
	 * 设置属性序号
	 * 
	 * @param propertyIndex
	 *            属性序号
	 */
	public void setPropertyIndex(Integer propertyIndex) {
		this.propertyIndex = propertyIndex;
	}

	/**
	 * 获取可选项
	 * 
	 * @return 可选项
	 */
	public List<String> getOptions() {
		return options;
	}

	/**
	 * 设置可选项
	 * 
	 * @param options
	 *            可选项
	 */
	public void setOptions(List<String> options) {
		this.options = options;
	}


	@Override
	public int compareTo(MemberAttribute o) {
		if (o == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), o.getOrder()).append(getId(), o.getId()).toComparison();
	}

}