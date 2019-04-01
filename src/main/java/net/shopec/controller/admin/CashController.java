package net.shopec.controller.admin;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.shopec.Pageable;
import net.shopec.entity.Admin;
import net.shopec.entity.Cash;
import net.shopec.security.CurrentUser;
import net.shopec.service.CashService;

/**
 * Controller - 提现
 * 
 */
@Controller("adminCashController")
@RequestMapping("/admin/cash")
public class CashController extends BaseController {

	@Inject
	private CashService cashService;

	/**
	 * 审核
	 */
	@PostMapping("/review")
	public String review(Long id, Boolean isPassed, @CurrentUser Admin currentUser, RedirectAttributes redirectAttributes) {
		Cash cash = cashService.find(id);
		if (isPassed == null || cash == null || !Cash.Status.pending.equals(cash.getStatus())) {
			return ERROR_VIEW;
		}
		cashService.review(cash, isPassed);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", cashService.findPage(pageable));
		return "admin/cash/list";
	}

}