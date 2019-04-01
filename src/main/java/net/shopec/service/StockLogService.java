package net.shopec.service;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.StockLog;
import net.shopec.entity.Store;

/**
 * Service - 库存记录
 * 
 */
public interface StockLogService extends BaseService<StockLog> {

	/**
	 * 查找库存记录分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 库存记录分页
	 */
	Page<StockLog> findPage(Store store, Pageable pageable);

}