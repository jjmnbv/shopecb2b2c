package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Member;
import net.shopec.entity.Store;
import net.shopec.entity.StoreFavorite;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 店铺收藏
 * 
 */
public interface StoreFavoriteDao extends BaseDao<StoreFavorite> {

	/**
	 * 判断店铺收藏是否存在
	 * 
	 * @param member
	 *            会员
	 * @param store
	 *            店铺
	 * @return 店铺收藏是否存在
	 */
	boolean exists(@Param("member")Member member, @Param("store")Store store);

	/**
	 * 查找店铺收藏
	 * 
	 * @param member
	 *            会员
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 店铺收藏
	 */
	List<StoreFavorite> findList(@Param("ew") Wrapper<StoreFavorite> wrapper, @Param("member")Member member);

	/**
	 * 查找店铺收藏分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 店铺收藏分页
	 */
	List<StoreFavorite> findPage(RowBounds rowBounds, @Param("ew")Wrapper<StoreFavorite> wrapper, @Param("member")Member member);

	/**
	 * 查找店铺收藏数量
	 * 
	 * @param member
	 *            会员
	 * @return 店铺收藏数量
	 */
	Long count(@Param("member")Member member);

}