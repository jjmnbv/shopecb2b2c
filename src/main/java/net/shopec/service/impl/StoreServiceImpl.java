package net.shopec.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.ProductCategoryStoreDao;
import net.shopec.dao.ProductDao;
import net.shopec.dao.StoreDao;
import net.shopec.entity.Business;
import net.shopec.entity.BusinessDepositLog;
import net.shopec.entity.CategoryApplication;
import net.shopec.entity.ProductCategory;
import net.shopec.entity.ProductCategoryStore;
import net.shopec.entity.Store;
import net.shopec.plugin.PromotionPlugin;
import net.shopec.plugin.discountPromotion.DiscountPromotionPlugin;
import net.shopec.plugin.fullReductionPromotion.FullReductionPromotionPlugin;
import net.shopec.service.BusinessService;
import net.shopec.service.StoreService;
import net.shopec.service.UserService;

/**
 * Service - 店铺
 * 
 */
@Service
public class StoreServiceImpl extends BaseServiceImpl<Store> implements StoreService {

	@Inject
	private StoreDao storeDao;
	@Inject
	private ProductDao productDao;
	@Inject
	private ProductCategoryStoreDao productCategoryStoreDao;
	@Inject
	private UserService userService;
	@Inject
	private BusinessService businessService;
//	@Inject
//	private MailService mailService;
//	@Inject
//	private SmsService smsService;

	@Override
	@Transactional(readOnly = true)
	public Store find(Long id) {
		return storeDao.find(id);
	}
	
	@Transactional(readOnly = true)
	public boolean nameExists(String name) {
		return storeDao.exists("name", name);
	}

	@Transactional(readOnly = true)
	public boolean nameUnique(Long id, String name) {
		return storeDao.unique(id, "name", name);
	}

	public boolean productCategoryExists(Store store, final ProductCategory productCategory) {
		Assert.notNull(productCategory, "notNull");
		Assert.notNull(store, "notNull");

		return CollectionUtils.exists(store.getProductCategories(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				ProductCategory storeProductCategory = (ProductCategory) object;
				return storeProductCategory != null && storeProductCategory.equals(productCategory);
			}
		});
	}

	@Transactional(readOnly = true)
	public Store findByName(String name) {
		return find("name", name);
	}

	@Transactional(readOnly = true)
	public List<Store> findList(Store.Type type, Store.Status status, Boolean isEnabled, Boolean hasExpired, Integer first, Integer count) {
		EntityWrapper<Store> wrapper = toWrapper(first, count, null, null);
		return storeDao.findList(wrapper, type, status, isEnabled, hasExpired);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findProductCategoryList(Store store, CategoryApplication.Status status) {
		return storeDao.findProductCategoryList(store, status);
	}

	@Transactional(readOnly = true)
	public Page<Store> findPage(Store.Type type, Store.Status status, Boolean isEnabled, Boolean hasExpired, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<Store> plusPage = getPlusPage(pageable);
		plusPage.setRecords(storeDao.findPage(type, status, isEnabled, hasExpired, plusPage, getWrapper(pageable)));
		return super.findPage(plusPage, pageable);
	}

	@Transactional(readOnly = true)
	public Store getCurrent() {
		Business currentUser = userService.getCurrent(Business.class);
		return currentUser != null ? currentUser.getStore() : null;
	}

	@CacheEvict(value = "authorization", allEntries = true)
	public void addEndDays(Store store, int amount) {
		Assert.notNull(store, "notNull");

		if (amount == 0) {
			return;
		}

//		if (!LockModeType.PESSIMISTIC_WRITE.equals(storeDao.getLockMode(store))) {
//			storeDao.flush();
//			storeDao.refresh(store, LockModeType.PESSIMISTIC_WRITE);
//		}

		Date now = new Date();
		Date currentEndDate = store.getEndDate();
		if (amount > 0) {
			store.setEndDate(DateUtils.addDays(currentEndDate.after(now) ? currentEndDate : now, amount));
		} else {
			store.setEndDate(DateUtils.addDays(currentEndDate, amount));
		}
		storeDao.update(store);
		//storeDao.flush();
	}

	public void addDiscountPromotionEndDays(Store store, int amount) {
		Assert.notNull(store, "notNull");

		if (amount == 0) {
			return;
		}

//		if (!LockModeType.PESSIMISTIC_WRITE.equals(storeDao.getLockMode(store))) {
//			storeDao.flush();
//			storeDao.refresh(store, LockModeType.PESSIMISTIC_WRITE);
//		}

		Date now = new Date();
		Date currentDiscountPromotionEndDate = store.getDiscountPromotionEndDate() != null ? store.getDiscountPromotionEndDate() : now;
		if (amount > 0) {
			store.setDiscountPromotionEndDate(DateUtils.addDays(currentDiscountPromotionEndDate.after(now) ? currentDiscountPromotionEndDate : now, amount));
		} else {
			store.setDiscountPromotionEndDate(DateUtils.addDays(currentDiscountPromotionEndDate, amount));
		}
		//storeDao.flush();
		storeDao.update(store);
	}

	public void addFullReductionPromotionEndDays(Store store, int amount) {
		Assert.notNull(store, "notNull");

		if (amount == 0) {
			return;
		}

//		if (!LockModeType.PESSIMISTIC_WRITE.equals(storeDao.getLockMode(store))) {
//			storeDao.flush();
//			storeDao.refresh(store, LockModeType.PESSIMISTIC_WRITE);
//		}

		Date now = new Date();
		Date currentFullReductionPromotionEndDate = store.getFullReductionPromotionEndDate() != null ? store.getFullReductionPromotionEndDate() : now;
		if (amount > 0) {
			store.setFullReductionPromotionEndDate(DateUtils.addDays(currentFullReductionPromotionEndDate.after(now) ? currentFullReductionPromotionEndDate : now, amount));
		} else {
			store.setFullReductionPromotionEndDate(DateUtils.addDays(currentFullReductionPromotionEndDate, amount));
		}
		//storeDao.flush();
		storeDao.update(store);
	}

	@CacheEvict(value = "authorization", allEntries = true)
	public void addBailPaid(Store store, BigDecimal amount) {
		Assert.notNull(store, "notNull");
		Assert.notNull(amount, "notNull");

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

//		if (!LockModeType.PESSIMISTIC_WRITE.equals(storeDao.getLockMode(store))) {
//			storeDao.flush();
//			storeDao.refresh(store, LockModeType.PESSIMISTIC_WRITE);
//		}

		Assert.notNull(store.getBailPaid(), "notNull");
		Assert.state(store.getBailPaid().add(amount).compareTo(BigDecimal.ZERO) >= 0, "state");

		store.setBailPaid(store.getBailPaid().add(amount));
		//storeDao.flush();
		storeDao.update(store);
	}

	@CacheEvict(value = "authorization", allEntries = true)
	public void review(Store store, boolean passed, String content) {
		Assert.notNull(store, "notNull");
		Assert.state(Store.Status.pending.equals(store.getStatus()), "state");
		Assert.state(passed || StringUtils.isNotEmpty(content), "state");

		if (passed) {
			BigDecimal serviceFee = store.getStoreRank().getServiceFee();
			BigDecimal bail = store.getStoreCategory().getBail();
			if (serviceFee.compareTo(BigDecimal.ZERO) <= 0 && bail.compareTo(BigDecimal.ZERO) <= 0) {
				store.setStatus(Store.Status.success);
				store.setEndDate(DateUtils.addYears(new Date(), 1));
			} else {
				store.setStatus(Store.Status.approved);
				store.setEndDate(new Date());
			}
//			smsService.sendApprovalStoreSms(store);
//			mailService.sendApprovalStoreMail(store);
		} else {
			store.setStatus(Store.Status.failed);
//			smsService.sendFailStoreSms(store, content);
//			mailService.sendFailStoreMail(store, content);
		}
		storeDao.update(store);
	}

	public void buy(Store store, PromotionPlugin promotionPlugin, int months) {
		Assert.notNull(store, "notNull");
		Assert.notNull(promotionPlugin, "notNull");
		Assert.state(promotionPlugin.getIsEnabled(), "state");
		Assert.state(months > 0, "state");

		BigDecimal amount = promotionPlugin.getPrice().multiply(new BigDecimal(months));
		Business business = store.getBusiness();
		Assert.state(business.getBalance() != null && business.getBalance().compareTo(amount) >= 0, "state");

		int days = months * 30;
		if (promotionPlugin instanceof DiscountPromotionPlugin) {
			addDiscountPromotionEndDays(store, days);
			businessService.addBalance(business, amount.negate(), BusinessDepositLog.Type.svcPayment, null);
		} else if (promotionPlugin instanceof FullReductionPromotionPlugin) {
			addFullReductionPromotionEndDays(store, days);
			businessService.addBalance(business, amount.negate(), BusinessDepositLog.Type.svcPayment, null);
		}
	}

	@CacheEvict(value = { "authorization", "product", "productCategory" }, allEntries = true)
	public void expiredStoreProcessing() {
		productDao.refreshExpiredStoreProductActive();
	}

	@Override
	@Transactional
	public Store save(Store store) {
		super.save(store);
		setProductCategoryStore(store);
		return store;
	}
	
	
	
	@Override
	@Transactional
	@CacheEvict(value = { "authorization", "product", "productCategory" }, allEntries = true)
	public Store update(Store store) {
		productDao.refreshActive(store);
		
		setProductCategoryStore(store);
		return super.update(store);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "authorization", "product", "productCategory" }, allEntries = true)
	public Store update(Store store, String... ignoreProperties) {
		return super.update(store, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "authorization", "product", "productCategory" }, allEntries = true)
	public void delete(Long id) {
		deleteById(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "authorization", "product", "productCategory" }, allEntries = true)
	public void delete(Long... ids) {
		if (ids != null) {
			for (Long id : ids) {
				EntityWrapper<ProductCategoryStore> wrapper = new EntityWrapper<ProductCategoryStore>();
				wrapper.where("stores_id = {0}", id);
				productCategoryStoreDao.delete(wrapper);
				delete(id);
			}
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = { "authorization", "product", "productCategory" }, allEntries = true)
	public void delete(Store store) {
		super.delete(store);
	}

	/**
	 * 设置经营分类中间表
	 */
	private void setProductCategoryStore(Store store) {
		EntityWrapper<ProductCategoryStore> wrapper = new EntityWrapper<ProductCategoryStore>();
		wrapper.where("stores_id = {0}", store.getId());
		productCategoryStoreDao.delete(wrapper);
		List<ProductCategoryStore> productCategoryStores = new ArrayList<ProductCategoryStore>();
		for (ProductCategory productCategory : store.getProductCategories()) {
			ProductCategoryStore productCategoryStore = new ProductCategoryStore();
			productCategoryStore.setStoresId(store.getId());
			productCategoryStore.setProductCategoriesId(productCategory.getId());
			productCategoryStores.add(productCategoryStore);
		}
		if (CollectionUtils.isNotEmpty(productCategoryStores)) {
			productCategoryStoreDao.batchSave(productCategoryStores);
		}
	}
}