package net.shopec.dao;

import java.util.List;

import net.shopec.entity.InstantMessage;
import net.shopec.entity.Store;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 即时通讯
 * 
 */
public interface InstantMessageDao extends BaseDao<InstantMessage> {

	/**
	 * 查找即时通讯分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 即时通讯分页
	 */
	List<InstantMessage> findPage(RowBounds rowBounds, @Param("ew")Wrapper<InstantMessage> wrapper, @Param("store")Store store);

}