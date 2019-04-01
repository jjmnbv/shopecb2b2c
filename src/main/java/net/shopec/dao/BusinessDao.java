package net.shopec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.shopec.entity.Business;

/**
 * Dao - 商家
 * 
 */
public interface BusinessDao extends BaseDao<Business> {

	/**
	 * 通过名称查找商家
	 * 
	 * @param keyword
	 *            关键词
	 * @param count
	 *            数量
	 * @return 商家
	 */
	List<Business> search(@Param("keyword")String keyword, @Param("count")Integer count);

	/**
	 * 查找所有商家
	 * 
	 * @return 所有商家
	 */
	List<Business> findAll();
}