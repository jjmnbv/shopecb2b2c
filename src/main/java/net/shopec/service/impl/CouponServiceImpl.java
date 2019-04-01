package net.shopec.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.CouponCodeDao;
import net.shopec.dao.CouponDao;
import net.shopec.dao.PromotionCouponDao;
import net.shopec.entity.Coupon;
import net.shopec.entity.CouponCode;
import net.shopec.entity.PromotionCoupon;
import net.shopec.entity.Store;
import net.shopec.service.CouponService;

/**
 * Service - 优惠券
 * 
 */
@Service
public class CouponServiceImpl extends BaseServiceImpl<Coupon> implements CouponService {

	/**
	 * 价格表达式变量
	 */
	private static final List<Map<String, Object>> PRICE_EXPRESSION_VARIABLES = new ArrayList<>();

	@Inject
	private CouponDao couponDao;
	@Inject
	private CouponCodeDao couponCodeDao;
	@Inject
	private PromotionCouponDao promotionCouponDao;

	static {
		Map<String, Object> variable0 = new HashMap<>();
		Map<String, Object> variable1 = new HashMap<>();
		Map<String, Object> variable2 = new HashMap<>();
		variable0.put("quantity", 99);
		variable0.put("price", new BigDecimal("99"));
		variable1.put("quantity", 99);
		variable1.put("price", new BigDecimal("9.9"));
		variable2.put("quantity", 99);
		variable2.put("price", new BigDecimal("0.99"));
		PRICE_EXPRESSION_VARIABLES.add(variable0);
		PRICE_EXPRESSION_VARIABLES.add(variable1);
		PRICE_EXPRESSION_VARIABLES.add(variable2);
	}

	@Transactional(readOnly = true)
	public boolean isValidPriceExpression(String priceExpression) {
		Assert.hasText(priceExpression, "HasText");

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
	public Coupon find(Long id) {
		return couponDao.find(id);
	}
	
	@Transactional(readOnly = true)
	public List<Coupon> findList(Store store) {
		return couponDao.findList(store, null, null, null);
	}

	@Transactional(readOnly = true)
	public List<Coupon> findList(Store store, Boolean isEnabled, Boolean isExchange, Boolean hasExpired) {
		return couponDao.findList(store, isEnabled, isExchange, hasExpired);
	}

	@Transactional(readOnly = true)
	public Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<Coupon> plusPage = getPlusPage(pageable);
		plusPage.setRecords(couponDao.selectPage(plusPage, getWrapper(pageable), isEnabled, isExchange, hasExpired));
		return super.findPage(plusPage, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Coupon> findPage(Store store, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<Coupon> plusPage = getPlusPage(pageable);
		plusPage.setRecords(couponDao.findPage(plusPage, getWrapper(pageable), store));
		return super.findPage(plusPage, pageable);
	}
	
	@Transactional(readOnly = true)
	public Page<Coupon> findPage(Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<Coupon> plusPage = getPlusPage(pageable);
		plusPage.setRecords(couponDao.findPage(plusPage, getWrapper(pageable), null));
		return super.findPage(plusPage, pageable);
	}

	@Override
	public Coupon save(Coupon coupon) {
		return super.save(coupon);
	}
	
	@Override
	public Coupon update(Coupon coupon) {
		return super.update(coupon);
	}
	
	@Override
	@Transactional
	public void delete(Coupon coupon) {
		if (coupon != null) {
			EntityWrapper<PromotionCoupon> wrapper = new EntityWrapper<PromotionCoupon>();
			wrapper.where("coupons_id = {0}", coupon.getId());
			promotionCouponDao.delete(wrapper);
			
			EntityWrapper<CouponCode> couponCodeWrapper = new EntityWrapper<CouponCode>();
			couponCodeWrapper.where("coupon_id = {0}", coupon.getId());
			couponCodeDao.delete(couponCodeWrapper);
			
			super.deleteById(coupon.getId());
		}
	}
	
}