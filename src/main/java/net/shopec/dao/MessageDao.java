package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Member;
import net.shopec.entity.Message;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 消息
 * 
 */
public interface MessageDao extends BaseDao<Message> {

	/**
	 * 查找消息分页
	 * 
	 * @param member
	 *            会员，null表示管理员
	 * @param pageable
	 *            分页信息
	 * @return 消息分页
	 */
	List<Message> findPage(RowBounds rowBounds, @Param("ew")Wrapper<Message> wrapper, @Param("member")Member member);

	/**
	 * 查找草稿分页
	 * 
	 * @param sender
	 *            发件人，null表示管理员
	 * @param pageable
	 *            分页信息
	 * @return 草稿分页
	 */
	List<Message> findDraftPage(RowBounds rowBounds, @Param("ew")Wrapper<Message> wrapper, @Param("member")Member sender);

	/**
	 * 查找消息数量
	 * 
	 * @param member
	 *            会员，null表示管理员
	 * @param read
	 *            是否已读
	 * @return 消息数量，不包含草稿
	 */
	Long count(@Param("member")Member member, @Param("read")Boolean read);

}