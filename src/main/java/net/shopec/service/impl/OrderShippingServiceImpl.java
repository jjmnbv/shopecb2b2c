package net.shopec.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.shopec.Setting;
import net.shopec.dao.OrderShippingDao;
import net.shopec.entity.OrderShipping;
import net.shopec.entity.Sn;
import net.shopec.service.OrderShippingService;
import net.shopec.service.SnService;
import net.shopec.util.JsonUtils;
import net.shopec.util.SystemUtils;
import net.shopec.util.WebUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Service - 订单发货
 * 
 */
@Service
public class OrderShippingServiceImpl extends BaseServiceImpl<OrderShipping> implements OrderShippingService {

	@Inject
	private OrderShippingDao orderShippingDao;
	@Inject
	private SnService snService;
	
	@Override
	public OrderShipping find(Long id) {
		return orderShippingDao.find(id);
	}
	
	@Transactional(readOnly = true)
	public OrderShipping findBySn(String sn) {
		return orderShippingDao.findByAttribute("sn", StringUtils.lowerCase(sn));
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Cacheable("transitSteps")
	public List<Map<String, String>> getTransitSteps(OrderShipping orderShipping) {
		Assert.notNull(orderShipping, "notNull");

		Setting setting = SystemUtils.getSetting();
		if (StringUtils.isEmpty(setting.getKuaidi100Key()) || StringUtils.isEmpty(orderShipping.getDeliveryCorpCode()) || StringUtils.isEmpty(orderShipping.getTrackingNo())) {
			return Collections.emptyList();
		}
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("id", setting.getKuaidi100Key());
		parameterMap.put("com", orderShipping.getDeliveryCorpCode());
		parameterMap.put("nu", orderShipping.getTrackingNo());
		parameterMap.put("show", "0");
		parameterMap.put("muti", "1");
		parameterMap.put("order", "asc");
		String content = WebUtils.get("http://api.kuaidi100.com/api", parameterMap);
		Map<String, Object> data = JsonUtils.toObject(content, new TypeReference<Map<String, Object>>() {
		});
		if (!StringUtils.equals(String.valueOf(data.get("status")), "1")) {
			return Collections.emptyList();
		}
		return (List<Map<String, String>>) data.get("data");
	}

	@Override
	@Transactional
	public OrderShipping save(OrderShipping orderShipping) {
		Assert.notNull(orderShipping, "notNull");

		orderShipping.setSn(snService.generate(Sn.Type.orderShipping));

		return super.save(orderShipping);
	}

}