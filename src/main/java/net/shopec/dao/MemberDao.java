package net.shopec.dao;

import java.util.Date;
import java.util.List;

import net.shopec.entity.Member;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 会员
 * 
 */
public interface MemberDao extends BaseDao<Member> {

	/**
	 * 查找会员分页
	 * 
	 * @param rankingType
	 *            排名类型
	 * @param pageable
	 *            分页信息
	 * @return 会员分页
	 */
	List<Member> findPage(RowBounds rowBounds, @Param("ew")Wrapper<Member> wrapper, @Param("rankingType")Member.RankingType rankingType);

	/**
	 * 查询会员注册数
	 * 
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 会员注册数
	 */
	Long registerMemberCount(@Param("beginDate")Date beginDate, @Param("endDate")Date endDate);

	/**
	 * 清除会员注册项值
	 * 
	 * @param propertyName
	 *            会员注册项
	 */
	void clearAttributeValue(@Param("propertyName")String propertyName);

}