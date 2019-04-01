package net.shopec.service;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.Business;
import net.shopec.entity.BusinessDepositLog;

/**
 * Service - 商家预存款记录
 * 
 */
public interface BusinessDepositLogService extends BaseService<BusinessDepositLog> {

	/**
	 * 查找商家预存款记录分页
	 * 
	 * @param business
	 *            商家
	 * @param pageable
	 *            分页信息
	 * @return 商家预存款记录分页
	 */
	Page<BusinessDepositLog> findPage(Business business, Pageable pageable);

}