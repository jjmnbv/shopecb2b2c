package net.shopec.dao;

import java.util.List;

import net.shopec.entity.MemberRank;
import net.shopec.entity.ProductCategory;
import net.shopec.entity.Promotion;
import net.shopec.entity.Store;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 促销
 * 
 */
public interface PromotionDao extends BaseDao<Promotion> {

	/**
	 * 查找促销
	 * 
	 * @param store
	 *            店铺
	 * @param type
	 *            类型
	 * @param isEnabled
	 *            是否开启
	 * @return 促销
	 */
	List<Promotion> findList(@Param("store")Store store, @Param("type")Promotion.Type type, @Param("isEnabled")Boolean isEnabled);

	/**
	 * 查找促销
	 * 
	 * @param store
	 *            店铺
	 * @param type
	 *            类型
	 * @param memberRank
	 *            会员等级
	 * @param productCategory
	 *            商品分类
	 * @param hasBegun
	 *            是否已开始
	 * @param hasEnded
	 *            是否已结束
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 促销
	 */
	List<Promotion> selectList(@Param("ew") Wrapper<Promotion> wrapper, @Param("store")Store store, @Param("type")Promotion.Type type, @Param("memberRank")MemberRank memberRank, @Param("productCategory")ProductCategory productCategory, @Param("hasBegun")Boolean hasBegun, @Param("hasEnded")Boolean hasEnded);

	/**
	 * 查找促销
	 * 
	 * @param store
	 *            店铺
	 * @param type
	 *            类型
	 * @param pageable
	 *            分页
	 * @return 促销分页
	 */
	List<Promotion> findPage(RowBounds rowBounds, @Param("ew")Wrapper<Promotion> wrapper, @Param("store")Store store, @Param("type")Promotion.Type type);

}