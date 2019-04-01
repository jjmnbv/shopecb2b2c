package net.shopec.service.impl;

import javax.inject.Inject;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.StockLogDao;
import net.shopec.entity.StockLog;
import net.shopec.entity.Store;
import net.shopec.service.StockLogService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 库存记录
 * 
 */
@Service
public class StockLogServiceImpl extends BaseServiceImpl<StockLog> implements StockLogService {

	@Inject
	private StockLogDao stockLogDao;

	@Transactional(readOnly = true)
	public Page<StockLog> findPage(Store store, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<StockLog> plusPage = getPlusPage(pageable);
		plusPage.setRecords(stockLogDao.findPage(plusPage, getWrapper(pageable), store));
		return super.findPage(plusPage, pageable);
	}

}