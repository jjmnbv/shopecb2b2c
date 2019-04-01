package net.shopec.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IEnum;

/**
 * Entity - 品牌
 * 
 */
public class Brand extends OrderedEntity<Brand> {

	private static final long serialVersionUID = -6109590619136943215L;

	/**
	 * 路径
	 */
	private static final String PATH = "/brand/detail/%d";

	/**
	 * 类型
	 */
	public enum Type implements IEnum {

		/**
		 * 文本
		 */
		text(0),

		/**
		 * 图片
		 */
		image(1);

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
	public Brand.Type type;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * logo
	 */
	private String logo;

	/**
	 * 网址
	 */
	private String url;

	/**
	 * 介绍
	 */
	private String introduction;

	/**
	 * 商品
	 */
	@TableField(exist=false)
	private Set<Product> products = new HashSet<>();

	/**
	 * 商品分类
	 */
	@TableField(exist=false)
	private Set<ProductCategory> productCategories = new HashSet<>();

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
	public Brand.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Brand.Type type) {
		this.type = type;
	}

	/**
	 * 获取logo
	 * 
	 * @return logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * 设置logo
	 * 
	 * @param logo
	 *            logo
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * 获取网址
	 * 
	 * @return 网址
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置网址
	 * 
	 * @param url
	 *            网址
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取介绍
	 * 
	 * @return 介绍
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * 设置介绍
	 * 
	 * @param introduction
	 *            介绍
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
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
	 * 获取商品分类
	 * 
	 * @return 商品分类
	 */
	public Set<ProductCategory> getProductCategories() {
		return productCategories;
	}

	/**
	 * 设置商品分类
	 * 
	 * @param productCategories
	 *            商品分类
	 */
	public void setProductCategories(Set<ProductCategory> productCategories) {
		this.productCategories = productCategories;
	}

	/**
	 * 获取路径
	 * 
	 * @return 路径
	 */
	public String getPath() {
		return String.format(Brand.PATH, getId());
	}

	/**
	 * 删除前处理
	 */
	public void preRemove() {
		Set<Product> products = getProducts();
		if (products != null) {
			for (Product product : products) {
				product.setBrand(null);
			}
		}
		Set<ProductCategory> productCategories = getProductCategories();
		if (productCategories != null) {
			for (ProductCategory productCategory : productCategories) {
				productCategory.getBrands().remove(this);
			}
		}
	}

	@Override
	public int compareTo(Brand o) {
		if (o == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), o.getOrder()).append(getId(), o.getId()).toComparison();
	}

}