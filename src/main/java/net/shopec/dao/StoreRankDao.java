package net.shopec.dao;

import java.util.List;

import net.shopec.entity.StoreRank;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 店铺等级
 * 
 */
public interface StoreRankDao extends BaseDao<StoreRank> {

	/**
	 * 查找店铺等级
	 * 
	 * @param isAllowRegister
	 *            是否允许注册
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 店铺等级
	 */
	List<StoreRank> findList(@Param("ew") Wrapper<StoreRank> wrapper, @Param("isAllowRegister")Boolean isAllowRegister);

}