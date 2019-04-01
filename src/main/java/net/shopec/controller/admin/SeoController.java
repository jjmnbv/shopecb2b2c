package net.shopec.controller.admin;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.shopec.Pageable;
import net.shopec.entity.Seo;
import net.shopec.service.SeoService;

/**
 * Controller - SEO设置
 * 
 */
@Controller("adminSeoController")
@RequestMapping("/admin/seo")
public class SeoController extends BaseController {

	@Inject
	private SeoService seoService;

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("seo", seoService.selectById(id));
		return "admin/seo/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public String update(Seo seo, RedirectAttributes redirectAttributes) {
		if (!isValid(seo)) {
			return ERROR_VIEW;
		}
		seoService.update(seo, "type");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", seoService.findPage(pageable));
		return "admin/seo/list";
	}

}