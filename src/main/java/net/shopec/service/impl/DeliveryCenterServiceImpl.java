package net.shopec.service.impl;

import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.DeliveryCenterDao;
import net.shopec.entity.DeliveryCenter;
import net.shopec.entity.Store;
import net.shopec.service.DeliveryCenterService;

/**
 * Service - 发货点
 * 
 */
@Service
public class DeliveryCenterServiceImpl extends BaseServiceImpl<DeliveryCenter> implements DeliveryCenterService {

	@Autowired
	private DeliveryCenterDao deliveryCenterDao;

	@Override
	public DeliveryCenter find(Long id) {
		return deliveryCenterDao.find(id);
	}
	
	@Transactional(readOnly = true)
	public DeliveryCenter findDefault(Store store) {
		return deliveryCenterDao.findDefault(store);
	}

	@Transactional
	public DeliveryCenter save(DeliveryCenter deliveryCenter) {
		Assert.notNull(deliveryCenter, "NotNull");

		if (BooleanUtils.isTrue(deliveryCenter.getIsDefault())) {
			deliveryCenterDao.clearDefault(deliveryCenter.getStore());
		}
		if (deliveryCenter.getArea() != null) {
			deliveryCenter.setAreaName(deliveryCenter.getArea().getFullName());
		}
		return super.save(deliveryCenter);
	}

	@Transactional
	public DeliveryCenter update(DeliveryCenter deliveryCenter) {
		Assert.notNull(deliveryCenter, "NotNull");

		if (deliveryCenter.getArea() != null) {
			deliveryCenter.setAreaName(deliveryCenter.getArea().getFullName());
		}
		DeliveryCenter pDeliveryCenter = super.update(deliveryCenter);
		if (BooleanUtils.isTrue(pDeliveryCenter.getIsDefault())) {
			deliveryCenterDao.clearDefaultExclude(pDeliveryCenter);
		}
		return pDeliveryCenter;
	}

	@Transactional(readOnly = true)
	public Page<DeliveryCenter> findPage(Store store, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<DeliveryCenter> plusPage = getPlusPage(pageable);
		plusPage.setRecords(deliveryCenterDao.findPage(plusPage, getWrapper(pageable), store));
		return super.findPage(plusPage, pageable);
	}

	@Transactional(readOnly = true)
	public List<DeliveryCenter> findAll(Store store) {
		return deliveryCenterDao.findAll(store);
	}

}