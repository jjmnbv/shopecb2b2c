package net.shopec.service.impl;

import javax.inject.Inject;

import net.shopec.dao.OrderReturnsDao;
import net.shopec.entity.OrderReturns;
import net.shopec.entity.Sn;
import net.shopec.service.OrderReturnsService;
import net.shopec.service.SnService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service - 订单退货
 * 
 */
@Service
public class OrderReturnsServiceImpl extends BaseServiceImpl<OrderReturns> implements OrderReturnsService {

	@Inject
	private SnService snService;
	@Inject
	private OrderReturnsDao orderReturnsDao;

	@Override
	public OrderReturns find(Long id) {
		return orderReturnsDao.find(id);
	}
	
	@Override
	@Transactional
	public OrderReturns save(OrderReturns orderReturns) {
		Assert.notNull(orderReturns, "notNull");

		orderReturns.setSn(snService.generate(Sn.Type.orderReturns));

		return super.save(orderReturns);
	}

}