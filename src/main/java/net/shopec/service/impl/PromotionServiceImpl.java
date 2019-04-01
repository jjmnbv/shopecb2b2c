package net.shopec.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.MemberRankDao;
import net.shopec.dao.ProductCategoryDao;
import net.shopec.dao.PromotionCouponDao;
import net.shopec.dao.PromotionDao;
import net.shopec.dao.PromotionMemberRankDao;
import net.shopec.dao.PromotionSkuDao;
import net.shopec.dao.StoreDao;
import net.shopec.entity.Coupon;
import net.shopec.entity.MemberRank;
import net.shopec.entity.ProductCategory;
import net.shopec.entity.Promotion;
import net.shopec.entity.Promotion.Type;
import net.shopec.entity.PromotionCoupon;
import net.shopec.entity.PromotionMemberRank;
import net.shopec.entity.PromotionSku;
import net.shopec.entity.Sku;
import net.shopec.entity.Store;
import net.shopec.service.PromotionService;

/**
 * Service - 促销
 * 
 */
@Service
public class PromotionServiceImpl extends BaseServiceImpl<Promotion> implements PromotionService {

	/**
	 * 价格表达式变量
	 */
	private static final List<Map<String, Object>> PRICE_EXPRESSION_VARIABLES = new ArrayList<>();

	/**
	 * 积分表达式变量
	 */
	private static final List<Map<String, Object>> POINT_EXPRESSION_VARIABLES = new ArrayList<>();

	@Inject
	private PromotionDao promotionDao;
	@Inject
	private MemberRankDao memberRankDao;
	@Inject
	private ProductCategoryDao productCategoryDao;
	@Inject
	private StoreDao storeDao;
	@Inject
	private PromotionCouponDao promotionCouponDao;
	@Inject
	private PromotionMemberRankDao promotionMemberRankDao;
	@Inject
	private PromotionSkuDao promotionSkuDao;

	static {
		Map<String, Object> variable0 = new HashMap<>();
		Map<String, Object> variable1 = new HashMap<>();
		Map<String, Object> variable2 = new HashMap<>();
		Map<String, Object> variable3 = new HashMap<>();
		variable0.put("quantity", 99);
		variable0.put("price", new BigDecimal("99"));
		variable1.put("quantity", 99);
		variable1.put("price", new BigDecimal("9.9"));
		variable2.put("quantity", 99);
		variable2.put("price", new BigDecimal("0.99"));
		variable3.put("quantity", 99);
		variable3.put("point", 99L);
		PRICE_EXPRESSION_VARIABLES.add(variable0);
		PRICE_EXPRESSION_VARIABLES.add(variable1);
		PRICE_EXPRESSION_VARIABLES.add(variable2);
		POINT_EXPRESSION_VARIABLES.add(variable3);
	}

	@Transactional(readOnly = true)
	public boolean isValidPriceExpression(String priceExpression) {
		Assert.hasText(priceExpression, "hasText");

		for (Map<String, Object> variable : PRICE_EXPRESSION_VARIABLES) {
			try {
				Binding binding = new Binding();
				for (Map.Entry<String, Object> entry : variable.entrySet()) {
					binding.setVariable(entry.getKey(), entry.getValue());
				}
				GroovyShell groovyShell = new GroovyShell(binding);
				Object result = groovyShell.evaluate(priceExpression);
				new BigDecimal(result.toString());
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Promotion find(Long id) {
		return promotionDao.find(id);
	}
	
	@Transactional(readOnly = true)
	public List<Promotion> findList(Store store, Type type, Boolean isEnabled) {
		return promotionDao.findList(store, type, isEnabled);
	}

	@Transactional(readOnly = true)
	public List<Promotion> findList(Store store, Promotion.Type type, MemberRank memberRank, ProductCategory productCategory, Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders) {
		EntityWrapper<Promotion> wrapper = toWrapper(null, count, filters, orders);
		return promotionDao.selectList(wrapper, store, type, memberRank, productCategory, hasBegun, hasEnded);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "promotion", condition = "#useCache")
	public List<Promotion> findList(Promotion.Type type, Long storeId, Long memberRankId, Long productCategoryId, Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		Store store = storeDao.find(storeId);
		if (storeId != null && store == null) {
			return Collections.emptyList();
		}
		MemberRank memberRank = memberRankDao.find(memberRankId);
		if (memberRankId != null && memberRank == null) {
			return Collections.emptyList();
		}
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		EntityWrapper<Promotion> wrapper = toWrapper(null, count, filters, orders);
		return promotionDao.selectList(wrapper, store, type, memberRank, productCategory, hasBegun, hasEnded);
	}

	@Transactional(readOnly = true)
	public Page<Promotion> findPage(Store store, Promotion.Type type, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<Promotion> plusPage = getPlusPage(pageable);
		plusPage.setRecords(promotionDao.findPage(plusPage, getWrapper(pageable), store, type));
		return super.findPage(plusPage, pageable);
	}

	public void shutDownPromotion(Type type) {
		while (true) {
			EntityWrapper<Promotion> wrapper = toWrapper(null, 100, null, null);
			List<Promotion> promotions = promotionDao.selectList(wrapper, null, type, null, null, null, null);
			if (CollectionUtils.isNotEmpty(promotions)) {
				for (Promotion promotion : promotions) {
					promotion.setIsEnabled(false);
					promotionDao.updateById(promotion);
				}
			}
			if (promotions.size() < 100) {
				break;
			}
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = "promotion", allEntries = true)
	public Promotion save(Promotion promotion) {
		setPromotionCoupon(promotion);
		setPromotionMemberRank(promotion);
		setPromotionSku(promotion);
		return super.save(promotion);
	}

	@Override
	@Transactional
	@CacheEvict(value = "promotion", allEntries = true)
	public Promotion update(Promotion promotion) {
		setPromotionCoupon(promotion);
		setPromotionMemberRank(promotion);
		setPromotionSku(promotion);
		return super.update(promotion);
	}

	@Override
	@Transactional
	@CacheEvict(value = "promotion", allEntries = true)
	public Promotion update(Promotion promotion, String... ignoreProperties) {
		return super.update(promotion, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "promotion", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "promotion", allEntries = true)
	public void delete(Long... ids) {
		for (Long id : ids) {
			// 促销优惠券中间表
			EntityWrapper<PromotionCoupon> couponWrapper = new EntityWrapper<PromotionCoupon>();
			couponWrapper.where("promotions_id = {0}", id);
			promotionCouponDao.delete(couponWrapper);
			// 促销会员等级中间表
			EntityWrapper<PromotionMemberRank> memberRankWrapper = new EntityWrapper<PromotionMemberRank>();
			memberRankWrapper.where("promotions_id = {0}", id);
			promotionMemberRankDao.delete(memberRankWrapper);
			// 促销SKU中间表
			EntityWrapper<PromotionSku> skuWrapper = new EntityWrapper<PromotionSku>();
			skuWrapper.where("gift_promotions_id = {0}", id);
			promotionSkuDao.delete(skuWrapper);
			
			super.deleteById(id);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = "promotion", allEntries = true)
	public void delete(Promotion promotion) {
		super.delete(promotion);
	}

	/**
	 * 促销优惠券中间表
	 * 
	 */
	private void setPromotionCoupon(Promotion promotion) {
		EntityWrapper<PromotionCoupon> wrapper = new EntityWrapper<PromotionCoupon>();
		wrapper.where("promotions_id = {0}", promotion.getId());
		promotionCouponDao.delete(wrapper);
		List<PromotionCoupon> promotionCoupons = new ArrayList<PromotionCoupon>();
		for (Coupon coupon : promotion.getCoupons()) {
			PromotionCoupon promotionCoupon = new PromotionCoupon();
			promotionCoupon.setCouponsId(coupon.getId());
			promotionCoupon.setPromotionsId(promotion.getId());
			promotionCoupons.add(promotionCoupon);
		}
		if (CollectionUtils.isNotEmpty(promotionCoupons)) {
			promotionCouponDao.batchSave(promotionCoupons);
		}
	}

	/**
	 * 促销会员等级中间表
	 * 
	 */
	private void setPromotionMemberRank(Promotion promotion) {
		EntityWrapper<PromotionMemberRank> wrapper = new EntityWrapper<PromotionMemberRank>();
		wrapper.where("promotions_id = {0}", promotion.getId());
		promotionMemberRankDao.delete(wrapper);
		List<PromotionMemberRank> promotionMemberRanks = new ArrayList<PromotionMemberRank>();
		for (MemberRank memberRank : promotion.getMemberRanks()) {
			PromotionMemberRank promotionMemberRank = new PromotionMemberRank();
			promotionMemberRank.setMemberRanksId(memberRank.getId());
			promotionMemberRank.setPromotionsId(promotion.getId());
			promotionMemberRanks.add(promotionMemberRank);
		}
		if (CollectionUtils.isNotEmpty(promotionMemberRanks)) {
			promotionMemberRankDao.batchSave(promotionMemberRanks);
		}
	}

	/**
	 * 促销SKU中间表
	 * 
	 */
	private void setPromotionSku(Promotion promotion) {
		EntityWrapper<PromotionSku> wrapper = new EntityWrapper<PromotionSku>();
		wrapper.where("gift_promotions_id = {0}", promotion.getId());
		promotionSkuDao.delete(wrapper);
		List<PromotionSku> promotionSkus = new ArrayList<PromotionSku>();
		if (CollectionUtils.isNotEmpty(promotion.getGifts())) {
			for (Sku sku : promotion.getGifts()) {
				PromotionSku promotionSku = new PromotionSku();
				promotionSku.setGiftsId(sku.getId());
				promotionSku.setGiftPromotionsId(promotion.getId());
				promotionSkus.add(promotionSku);
			}
			if (CollectionUtils.isNotEmpty(promotionSkus)) {
				promotionSkuDao.batchSave(promotionSkus);
			}
		}
	}
	
}