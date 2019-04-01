package net.shopec.controller.admin;

import java.util.HashSet;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.shopec.Message;
import net.shopec.Pageable;
import net.shopec.entity.ShippingMethod;
import net.shopec.service.DeliveryCorpService;
import net.shopec.service.PaymentMethodService;
import net.shopec.service.ShippingMethodService;

/**
 * Controller - 配送方式
 * 
 */
@Controller("adminShippingMethodController")
@RequestMapping("/admin/shipping_method")
public class ShippingMethodController extends BaseController {

	@Inject
	private ShippingMethodService shippingMethodService;
	@Inject
	private PaymentMethodService paymentMethodService;
	@Inject
	private DeliveryCorpService deliveryCorpService;

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		return "admin/shipping_method/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public String save(ShippingMethod shippingMethod, Long defaultDeliveryCorpId, Long[] paymentMethodIds, RedirectAttributes redirectAttributes) {
		shippingMethod.setDefaultDeliveryCorp(deliveryCorpService.find(defaultDeliveryCorpId));
		shippingMethod.setPaymentMethods(new HashSet<>(paymentMethodService.findList(paymentMethodIds)));
		if (!isValid(shippingMethod)) {
			return ERROR_VIEW;
		}
		shippingMethod.setAreaFreightConfigs(null);
		shippingMethod.setOrders(null);
		shippingMethodService.save(shippingMethod);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethod", shippingMethodService.find(id));
		return "admin/shipping_method/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public String update(ShippingMethod shippingMethod, Long defaultDeliveryCorpId, Long[] paymentMethodIds, RedirectAttributes redirectAttributes) {
		shippingMethod.setDefaultDeliveryCorp(deliveryCorpService.find(defaultDeliveryCorpId));
		shippingMethod.setPaymentMethods(new HashSet<>(paymentMethodService.findList(paymentMethodIds)));
		if (!isValid(shippingMethod)) {
			return ERROR_VIEW;
		}
		shippingMethodService.update(shippingMethod, "areaFreightConfigs", "version", "orders");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", shippingMethodService.findPage(pageable));
		return "admin/shipping_method/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public @ResponseBody Message delete(Long[] ids) {
		if (ids.length >= shippingMethodService.count()) {
			return Message.error("admin.common.deleteAllNotAllowed");
		}
		shippingMethodService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}