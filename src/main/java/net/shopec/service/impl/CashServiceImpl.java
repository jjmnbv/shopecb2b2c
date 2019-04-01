package net.shopec.service.impl;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.CashDao;
import net.shopec.entity.Business;
import net.shopec.entity.BusinessDepositLog;
import net.shopec.entity.Cash;
import net.shopec.service.BusinessService;
import net.shopec.service.CashService;

/**
 * Service - 提现
 * 
 */
@Service
public class CashServiceImpl extends BaseServiceImpl<Cash> implements CashService {

	@Inject
	private CashDao cashDao;
	@Inject
	private BusinessService businessService;

	@Transactional(readOnly = true)
	public Page<Cash> findPage(Business business, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<Cash> plusPage = getPlusPage(pageable);
		plusPage.setRecords(cashDao.findPage(plusPage, getWrapper(pageable), business));
		return super.findPage(plusPage, pageable);
	}

	@Transactional
	public void applyCash(Cash cash, Business business) {
		Assert.notNull(cash, "notNull");
		Assert.notNull(business, "notNull");
		Assert.isTrue(cash.getAmount().compareTo(BigDecimal.ZERO) > 0, "isTrue");

		cash.setStatus(Cash.Status.pending);
		cash.setBusiness(business);
		super.save(cash);

		businessService.addBalance(cash.getBusiness(), cash.getAmount().negate(), BusinessDepositLog.Type.cash, null);
		businessService.addFrozenFund(business, cash.getAmount());

	}

	public void review(Cash cash, Boolean isPassed) {
		Assert.notNull(cash, "notNull");
		Assert.notNull(isPassed, "notNull");

		Business business = cash.getBusiness();
		if (isPassed) {
			Assert.notNull(cash.getAmount(), "notNull");
			Assert.notNull(cash.getBusiness(), "notNull");
			Assert.notNull(cash.getBusiness(), "notNull");
			cash.setStatus(Cash.Status.approved);
		} else {
			cash.setStatus(Cash.Status.failed);
			businessService.addBalance(cash.getBusiness(), cash.getAmount(), BusinessDepositLog.Type.unfrozen, null);
		}
		super.update(cash);
		businessService.addFrozenFund(business, cash.getAmount().negate());
	}

}