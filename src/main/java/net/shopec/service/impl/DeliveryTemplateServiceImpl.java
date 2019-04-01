package net.shopec.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.DeliveryTemplateDao;
import net.shopec.entity.DeliveryCenter;
import net.shopec.entity.DeliveryTemplate;
import net.shopec.entity.Order;
import net.shopec.entity.Store;
import net.shopec.service.DeliveryTemplateService;

/**
 * Service - 快递单模板
 * 
 */
@Service
public class DeliveryTemplateServiceImpl extends BaseServiceImpl<DeliveryTemplate> implements DeliveryTemplateService {

	@Autowired
	private DeliveryTemplateDao deliveryTemplateDao;
	
	@Transactional(readOnly = true)
	public DeliveryTemplate findDefault(Store store) {
		return deliveryTemplateDao.findDefault(store);
	}

	@Transactional(readOnly = true)
	public List<DeliveryTemplate> findList(Store store) {
		return deliveryTemplateDao.findList(store);
	}

	@Transactional(readOnly = true)
	public Page<DeliveryTemplate> findPage(Store store, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<DeliveryTemplate> plusPage = getPlusPage(pageable);
		plusPage.setRecords(deliveryTemplateDao.findPage(plusPage, getWrapper(pageable), store));
		return super.findPage(plusPage, pageable);
	}

	@Transactional(readOnly = true)
	public String resolveContent(DeliveryTemplate deliveryTemplate, Store store, DeliveryCenter deliveryCenter, Order order) {
		Assert.notNull(deliveryTemplate, "NotNull");

		List<String> tagNames = new ArrayList<>();
		List<String> values = new ArrayList<>();

		for (DeliveryTemplate.StoreAttribute storeAttribute : DeliveryTemplate.StoreAttribute.values()) {
			tagNames.add(storeAttribute.getTagName());
			values.add(storeAttribute.getValue(store));
		}
		for (DeliveryTemplate.DeliveryCenterAttribute deliveryCenterAttribute : DeliveryTemplate.DeliveryCenterAttribute.values()) {
			tagNames.add(deliveryCenterAttribute.getTagName());
			values.add(deliveryCenterAttribute.getValue(deliveryCenter));
		}
		for (DeliveryTemplate.OrderAttribute orderAttribute : DeliveryTemplate.OrderAttribute.values()) {
			tagNames.add(orderAttribute.getTagName());
			values.add(orderAttribute.getValue(order));
		}

		return StringUtils.replaceEachRepeatedly(deliveryTemplate.getContent(), tagNames.toArray(new String[tagNames.size()]), values.toArray(new String[values.size()]));
	}

	@Transactional
	public DeliveryTemplate save(DeliveryTemplate deliveryTemplate) {
		Assert.notNull(deliveryTemplate, "NotNull");

		if (BooleanUtils.isTrue(deliveryTemplate.getIsDefault())) {
			deliveryTemplateDao.clearDefault(deliveryTemplate.getStore());
		}
		return super.save(deliveryTemplate);
	}

	@Transactional
	public DeliveryTemplate update(DeliveryTemplate deliveryTemplate) {
		Assert.notNull(deliveryTemplate, "NotNull");

		DeliveryTemplate pDeliveryTemplate = super.update(deliveryTemplate);
		if (BooleanUtils.isTrue(pDeliveryTemplate.getIsDefault())) {
			deliveryTemplateDao.clearDefaultExclude(pDeliveryTemplate);
		}
		return pDeliveryTemplate;
	}

}