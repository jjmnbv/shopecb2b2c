package net.shopec.controller.admin;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller - Js
 * 
 */
@Controller("adminJsController")
@RequestMapping("/resources/admin/js")
public class JsController {

	@Value("${javascript_content_type}")
	private String javascriptContentType;
	
	

	/**
	 * 共用
	 */
	@GetMapping("/common.js")
	public String common(HttpServletResponse response) {
		response.setContentType(javascriptContentType);
		return "admin/js/common";
	}

}