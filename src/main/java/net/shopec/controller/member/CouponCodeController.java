package net.shopec.controller.member;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonView;

import net.shopec.Pageable;
import net.shopec.Results;
import net.shopec.entity.BaseEntity;
import net.shopec.entity.Coupon;
import net.shopec.entity.Member;
import net.shopec.security.CurrentUser;
import net.shopec.service.CouponCodeService;
import net.shopec.service.CouponService;

/**
 * Controller - 优惠码
 * 
 */
@Controller("memberCouponCodeController")
@RequestMapping("/member/coupon_code")
public class CouponCodeController extends BaseController {

	/**
	 * 每页记录数
	 */
	private static final int PAGE_SIZE = 10;

	@Inject
	private CouponService couponService;
	@Inject
	private CouponCodeService couponCodeService;

	/**
	 * 添加属性
	 */
	@ModelAttribute
	public void populateModel(Long couponId, ModelMap model) {
		model.addAttribute("coupon", couponService.find(couponId));
	}

	/**
	 * 兑换
	 */
	@GetMapping("/exchange")
	public String exchange(Integer pageNumber, ModelMap model) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", couponService.findPage(true, true, false, pageable));
		return "member/coupon_code/exchange";
	}

	/**
	 * 兑换
	 */
	@PostMapping("/exchange")
	public ResponseEntity<?> exchange(@ModelAttribute(binding = false) Coupon coupon, @CurrentUser Member currentUser) {
		if (coupon == null) {
			return Results.NOT_FOUND;
		}

		if (!coupon.getIsEnabled() || !coupon.getIsExchange() || coupon.hasExpired()) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (currentUser.getPoint() < coupon.getPoint()) {
			return Results.unprocessableEntity("member.couponCode.point");
		}
		couponCodeService.exchange(coupon, currentUser);
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Integer pageNumber, @CurrentUser Member currentUser, ModelMap model) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", couponCodeService.findPage(currentUser, pageable));
		return "member/coupon_code/list";
	}

	/**
	 * 列表
	 */
	@GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(BaseEntity.BaseView.class)
	public ResponseEntity<?> list(Integer pageNumber, @CurrentUser Member currentUser) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		return ResponseEntity.ok(couponCodeService.findPage(currentUser, pageable).getContent());
	}

}