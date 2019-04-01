package net.shopec.controller.shop;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import net.shopec.entity.Promotion;
import net.shopec.exception.ResourceNotFoundException;
import net.shopec.service.PromotionService;

/**
 * Controller - 促销
 * 
 */
@Controller("shopPromotionController")
@RequestMapping("/promotion")
public class PromotionController extends BaseController {

	@Inject
	private PromotionService promotionService;

	/**
	 * 详情
	 */
	@GetMapping("/detail/{promotionId}")
	public String detail(@PathVariable Long promotionId, ModelMap model) {
		Promotion promotion = promotionService.find(promotionId);
		if (promotion == null) {
			throw new ResourceNotFoundException();
		}
		model.addAttribute("promotion", promotion);
		return "shop/promotion/detail";
	}

}