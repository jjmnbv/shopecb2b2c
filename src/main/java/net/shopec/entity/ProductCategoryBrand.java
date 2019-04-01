package net.shopec.entity;

import java.io.Serializable;

/**
 * <p>
 * 商品分类品牌中间表
 * </p>
 *
 * @author 江南红衣
 * @since 2018-04-26
 */
public class ProductCategoryBrand implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 商品分类
	 */
	private Long productCategoriesId;

	/**
	 * 关联品牌
	 */
	private Long brandsId;

	public Long getProductCategoriesId() {
		return productCategoriesId;
	}

	public void setProductCategoriesId(Long productCategoriesId) {
		this.productCategoriesId = productCategoriesId;
	}

	public Long getBrandsId() {
		return brandsId;
	}

	public void setBrandsId(Long brandsId) {
		this.brandsId = brandsId;
	}

	@Override
	public String toString() {
		return "ProductCategoryBrand{" + "productCategoriesId=" + productCategoriesId + ", brandsId=" + brandsId + "}";
	}
}
