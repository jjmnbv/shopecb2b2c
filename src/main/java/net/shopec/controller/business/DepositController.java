package net.shopec.controller.business;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.shopec.Pageable;
import net.shopec.Results;
import net.shopec.entity.Business;
import net.shopec.plugin.PaymentPlugin;
import net.shopec.security.CurrentUser;
import net.shopec.service.BusinessDepositLogService;
import net.shopec.service.PluginService;
import net.shopec.util.WebUtils;

/**
 * Controller - 预存款
 * 
 */
@Controller("businessDepositController")
@RequestMapping("/business/deposit")
public class DepositController extends BaseController {

	@Inject
	private BusinessDepositLogService businessDepositLogService;
	@Inject
	private PluginService pluginService;

	/**
	 * 计算
	 */
	@GetMapping("/calculate")
	public ResponseEntity<?> calculate(String paymentPluginId, BigDecimal rechargeAmount) {
		Map<String, Object> data = new HashMap<>();
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (rechargeAmount == null || rechargeAmount.compareTo(BigDecimal.ZERO) <= 0) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		data.put("fee", paymentPlugin.calculateFee(rechargeAmount));
		return ResponseEntity.ok(data);
	}

	/**
	 * 检查余额
	 */
	@PostMapping("/check_balance")
	public @ResponseBody Map<String, Object> checkBalance(@CurrentUser Business currenUser) {
		Map<String, Object> data = new HashMap<>();
		data.put("balance", currenUser.getBalance());
		return data;
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
		return "business/deposit/recharge";
	}

	/**
	 * 记录
	 */
	@GetMapping("/log")
	public String log(Pageable pageable, @CurrentUser Business currenUser, ModelMap model) {
		model.addAttribute("page", businessDepositLogService.findPage(currenUser, pageable));
		return "business/deposit/log";
	}

}