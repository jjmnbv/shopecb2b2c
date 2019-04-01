package net.shopec.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IEnum;

/**
 * Entity - 商家注册项
 * 
 */
public class BusinessAttribute extends OrderedEntity<BusinessAttribute> {

	private static final long serialVersionUID = -2636800237333705047L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum {

		/**
		 * 名称
		 */
		name(0),

		/**
		 * 营业执照
		 */
		licenseNumber(1),

		/**
		 * 营业执照图片
		 */
		licenseImage(2),

		/**
		 * 法人姓名
		 */
		legalPerson(3),

		/**
		 * 法人身份证
		 */
		idCard(4),

		/**
		 * 法人身份证图片
		 */
		idCardImage(5),

		/**
		 * 电话
		 */
		phone(6),

		/**
		 * 组织机构代码
		 */
		organizationCode(7),

		/**
		 * 组织机构代码证图片
		 */
		organizationImage(8),

		/**
		 * 纳税人识别号
		 */
		identificationNumber(9),

		/**
		 * 税务登记证图片
		 */
		taxImage(10),

		/**
		 * 银行开户名
		 */
		bankName(11),

		/**
		 * 公司银行账号
		 */
		bankAccount(12),

		/**
		 * 文本
		 */
		text(13),

		/**
		 * 单选项
		 */
		select(14),

		/**
		 * 多选项
		 */
		checkbox(15),

		/**
		 * 图片
		 */
		image(16),

		/**
		 * 日期
		 */
		date(17);

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
	private BusinessAttribute.Type type;

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
	@TableField(exist=false)
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
	public BusinessAttribute.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(BusinessAttribute.Type type) {
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
	public int compareTo(BusinessAttribute o) {
		if (o == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), o.getOrder()).append(getId(), o.getId()).toComparison();
	}

}