package net.shopec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.shopec.entity.Navigation;

/**
 * Dao - 导航
 * 
 */
public interface NavigationDao extends BaseDao<Navigation> {

	/**
	 * 查找导航
	 * 
	 * @param position
	 *            位置
	 * @return 导航
	 */
	List<Navigation> findList(@Param("position")Navigation.Position position);

}