package net.shopec.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.PreRemove;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * Entity - 店铺商品标签
 * 
 */
public class StoreProductTag extends OrderedEntity<StoreProductTag> {

	private static final long serialVersionUID = 8007044548342479047L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 是否启用
	 */
	private Boolean isEnabled;

	/**
	 * 备注
	 */
	private String memo;

	/**
	 * 店铺
	 */
	@TableField(exist = false)
	private Store store;

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
				product.getStoreProductTags().remove(this);
			}
		}
	}

	@Override
	public int compareTo(StoreProductTag o) {
		if (o == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), o.getOrder()).append(getId(), o.getId()).toComparison();
	}

}