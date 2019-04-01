package net.shopec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.shopec.entity.ProductStoreProductTag;

/**
 * <p>
 * 店铺商品标签中间表 Mapper 接口
 * </p>
 *
 * @author 江南红衣
 * @since 2018-06-06
 */
public interface ProductStoreProductTagDao extends BaseDao<ProductStoreProductTag> {

	/**
	 * 批量保存
	 * @param productStoreProductTags
	 * @return
	 */
	int batchSave(@Param("productStoreProductTags")List<ProductStoreProductTag> productStoreProductTags);
	
}
