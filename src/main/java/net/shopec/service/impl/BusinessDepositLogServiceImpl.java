package net.shopec.service.impl;


import javax.inject.Inject;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.BusinessDepositLogDao;
import net.shopec.entity.Business;
import net.shopec.entity.BusinessDepositLog;
import net.shopec.service.BusinessDepositLogService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 商家预存款记录
 * 
 */
@Service
public class BusinessDepositLogServiceImpl extends BaseServiceImpl<BusinessDepositLog> implements BusinessDepositLogService {

	@Inject
	private BusinessDepositLogDao businessDepositLogDao;

	@Transactional(readOnly = true)
	public Page<BusinessDepositLog> findPage(Business business, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<BusinessDepositLog> plusPage = getPlusPage(pageable);
		plusPage.setRecords(businessDepositLogDao.findPage(plusPage, getWrapper(pageable), business));
		return super.findPage(plusPage, pageable);
	}

}