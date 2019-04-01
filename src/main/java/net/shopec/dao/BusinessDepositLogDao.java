package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Business;
import net.shopec.entity.BusinessDepositLog;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 商家预存款记录
 * 
 */
public interface BusinessDepositLogDao extends BaseDao<BusinessDepositLog> {

	/**
	 * 查找商家预存款记录分页
	 * 
	 * @param business
	 *            商家
	 * @param pageable
	 *            分页信息
	 * @return 商家预存款记录分页
	 */
	List<BusinessDepositLog> findPage(RowBounds rowBounds, @Param("ew")Wrapper<BusinessDepositLog> wrapper, @Param("business")Business business);

}