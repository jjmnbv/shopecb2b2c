package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Member;
import net.shopec.entity.PointLog;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 积分记录
 * 
 */
public interface PointLogDao extends BaseDao<PointLog> {

	/**
	 * 查找积分记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 积分记录分页
	 */
	List<PointLog> findPage(RowBounds rowBounds, @Param("ew")Wrapper<PointLog> wrapper, @Param("member")Member member);

}