package net.shopec.entity;

import java.io.Serializable;

/**
 * <p>
 * 商品商品标签中间表
 * </p>
 *
 * @author 江南红衣
 * @since 2018-05-21
 */
public class ProductProductTag implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 商品
	 */
	private Long productsId;

	/**
	 * 商品标签
	 */
	private Long productTagsId;

	public Long getProductsId() {
		return productsId;
	}

	public void setProductsId(Long productsId) {
		this.productsId = productsId;
	}

	public Long getProductTagsId() {
		return productTagsId;
	}

	public void setProductTagsId(Long productTagsId) {
		this.productTagsId = productTagsId;
	}

	@Override
	public String toString() {
		return "ProductProductTag{" + "productsId=" + productsId
				+ ", productTagsId=" + productTagsId + "}";
	}
}
