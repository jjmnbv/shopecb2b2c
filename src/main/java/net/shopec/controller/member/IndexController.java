package net.shopec.controller.member;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.shopec.controller.shop.BaseController;
import net.shopec.entity.Member;
import net.shopec.entity.Order;
import net.shopec.security.CurrentUser;
import net.shopec.service.ConsultationService;
import net.shopec.service.CouponCodeService;
import net.shopec.service.MessageService;
import net.shopec.service.OrderService;
import net.shopec.service.ProductFavoriteService;
import net.shopec.service.ProductNotifyService;
import net.shopec.service.ReviewService;

/**
 * Controller - 首页
 * 
 */
@Controller("memberIndexController")
@RequestMapping("/member/index")
public class IndexController extends BaseController {

	/**
	 * 最新订单数量
	 */
	private static final int NEW_ORDER_SIZE = 3;

	@Inject
	private OrderService orderService;
	@Inject
	private CouponCodeService couponCodeService;
	@Inject
	private MessageService messageService;
	@Inject
	private ProductFavoriteService productFavoriteService;
	@Inject
	private ProductNotifyService productNotifyService;
	@Inject
	private ReviewService reviewService;
	@Inject
	private ConsultationService consultationService;

	/**
	 * 首页
	 */
	@GetMapping
	public String index(@CurrentUser Member currentUser, ModelMap model) {
		model.addAttribute("pendingPaymentOrderCount", orderService.count(null, Order.Status.pendingPayment, null, currentUser, null, null, null, null, null, null, false));
		model.addAttribute("pendingShipmentOrderCount", orderService.count(null, Order.Status.pendingShipment, null, currentUser, null, null, null, null, null, null, null));
		model.addAttribute("shippedOrderCount", orderService.count(null, Order.Status.shipped, null, currentUser, null, null, null, null, null, null, null));
		model.addAttribute("messageCount", messageService.count(currentUser, false));
		model.addAttribute("couponCodeCount", couponCodeService.count(null, currentUser, null, false, false));
		model.addAttribute("productFavoriteCount", productFavoriteService.count(currentUser));
		model.addAttribute("productNotifyCount", productNotifyService.count(currentUser, null, null, null));
		model.addAttribute("reviewCount", reviewService.count(currentUser, null, null, null));
		model.addAttribute("consultationCount", consultationService.count(currentUser, null, null));
		model.addAttribute("newOrders", orderService.findList(null, null, null, currentUser, null, null, null, null, null, null, null, NEW_ORDER_SIZE, null, null));
		return "member/index";
	}

}