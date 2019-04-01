package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Coupon;
import net.shopec.entity.CouponCode;
import net.shopec.entity.Member;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 优惠码
 * 
 */
public interface CouponCodeDao extends BaseDao<CouponCode> {

	/**
	 * 查找优惠码分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 优惠码分页
	 */
	List<CouponCode> findPage(RowBounds rowBounds, @Param("ew")Wrapper<CouponCode> wrapper, @Param("member")Member member);

	/**
	 * 查找优惠码数量
	 * 
	 * @param coupon
	 *            优惠券
	 * @param member
	 *            会员
	 * @param hasBegun
	 *            是否已开始
	 * @param hasExpired
	 *            是否已过期
	 * @param isUsed
	 *            是否已使用
	 * @return 优惠码数量
	 */
	Long count(@Param("coupon")Coupon coupon, @Param("member")Member member, @Param("hasBegun")Boolean hasBegun, @Param("hasExpired")Boolean hasExpired, @Param("isUsed")Boolean isUsed);

}