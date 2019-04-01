package net.shopec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.shopec.entity.ProductProductTag;

/**
 * <p>
 * 商品标签中间表 Mapper 接口
 * </p>
 *
 * @author 江南红衣
 * @since 2018-06-06
 */
public interface ProductProductTagDao extends BaseDao<ProductProductTag> {

	/**
	 * 批量保存
	 * @param productProductTags
	 * @return
	 */
	int batchSave(@Param("productProductTags")List<ProductProductTag> productProductTags);
	
}
