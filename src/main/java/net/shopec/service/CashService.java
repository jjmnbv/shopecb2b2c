package net.shopec.service;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.Business;
import net.shopec.entity.Cash;

/**
 * Service - 提现
 * 
 */
public interface CashService extends BaseService<Cash> {

	/**
	 * 申请提现
	 * 
	 * @param cash
	 *            提现
	 * @param business
	 *            商家
	 */
	void applyCash(Cash cash, Business business);

	/**
	 * 查找提现记录分页
	 * 
	 * @param business
	 *            商家
	 * @param pageable
	 *            分页信息
	 * @return 提现记录分页
	 */
	Page<Cash> findPage(Business business, Pageable pageable);

	/**
	 * 审核提现
	 * 
	 * @param cash
	 *            提现
	 * @param isPassed
	 *            是否审核通过
	 */
	void review(Cash cash, Boolean isPassed);
}