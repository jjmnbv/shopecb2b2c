package net.shopec.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import net.shopec.Page;
import net.shopec.Pageable;

/**
 * Dao - 基类
 * 
 */
public interface BaseDao<T> extends BaseMapper<T> {

	/**
	 * 查找实体对象
	 * 
	 * @param id
	 *            ID
	 * @return 实体对象，若不存在则返回null
	 */
	T find(@Param("id")Long id);

	/**
	 * 查找实体对象
	 * 
	 * @param attributeName
	 *            属性名称
	 * @param attributeValue
	 *            属性值
	 * @return 实体对象，若不存在则返回null
	 */
	T findByAttribute(@Param("attributeName")String attributeName, @Param("attributeValue")Object attributeValue);

	/**
	 * 查找实体对象集合
	 * 
	 * @param attributeName
	 *            属性名称
	 * @param attributeValue
	 *            属性值
	 * @return 实体对象，若不存在则返回null
	 */
	Set<T> findSet(@Param("attributeName")String attributeName, @Param("attributeValue")Object attributeValue);
	
	/**
	 * 查找所有实体对象集合
	 * 
	 * @return 所有实体对象集合
	 */
	List<T> findAll();
	
	/**
	 * 查找实体对象集合
	 * 
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 实体对象集合
	 */
	//List<T> findList(@Param("first")Integer first, @Param("count")Integer count, @Param("filters")List<Filter> filters, @Param("orders")List<Order> orders);

	/**
	 * 查找实体对象分页
	 * 
	 * @param pageable
	 *            分页信息
	 * @return 实体对象分页
	 */
	Page<T> findPage(@Param("pageable")Pageable pageable);

	/**
	 * 判断是否存在
	 * 
	 * @param attributeName
	 *            属性名称
	 * @param attributeValue
	 *            属性值
	 * @return 是否存在
	 */
	boolean exists(@Param("attributeName")String attributeName, @Param("attributeValue")Object attributeValue);

	/**
	 * 判断是否唯一
	 * 
	 * @param id
	 *            ID
	 * @param attributeName
	 *            属性名称
	 * @param attributeValue
	 *            属性值
	 * @return 是否唯一
	 */
	boolean unique(@Param("id")Long id, @Param("attributeName")String attributeName, @Param("attributeValue")Object attributeValue);
	
	/**
	 * 持久化实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	int save(@Param("entity")T entity);

	/**
	 * 更新实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	int update(@Param("entity")T entity);

	/**
	 * 移除实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	int remove(@Param("entity")T entity);
	
}
