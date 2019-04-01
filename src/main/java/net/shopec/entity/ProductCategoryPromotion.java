package net.shopec.entity;

import java.io.Serializable;

/**
 * <p>
 * 商品分类促销中间表
 * </p>
 *
 * @author 江南红衣
 * @since 2018-04-26
 */
public class ProductCategoryPromotion implements Serializable {

	private static final long serialVersionUID = 193046422351103201L;

	/**
	 * 商品分类
	 */
	private Long productCategoriesId;

	/**
	 * 关联促销
	 */
	private Long promotionsId;

	public Long getProductCategoriesId() {
		return productCategoriesId;
	}

	public void setProductCategoriesId(Long productCategoriesId) {
		this.productCategoriesId = productCategoriesId;
	}

	public Long getPromotionsId() {
		return promotionsId;
	}

	public void setPromotionsId(Long promotionsId) {
		this.promotionsId = promotionsId;
	}

	@Override
	public String toString() {
		return "ProductCategoryPromotion{" + "productCategoriesId=" + productCategoriesId + ", promotionsId="
				+ promotionsId + "}";
	}
}
