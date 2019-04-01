package net.shopec.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.CouponCodeDao;
import net.shopec.entity.Coupon;
import net.shopec.entity.CouponCode;
import net.shopec.entity.Member;
import net.shopec.entity.PointLog;
import net.shopec.service.CouponCodeService;
import net.shopec.service.MemberService;

/**
 * Service - 优惠码
 * 
 */
@Service
public class CouponCodeServiceImpl extends BaseServiceImpl<CouponCode> implements CouponCodeService {

	@Inject
	private CouponCodeDao couponCodeDao;
	@Inject
	private MemberService memberService;

	@Transactional(readOnly = true)
	public boolean codeExists(String code) {
		return couponCodeDao.exists("code", StringUtils.lowerCase(code));
	}

	@Transactional(readOnly = true)
	public CouponCode findByCode(String code) {
		return couponCodeDao.findByAttribute("code", StringUtils.lowerCase(code));
	}

	public CouponCode generate(Coupon coupon, Member member) {
		Assert.notNull(coupon, "NotNull");

		CouponCode couponCode = new CouponCode();
		couponCode.setCode(coupon.getPrefix() + DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)).toUpperCase());
		couponCode.setIsUsed(false);
		couponCode.setCoupon(coupon);
		couponCode.setMember(member);
		super.save(couponCode);
		return couponCode;
	}

	public List<CouponCode> generate(Coupon coupon, Member member, Integer count) {
		Assert.notNull(coupon, "NotNull");
		Assert.notNull(count, "NotNull");

		List<CouponCode> couponCodes = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			CouponCode couponCode = generate(coupon, member);
			couponCodes.add(couponCode);
			if (i % 50 == 0) {
//				couponCodeDao.flush();
//				couponCodeDao.clear();
			}
		}
		return couponCodes;
	}

	public CouponCode exchange(Coupon coupon, Member member) {
		Assert.notNull(coupon, "NotNull");
		Assert.notNull(coupon.getPoint(), "NotNull");
		Assert.state(coupon.getIsEnabled() && coupon.getIsExchange() && !coupon.hasExpired(), "NotNull");
		Assert.notNull(member, "NotNull");
		Assert.notNull(member.getPoint(), "NotNull");
		Assert.state(member.getPoint() >= coupon.getPoint(), "NotNull");

		if (coupon.getPoint() > 0) {
			memberService.addPoint(member, -coupon.getPoint(), PointLog.Type.exchange, null);
		}

		return generate(coupon, member);
	}

	@Transactional(readOnly = true)
	public Page<CouponCode> findPage(Member member, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<CouponCode> plusPage = getPlusPage(pageable);
		plusPage.setRecords(couponCodeDao.findPage(plusPage, getWrapper(pageable), member));
		return super.findPage(plusPage, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed) {
		return couponCodeDao.count(coupon, member, hasBegun, hasExpired, isUsed);
	}

}