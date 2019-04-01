package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Member;
import net.shopec.entity.MemberDepositLog;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 会员预存款记录
 * 
 */
public interface MemberDepositLogDao extends BaseDao<MemberDepositLog> {

	/**
	 * 查找会员预存款记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 会员预存款记录分页
	 */
	List<MemberDepositLog> findPage(RowBounds rowBounds, @Param("ew")Wrapper<MemberDepositLog> wrapper, @Param("member")Member member);

}