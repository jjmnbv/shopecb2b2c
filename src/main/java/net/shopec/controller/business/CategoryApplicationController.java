package net.shopec.controller.business;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.shopec.Pageable;
import net.shopec.entity.CategoryApplication;
import net.shopec.entity.ProductCategory;
import net.shopec.entity.Store;
import net.shopec.security.CurrentStore;
import net.shopec.service.CategoryApplicationService;
import net.shopec.service.ProductCategoryService;
import net.shopec.service.StoreService;

/**
 * Controller - 经营分类申请
 * 
 */
@Controller("businessCategoryApplicationController")
@RequestMapping("/business/category_application")
public class CategoryApplicationController extends BaseController {

	@Inject
	private CategoryApplicationService categoryApplicationService;
	@Inject
	private ProductCategoryService productCategoryService;
	@Inject
	private StoreService storeService;

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(@CurrentStore Store currentStore, ModelMap model) {
		if (currentStore == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}

		List<ProductCategory> productCategories = storeService.findProductCategoryList(currentStore, CategoryApplication.Status.pending);
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("appliedProductCategories", productCategories);
		return "business/category_application/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public String save(Long productCategoryId, @CurrentStore Store currentStore, RedirectAttributes redirectAttributes) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null || currentStore == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}
		if (storeService.productCategoryExists(currentStore, productCategory) || categoryApplicationService.exist(currentStore, productCategory, CategoryApplication.Status.pending)) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}

		CategoryApplication categoryApplication = new CategoryApplication();
		categoryApplication.setStatus(CategoryApplication.Status.pending);
		categoryApplication.setRate(Store.Type.general.equals(currentStore.getType()) ? productCategory.getGeneralRate() : productCategory.getSelfRate());
		categoryApplication.setStore(currentStore);
		categoryApplication.setProductCategory(productCategory);
		categoryApplicationService.save(categoryApplication);

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, @CurrentStore Store currentStore, ModelMap model) {
		model.addAttribute("page", categoryApplicationService.findPage(currentStore, pageable));
		return "business/category_application/list";
	}

}