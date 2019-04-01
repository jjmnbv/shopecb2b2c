package net.shopec.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import net.shopec.Setting;
import net.shopec.dao.DefaultFreightConfigDao;
import net.shopec.dao.ShippingMethodPaymentMethodDao;
import net.shopec.entity.Area;
import net.shopec.entity.AreaFreightConfig;
import net.shopec.entity.DefaultFreightConfig;
import net.shopec.entity.PaymentMethod;
import net.shopec.entity.Receiver;
import net.shopec.entity.ShippingMethod;
import net.shopec.entity.ShippingMethodPaymentMethod;
import net.shopec.entity.Store;
import net.shopec.service.ShippingMethodService;
import net.shopec.util.SystemUtils;

/**
 * Service - 配送方式
 * 
 */
@Service
public class ShippingMethodServiceImpl extends BaseServiceImpl<ShippingMethod> implements ShippingMethodService {

	@Inject
	private DefaultFreightConfigDao defaultFreightConfigDao;
	@Inject
	private ShippingMethodPaymentMethodDao shippingMethodPaymentMethodDao;

	@Transactional(readOnly = true)
	public BigDecimal calculateFreight(ShippingMethod shippingMethod, Store store, Area area, Integer weight) {
		Assert.notNull(shippingMethod, "notNull");

		Setting setting = SystemUtils.getSetting();
		DefaultFreightConfig defaultFreightConfig = defaultFreightConfigDao.findDefault(shippingMethod, store);
		BigDecimal firstPrice = defaultFreightConfig != null ? defaultFreightConfig.getFirstPrice() : BigDecimal.ZERO;
		BigDecimal continuePrice = defaultFreightConfig != null ? defaultFreightConfig.getContinuePrice() : BigDecimal.ZERO;
		Integer firstWeight = defaultFreightConfig != null ? defaultFreightConfig.getFirstWeight() : 0;
		Integer continueWeight = defaultFreightConfig != null ? defaultFreightConfig.getContinueWeight() : 1;
		if (area != null && CollectionUtils.isNotEmpty(shippingMethod.getAreaFreightConfigs())) {
			List<Area> areas = new ArrayList<>();
			areas.addAll(area.getParents());
			areas.add(area);
			for (int i = areas.size() - 1; i >= 0; i--) {
				AreaFreightConfig areaFreightConfig = shippingMethod.getAreaFreightConfig(store, areas.get(i));
				if (areaFreightConfig != null) {
					firstPrice = areaFreightConfig.getFirstPrice();
					continuePrice = areaFreightConfig.getContinuePrice();
					firstWeight = areaFreightConfig.getFirstWeight();
					continueWeight = areaFreightConfig.getContinueWeight();
					break;
				}
			}
		}
		if (weight == null || weight <= firstWeight || continuePrice.compareTo(BigDecimal.ZERO) == 0) {
			return setting.setScale(firstPrice);
		} else {
			double contiuneWeightCount = Math.ceil((weight - firstWeight) / (double) continueWeight);
			return setting.setScale(firstPrice.add(continuePrice.multiply(new BigDecimal(String.valueOf(contiuneWeightCount)))));
		}
	}

	@Transactional(readOnly = true)
	public BigDecimal calculateFreight(ShippingMethod shippingMethod, Store store, Receiver receiver, Integer weight) {
		return calculateFreight(shippingMethod, store, receiver != null ? receiver.getArea() : null, weight);
	}

	@Override
	public ShippingMethod save(ShippingMethod shippingMethod) {
		super.save(shippingMethod);
		setPaymentMethods(shippingMethod);
		return shippingMethod;
	}
	
	@Override
	public ShippingMethod update(ShippingMethod shippingMethod, String... ignoreProperties) {
		setPaymentMethods(shippingMethod);
		return super.update(shippingMethod, ignoreProperties);
	}



	private void setPaymentMethods(ShippingMethod shippingMethod) {
		EntityWrapper<ShippingMethodPaymentMethod> wrapper = new EntityWrapper<ShippingMethodPaymentMethod>();
		wrapper.where("shipping_methods_id = {0}", shippingMethod.getId());
		shippingMethodPaymentMethodDao.delete(wrapper);
		List<ShippingMethodPaymentMethod> shippingMethodPaymentMethods = new ArrayList<>();
		for (PaymentMethod paymentMethod : shippingMethod.getPaymentMethods()) {
			ShippingMethodPaymentMethod shippingMethodPaymentMethod = new ShippingMethodPaymentMethod();
			shippingMethodPaymentMethod.setPaymentMethodsId(paymentMethod.getId());
			shippingMethodPaymentMethod.setShippingMethodsId(shippingMethod.getId());
			shippingMethodPaymentMethods.add(shippingMethodPaymentMethod);
		}
		if (CollectionUtils.isNotEmpty(shippingMethodPaymentMethods)) {
			shippingMethodPaymentMethodDao.batchSave(shippingMethodPaymentMethods);
		}
	}


}