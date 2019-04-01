package net.shopec.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller - 首页
 * 
 */
@Controller("adminIndexController")
@RequestMapping("/admin/index")
public class IndexController {

	/**
	 * 首页
	 */
	@GetMapping
	public String index(ModelMap model) {
		return "admin/index";
	}

}