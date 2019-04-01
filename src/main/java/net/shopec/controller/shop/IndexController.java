package net.shopec.controller.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller - 首页
 * 
 */
@Controller("shopIndexController")
@RequestMapping("/")
public class IndexController extends BaseController {

	/**
	 * 首页
	 */
	@GetMapping
	public String index(ModelMap model) {
		return "shop/index";
	}

}