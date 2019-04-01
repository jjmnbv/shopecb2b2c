package net.shopec.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.PreRemove;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * Entity - 商品标签
 * 
 */
public class ProductTag extends OrderedEntity<ProductTag> {

	private static final long serialVersionUID = 4136507336496569742L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 备注
	 */
	private String memo;

	/**
	 * 商品
	 */
	@TableField(exist = false)
	private Set<Product> products = new HashSet<>();

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
	 * 获取图标
	 * 
	 * @return 图标
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * 设置图标
	 * 
	 * @param icon
	 *            图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 * 
	 * @param memo
	 *            备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取商品
	 * 
	 * @return 商品
	 */
	public Set<Product> getProducts() {
		return products;
	}

	/**
	 * 设置商品
	 * 
	 * @param products
	 *            商品
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
		Set<Product> products = getProducts();
		if (products != null) {
			for (Product product : products) {
				product.getProductTags().remove(this);
			}
		}
	}

	@Override
	public int compareTo(ProductTag o) {
		if (o == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), o.getOrder()).append(getId(), o.getId()).toComparison();
	}

}