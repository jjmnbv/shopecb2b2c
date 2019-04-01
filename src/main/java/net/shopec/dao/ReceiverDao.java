package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Member;
import net.shopec.entity.Receiver;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 收货地址
 * 
 */
public interface ReceiverDao extends BaseDao<Receiver> {

	/**
	 * 查找默认收货地址
	 * 
	 * @param member
	 *            会员
	 * @return 默认收货地址，若不存在则返回最新收货地址
	 */
	Receiver findDefault(@Param("member")Member member);

	/**
	 * 查找收货地址
	 * 
	 * @param member
	 *            会员
	 * @return 收货地址
	 */
	List<Receiver> findList(@Param("member")Member member);

	/**
	 * 查找收货地址分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 收货地址分页
	 */
	List<Receiver> findPage(RowBounds rowBounds, @Param("ew")Wrapper<Receiver> wrapper, @Param("member")Member member);

	/**
	 * 清除默认
	 * 
	 * @param member
	 *            会员
	 */
	void clearDefault(@Param("member")Member member);

	/**
	 * 清除默认
	 * 
	 * @param member
	 *            会员
	 * @param exclude
	 *            排除收货地址
	 */
	void clearDefaultExclude(@Param("member")Member member, @Param("exclude")Receiver exclude);

}