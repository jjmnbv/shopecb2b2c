package net.shopec.dao;

import java.util.List;

import net.shopec.entity.ProductCategoryStore;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 店铺商品分类中间表 Mapper 接口
 * </p>
 *
 * @author 江南红衣
 * @since 2018-05-04
 */
public interface ProductCategoryStoreDao extends BaseDao<ProductCategoryStore> {

	/**
	 * 批量保存
	 * @param adminRoles
	 * @return
	 */
	int batchSave(@Param("productCategoryStores")List<ProductCategoryStore> productCategoryStores);
	
}
