package net.shopec.controller.admin;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.shopec.Message;
import net.shopec.Pageable;
import net.shopec.service.OrderShippingService;

/**
 * Controller - 订单发货
 * 
 */
@Controller("adminOrderShippingController")
@RequestMapping("/admin/shipping")
public class OrderShippingController extends BaseController {

	@Inject
	private OrderShippingService orderShippingService;

	/**
	 * 查看
	 */
	@GetMapping("/view")
	public String view(Long id, ModelMap model) {
		model.addAttribute("shipping", orderShippingService.find(id));
		return "admin/order_shipping/view";
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", orderShippingService.findPage(pageable));
		return "admin/order_shipping/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public @ResponseBody Message delete(Long[] ids) {
		orderShippingService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}