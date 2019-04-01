package net.shopec.service;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.InstantMessage;
import net.shopec.entity.Store;

/**
 * Service - 即时通讯
 * 
 */
public interface InstantMessageService extends BaseService<InstantMessage> {

	/**
	 * 查找即时通讯分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 即时通讯分页
	 */
	Page<InstantMessage> findPage(Store store, Pageable pageable);

}