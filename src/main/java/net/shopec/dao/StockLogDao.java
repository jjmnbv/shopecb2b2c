package net.shopec.dao;

import java.util.List;

import net.shopec.entity.StockLog;
import net.shopec.entity.Store;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 库存记录
 * 
 */
public interface StockLogDao extends BaseDao<StockLog> {

	/**
	 * 查找库存记录分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 库存记录分页
	 */
	List<StockLog> findPage(RowBounds rowBounds, @Param("ew")Wrapper<StockLog> wrapper, @Param("store")Store store);

}