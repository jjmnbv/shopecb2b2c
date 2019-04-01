package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Business;
import net.shopec.entity.Cash;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 提现
 * 
 */
public interface CashDao extends BaseDao<Cash> {

	/**
	 * 查找提现记录分页
	 * 
	 * @param business
	 *            商家
	 * @param pageable
	 *            分页信息
	 * @return 提现记录分页
	 */
	List<Cash> findPage(RowBounds rowBounds, @Param("ew")Wrapper<Cash> wrapper, @Param("business")Business business);

}