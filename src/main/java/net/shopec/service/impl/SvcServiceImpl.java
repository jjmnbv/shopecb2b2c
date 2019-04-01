package net.shopec.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.shopec.Order;
import net.shopec.dao.SvcDao;
import net.shopec.entity.Sn;
import net.shopec.entity.Store;
import net.shopec.entity.StoreRank;
import net.shopec.entity.Svc;
import net.shopec.service.SnService;
import net.shopec.service.SvcService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service - 服务
 * 
 */
@Service
public class SvcServiceImpl extends BaseServiceImpl<Svc> implements SvcService {

	@Inject
	private SvcDao svcDao;
	@Inject
	private SnService snService;

	@Transactional(readOnly = true)
	public Svc findBySn(String sn) {
		return find("sn", StringUtils.lowerCase(sn));
	}

	@Transactional(readOnly = true)
	public Svc findTheLatest(Store store, String promotionPluginId, StoreRank storeRank) {

		List<Order> orderList = new ArrayList<>();
		orderList.add(new Order("createdDate", Order.Direction.desc));
		List<Svc> serviceOrders = svcDao.find(store, promotionPluginId, storeRank, orderList);

		return CollectionUtils.isNotEmpty(serviceOrders) ? serviceOrders.get(0) : null;
	}

	@Override
	@Transactional
	public Svc save(Svc svc) {
		Assert.notNull(svc, "notNull");

		svc.setSn(snService.generate(Sn.Type.platformService));

		return super.save(svc);
	}

}