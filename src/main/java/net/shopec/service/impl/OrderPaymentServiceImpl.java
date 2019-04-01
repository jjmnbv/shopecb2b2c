package net.shopec.service.impl;


import javax.inject.Inject;

import net.shopec.dao.OrderPaymentDao;
import net.shopec.entity.OrderPayment;
import net.shopec.entity.Sn;
import net.shopec.service.OrderPaymentService;
import net.shopec.service.SnService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service - 订单支付
 * 
 */
@Service
public class OrderPaymentServiceImpl extends BaseServiceImpl<OrderPayment> implements OrderPaymentService {

	@Inject
	private OrderPaymentDao orderPaymentDao;
	@Inject
	private SnService snService;

	@Override
	public OrderPayment find(Long id) {
		return orderPaymentDao.find(id);
	}
	
	@Transactional(readOnly = true)
	public OrderPayment findBySn(String sn) {
		return orderPaymentDao.findByAttribute("sn", StringUtils.lowerCase(sn));
	}

	@Override
	@Transactional
	public OrderPayment save(OrderPayment orderPayment) {
		Assert.notNull(orderPayment, "notNull");

		orderPayment.setSn(snService.generate(Sn.Type.orderPayment));

		return super.save(orderPayment);
	}

}