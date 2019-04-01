package net.shopec.entity;

import java.io.Serializable;

/**
 * <p>
 * 商品促销中间表
 * </p>
 *
 * @author 江南红衣
 * @since 2018-05-21
 */
public class ProductPromotion implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 商品
	 */
	private Long productsId;

	/**
	 * 促销
	 */
	private Long promotionsId;

	public Long getProductsId() {
		return productsId;
	}

	public void setProductsId(Long productsId) {
		this.productsId = productsId;
	}

	public Long getPromotionsId() {
		return promotionsId;
	}

	public void setPromotionsId(Long promotionsId) {
		this.promotionsId = promotionsId;
	}

	@Override
	public String toString() {
		return "ProductPromotion{" + "productsId=" + productsId + ", promotionsId=" + promotionsId + "}";
	}
}
