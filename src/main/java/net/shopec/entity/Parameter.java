package net.shopec.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * Entity - 参数
 * 
 */
public class Parameter extends OrderedEntity<Parameter> {

	private static final long serialVersionUID = -6159626519016913987L;

	/**
	 * 参数组
	 */
	@TableField(value = "parameter_group")
	private String group;

	/**
	 * 绑定分类
	 */
	@TableField(exist = false)
	private ProductCategory productCategory;

	/**
	 * 参数名称
	 */
	@TableField(exist = false)
	private List<String> names = new ArrayList<>();

	/**
	 * 获取参数组
	 * 
	 * @return 参数组
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * 设置参数组
	 * 
	 * @param group
	 *            参数组
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * 获取绑定分类
	 * 
	 * @return 绑定分类
	 */
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	/**
	 * 设置绑定分类
	 * 
	 * @param productCategory
	 *            绑定分类
	 */
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	/**
	 * 获取参数名称
	 * 
	 * @return 参数名称
	 */
	public List<String> getNames() {
		return names;
	}

	/**
	 * 设置参数名称
	 * 
	 * @param names
	 *            参数名称
	 */
	public void setNames(List<String> names) {
		this.names = names;
	}


	@Override
	public int compareTo(Parameter o) {
		if (o == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), o.getOrder()).append(getId(), o.getId()).toComparison();
	}

}