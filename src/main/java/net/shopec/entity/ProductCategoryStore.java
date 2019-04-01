package net.shopec.entity;

import java.io.Serializable;

/**
 * <p>
 * 店铺商品分类中间表
 * </p>
 *
 * @author 江南红衣
 * @since 2018-05-04
 */
public class ProductCategoryStore implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long storesId;

	private Long productCategoriesId;

	public Long getStoresId() {
		return storesId;
	}

	public void setStoresId(Long storesId) {
		this.storesId = storesId;
	}

	public Long getProductCategoriesId() {
		return productCategoriesId;
	}

	public void setProductCategoriesId(Long productCategoriesId) {
		this.productCategoriesId = productCategoriesId;
	}

	@Override
	public String toString() {
		return "ProductCategoryStore{" + "storesId=" + storesId
				+ ", productCategoriesId=" + productCategoriesId + "}";
	}
}
