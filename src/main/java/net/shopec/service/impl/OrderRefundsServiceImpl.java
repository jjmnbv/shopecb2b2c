package net.shopec.service.impl;

import javax.inject.Inject;

import net.shopec.dao.OrderRefundsDao;
import net.shopec.entity.OrderRefunds;
import net.shopec.entity.Sn;
import net.shopec.service.OrderRefundsService;
import net.shopec.service.SnService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service - 订单退款
 * 
 */
@Service
public class OrderRefundsServiceImpl extends BaseServiceImpl<OrderRefunds> implements OrderRefundsService {

	@Inject
	private SnService snService;
	@Inject
	private OrderRefundsDao orderRefundsDao;

	@Override
	public OrderRefunds find(Long id) {
		return orderRefundsDao.find(id);
	}
	
	@Override
	@Transactional
	public OrderRefunds save(OrderRefunds orderRefunds) {
		Assert.notNull(orderRefunds, "notNull");

		orderRefunds.setSn(snService.generate(Sn.Type.orderRefunds));

		return super.save(orderRefunds);
	}

}