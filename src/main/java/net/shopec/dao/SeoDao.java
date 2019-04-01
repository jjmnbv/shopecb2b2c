package net.shopec.dao;

import net.shopec.entity.Seo;

import org.apache.ibatis.annotations.Param;

/**
 * Dao - SEO设置
 * 
 */
public interface SeoDao extends BaseDao<Seo> {

	/**
	 * 查找实体对象
	 * 
	 * @param attributeName
	 *            属性名称
	 * @param type
	 *            类型
	 * @return 实体对象，若不存在则返回null
	 */
	Seo find(@Param("attributeName")String attributeName, @Param("type")Seo.Type type);
}