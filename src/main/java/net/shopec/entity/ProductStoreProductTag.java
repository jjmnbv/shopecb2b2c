package net.shopec.entity;

import java.io.Serializable;

/**
 * <p>
 * 商品店铺商品标签中间表
 * </p>
 *
 * @author 江南红衣
 * @since 2018-05-21
 */
public class ProductStoreProductTag implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 商品
	 */
	private Long productsId;

	/**
	 * 店铺商品标签
	 */
	private Long storeProductTagsId;

	public Long getProductsId() {
		return productsId;
	}

	public void setProductsId(Long productsId) {
		this.productsId = productsId;
	}

	public Long getStoreProductTagsId() {
		return storeProductTagsId;
	}

	public void setStoreProductTagsId(Long storeProductTagsId) {
		this.storeProductTagsId = storeProductTagsId;
	}

	@Override
	public String toString() {
		return "ProductStoreProductTag{" + "productsId=" + productsId
				+ ", storeProductTagsId=" + storeProductTagsId + "}";
	}
}
