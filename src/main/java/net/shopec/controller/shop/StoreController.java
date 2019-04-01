package net.shopec.controller.shop;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import net.shopec.entity.Store;
import net.shopec.exception.ResourceNotFoundException;
import net.shopec.service.StoreProductCategoryService;
import net.shopec.service.StoreProductTagService;
import net.shopec.service.StoreService;

/**
 * Controller - 店铺
 * 
 */
@Controller("shopStoreController")
@RequestMapping("/store")
public class StoreController extends BaseController {

	@Inject
	private StoreService storeService;
	@Inject
	private StoreProductCategoryService storeProductCategoryService;
	@Inject
	private StoreProductTagService storeProductTagService;

	/**
	 * 首页
	 */
	@GetMapping("/{storeId}")
	public String index(@PathVariable Long storeId, ModelMap model) {
		Store store = storeService.find(storeId);
		if (store == null) {
			throw new ResourceNotFoundException();
		}
		model.addAttribute("store", store);
		model.addAttribute("storeProductCategoryTree", storeProductCategoryService.findTree(store));
		model.addAttribute("storeProductTags", storeProductTagService.findList(store, true));
		return "shop/store/index";
	}

}