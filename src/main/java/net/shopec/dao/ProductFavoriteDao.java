package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Member;
import net.shopec.entity.Product;
import net.shopec.entity.ProductFavorite;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 商品收藏
 * 
 */
public interface ProductFavoriteDao extends BaseDao<ProductFavorite> {

	/**
	 * 判断商品收藏是否存在
	 * 
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @return 商品收藏是否存在
	 */
	boolean exists(@Param("member")Member member, @Param("product")Product product);

	/**
	 * 查找商品收藏
	 * 
	 * @param member
	 *            会员
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 商品收藏
	 */
	List<ProductFavorite> findList(@Param("ew") Wrapper<ProductFavorite> wrapper, @Param("member")Member member);

	/**
	 * 查找商品收藏分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 商品收藏分页
	 */
	List<ProductFavorite> findPage(RowBounds rowBounds, @Param("ew")Wrapper<ProductFavorite> wrapper, @Param("member")Member member);

	/**
	 * 查找商品收藏数量
	 * 
	 * @param member
	 *            会员
	 * @return 商品收藏数量
	 */
	Long count(@Param("member")Member member);

}