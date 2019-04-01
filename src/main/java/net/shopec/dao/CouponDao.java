package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Coupon;
import net.shopec.entity.Store;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 优惠券
 * 
 */
public interface CouponDao extends BaseDao<Coupon> {

	/**
	 * 查找优惠券
	 * 
	 * @param store
	 *            店铺
	 * @param isEnabled
	 *            是否启用
	 * @param isExchange
	 *            是否允许积分兑换
	 * @param hasExpired
	 *            是否已过期
	 * @return 优惠券
	 */
	List<Coupon> findList(@Param("store")Store store, @Param("isEnabled")Boolean isEnabled, @Param("isExchange")Boolean isExchange, @Param("hasExpired")Boolean hasExpired);

	/**
	 * 查找优惠券分页
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param isExchange
	 *            是否允许积分兑换
	 * @param hasExpired
	 *            是否已过期
	 * @param pageable
	 *            分页信息
	 * @return 优惠券分页
	 */
	List<Coupon> selectPage(RowBounds rowBounds, @Param("ew")Wrapper<Coupon> wrapper, @Param("isEnabled")Boolean isEnabled, @Param("isExchange")Boolean isExchange, @Param("hasExpired")Boolean hasExpired);

	/**
	 * 查找优惠券分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页信息
	 * @return 优惠券分页
	 */
	List<Coupon> findPage(RowBounds rowBounds, @Param("ew")Wrapper<Coupon> wrapper, @Param("store")Store store);

}