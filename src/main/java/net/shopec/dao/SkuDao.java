package net.shopec.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import net.shopec.entity.Product;
import net.shopec.entity.Sku;
import net.shopec.entity.Store;

/**
 * Dao - SKU
 * 
 */
public interface SkuDao extends BaseDao<Sku> {

	/**
	 * 通过编号、名称查找SKU
	 * 
	 * @param store
	 *            店铺
	 * @param type
	 *            类型
	 * @param keyword
	 *            关键词
	 * @param excludes
	 *            排除SKU
	 * @param count
	 *            数量
	 * @return SKU
	 */
	List<Sku> search(@Param("store")Store store, @Param("type")Product.Type type, @Param("keyword")String keyword, @Param("excludes")Set<Sku> excludes, @Param("count")Integer count);

}