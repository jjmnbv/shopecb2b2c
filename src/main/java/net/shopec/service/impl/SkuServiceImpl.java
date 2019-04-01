package net.shopec.service.impl;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.IdWorker;

import net.shopec.dao.SkuDao;
import net.shopec.dao.StockLogDao;
import net.shopec.entity.Product;
import net.shopec.entity.Sku;
import net.shopec.entity.StockLog;
import net.shopec.entity.Store;
import net.shopec.service.SkuService;

/**
 * Service - SKU
 * 
 */
@Service
public class SkuServiceImpl extends BaseServiceImpl<Sku> implements SkuService {

	@Inject
	private SkuDao skuDao;
	@Inject
	private StockLogDao stockLogDao;

	@Override
	public Sku find(Long id) {
		return skuDao.find(id);
	}
	
	@Transactional(readOnly = true)
	public boolean snExists(String sn) {
		return skuDao.exists("sn", StringUtils.lowerCase(sn));
	}

	@Transactional(readOnly = true)
	public Sku findBySn(String sn) {
		return skuDao.findByAttribute("sku.sn", StringUtils.lowerCase(sn));
	}

	@Transactional(readOnly = true)
	public List<Sku> search(Store store, Product.Type type, String keyword, Set<Sku> excludes, Integer count) {
		return skuDao.search(store, type, keyword, excludes, count);
	}

	public void addStock(Sku sku, int amount, StockLog.Type type, String memo) {
		Assert.notNull(sku, "notNull");
		Assert.notNull(type, "notNull");

		if (amount == 0) {
			return;
		}


		Assert.notNull(sku.getStock(), "notNull");
		Assert.state(sku.getStock() + amount >= 0, "state");

		sku.setStock(sku.getStock() + amount);
		skuDao.updateById(sku);

		StockLog stockLog = new StockLog();
		stockLog.setId(IdWorker.getId());
		stockLog.setType(type);
		stockLog.setInQuantity(amount > 0 ? amount : 0);
		stockLog.setOutQuantity(amount < 0 ? Math.abs(amount) : 0);
		stockLog.setStock(sku.getStock());
		stockLog.setMemo(memo);
		stockLog.setSku(sku);
		stockLogDao.save(stockLog);
	}

	public void addAllocatedStock(Sku sku, int amount) {
		Assert.notNull(sku, "notNull");

		if (amount == 0) {
			return;
		}

//		if (!LockModeType.PESSIMISTIC_WRITE.equals(skuDao.getLockMode(sku))) {
//			skuDao.flush();
//			skuDao.refresh(sku, LockModeType.PESSIMISTIC_WRITE);
//		}

		Assert.notNull(sku.getAllocatedStock(), "notNull");
		Assert.state(sku.getAllocatedStock() + amount >= 0, "state");

		sku.setAllocatedStock(sku.getAllocatedStock() + amount);
		skuDao.updateById(sku);
		//skuDao.flush();
	}

	@Transactional(readOnly = true)
	public void filter(List<Sku> skus) {
		CollectionUtils.filter(skus, new Predicate() {
			public boolean evaluate(Object object) {
				Sku sku = (Sku) object;
				return sku != null && sku.getStock() != null;
			}
		});
	}

	@Override
	@Transactional
	public void delete(Sku sku) {
		// 库存记录
		EntityWrapper<StockLog> skuWrapper = new EntityWrapper<StockLog>();
		skuWrapper.where("sku_id = {0}", sku.getId());
		stockLogDao.delete(skuWrapper);
		super.delete(sku);
	}
}