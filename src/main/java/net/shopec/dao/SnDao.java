package net.shopec.dao;

import org.apache.ibatis.annotations.Param;

import net.shopec.entity.Sn;

/**
 * Dao - 序列号
 * 
 */
public interface SnDao {

	/**
	 * 查找实体对象
	 * 
	 * @param id
	 *            ID
	 * @return 实体对象，若不存在则返回null
	 */
	Sn find(@Param("type")Sn.Type type);

	/**
	 * 更新实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	int update(@Param("sn")Sn sn);
	
}