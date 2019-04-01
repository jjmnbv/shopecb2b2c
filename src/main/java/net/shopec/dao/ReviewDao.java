package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Member;
import net.shopec.entity.Product;
import net.shopec.entity.Review;
import net.shopec.entity.Store;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 评论
 * 
 */
public interface ReviewDao extends BaseDao<Review> {

	/**
	 * 查找评论
	 * 
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param type
	 *            类型
	 * @param isShow
	 *            是否显示
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 评论
	 */
	List<Review> findList(@Param("ew") Wrapper<Review> wrapper, @Param("member")Member member, @Param("product")Product product, @Param("type")Review.Type type, @Param("isShow")Boolean isShow);

	/**
	 * 查找评论分页
	 * 
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param store
	 *            店铺
	 * @param type
	 *            类型
	 * @param isShow
	 *            是否显示
	 * @param pageable
	 *            分页信息
	 * @return 评论分页
	 */
	List<Review> findPage(RowBounds rowBounds, @Param("ew")Wrapper<Review> wrapper, @Param("member")Member member, @Param("product")Product product, @Param("store")Store store, @Param("type")Review.Type type, @Param("isShow")Boolean isShow);

	/**
	 * 查找评论数量
	 * 
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param type
	 *            类型
	 * @param isShow
	 *            是否显示
	 * @return 评论数量
	 */
	Long count(@Param("member")Member member, @Param("product")Product product, @Param("type")Review.Type type, @Param("isShow")Boolean isShow);

	/**
	 * 计算商品总评分
	 * 
	 * @param product
	 *            商品
	 * @return 商品总评分，仅计算显示评论
	 */
	long calculateTotalScore(@Param("product")Product product);

	/**
	 * 计算商品评分次数
	 * 
	 * @param product
	 *            商品
	 * @return 商品评分次数，仅计算显示评论
	 */
	long calculateScoreCount(@Param("product")Product product);

}