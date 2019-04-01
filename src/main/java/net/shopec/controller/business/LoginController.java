package net.shopec.controller.business;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.shopec.entity.Business;
import net.shopec.security.CurrentUser;
import net.shopec.util.WebUtils;

/**
 * Controller - 商家登录
 * 
 */
@Controller("businessLoginController")
@RequestMapping("/business/login")
public class LoginController extends BaseController {

	/**
	 * "重定向令牌"Cookie名称
	 */
	private static final String REDIRECT_TOKEN_COOKIE_NAME = "redirectToken";

	/**
	 * 登录页面
	 */
	@GetMapping
	public String index(String redirectUrl, String redirectToken, @CurrentUser Business currentUser, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		if (StringUtils.isNotEmpty(redirectUrl) && StringUtils.isNotEmpty(redirectToken) && StringUtils.equals(redirectToken, WebUtils.getCookie(request, REDIRECT_TOKEN_COOKIE_NAME))) {
			model.addAttribute("redirectUrl", redirectUrl);
			WebUtils.removeCookie(request, response, REDIRECT_TOKEN_COOKIE_NAME);
		}
		return currentUser != null ? "redirect:/business/index" : "business/login/index";
	}
}