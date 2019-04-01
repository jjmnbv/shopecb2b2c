package net.shopec.controller.member;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonView;

import net.shopec.Pageable;
import net.shopec.Results;
import net.shopec.entity.BaseEntity;
import net.shopec.entity.Member;
import net.shopec.plugin.PaymentPlugin;
import net.shopec.security.CurrentUser;
import net.shopec.service.MemberDepositLogService;
import net.shopec.service.PluginService;
import net.shopec.util.WebUtils;

/**
 * Controller - 预存款
 * 
 */
@Controller("memberDepositController")
@RequestMapping("/member/deposit")
public class DepositController extends BaseController {

	/**
	 * 每页记录数
	 */
	private static final int PAGE_SIZE = 10;

	@Inject
	private MemberDepositLogService memberDepositLogService;
	@Inject
	private PluginService pluginService;

	/**
	 * 计算支付手续费
	 */
	@PostMapping("/calculate_fee")
	public ResponseEntity<?> calculateFee(String paymentPluginId, BigDecimal rechargeAmount) {
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null) {
			return Results.NOT_FOUND;
		}
		if (!paymentPlugin.getIsEnabled() || rechargeAmount == null || rechargeAmount.compareTo(BigDecimal.ZERO) < 0) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		Map<String, Object> data = new HashMap<>();
		data.put("fee", paymentPlugin.calculateFee(rechargeAmount));
		return ResponseEntity.ok(data);
	}

	/**
	 * 检查余额
	 */
	@PostMapping("/check_balance")
	public ResponseEntity<?> checkBalance(@CurrentUser Member currentUser) {
		Map<String, Object> data = new HashMap<>();
		data.put("balance", currentUser.getBalance());
		return ResponseEntity.ok(data);
	}

	/**
	 * 充值
	 */
	@GetMapping("/recharge")
	public String recharge(ModelMap model) {
		List<PaymentPlugin> paymentPlugins = pluginService.getActivePaymentPlugins(WebUtils.getRequest());
		if (!paymentPlugins.isEmpty()) {
			model.addAttribute("defaultPaymentPlugin", paymentPlugins.get(0));
			model.addAttribute("paymentPlugins", paymentPlugins);
		}
		return "member/deposit/recharge";
	}

	/**
	 * 记录
	 */
	@GetMapping("/log")
	public String log(Integer pageNumber, @CurrentUser Member currentUser, ModelMap model) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", memberDepositLogService.findPage(currentUser, pageable));
		return "member/deposit/log";
	}

	/**
	 * 记录
	 */
	@GetMapping(path = "/log", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(BaseEntity.BaseView.class)
	public ResponseEntity<?> log(Integer pageNumber, @CurrentUser Member currentUser) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		return ResponseEntity.ok(memberDepositLogService.findPage(currentUser, pageable).getContent());
	}

}