package net.shopec.controller.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.shopec.FileType;
import net.shopec.Pageable;
import net.shopec.Results;
import net.shopec.entity.Attribute;
import net.shopec.entity.BaseEntity;
import net.shopec.entity.Brand;
import net.shopec.entity.Parameter;
import net.shopec.entity.Product;
import net.shopec.entity.ProductCategory;
import net.shopec.entity.ProductImage;
import net.shopec.entity.ProductTag;
import net.shopec.entity.Promotion;
import net.shopec.entity.Sku;
import net.shopec.entity.Specification;
import net.shopec.entity.Store;
import net.shopec.entity.StoreProductCategory;
import net.shopec.entity.StoreProductTag;
import net.shopec.exception.UnauthorizedException;
import net.shopec.security.CurrentStore;
import net.shopec.service.AttributeService;
import net.shopec.service.BrandService;
import net.shopec.service.FileService;
import net.shopec.service.ParameterValueService;
import net.shopec.service.ProductCategoryService;
import net.shopec.service.ProductImageService;
import net.shopec.service.ProductService;
import net.shopec.service.ProductTagService;
import net.shopec.service.PromotionService;
import net.shopec.service.SkuService;
import net.shopec.service.SpecificationItemService;
import net.shopec.service.SpecificationService;
import net.shopec.service.StoreProductCategoryService;
import net.shopec.service.StoreProductTagService;
import net.shopec.service.StoreService;

/**
 * Controller - 商品
 * 
 */
@Controller("businessProductController")
@RequestMapping("/business/product")
public class ProductController extends BaseController {

	@Inject
	private ProductService productService;
	@Inject
	private SkuService skuService;
	@Inject
	private StoreService storeService;
	@Inject
	private ProductCategoryService productCategoryService;
	@Inject
	private StoreProductCategoryService storeProductCategoryService;
	@Inject
	private BrandService brandService;
	@Inject
	private PromotionService promotionService;
	@Inject
	private ProductTagService productTagService;
	@Inject
	private StoreProductTagService storeProductTagService;
	@Inject
	private ProductImageService productImageService;
	@Inject
	private ParameterValueService parameterValueService;
	@Inject
	private SpecificationItemService specificationItemService;
	@Inject
	private AttributeService attributeService;
	@Inject
	private SpecificationService specificationService;
	@Inject
	private FileService fileService;

	/**
	 * 添加属性
	 */
	@ModelAttribute
	public void populateModel(Long productId, Long productCategoryId, @CurrentStore Store currentStore, ModelMap model) {
		Product product = productService.find(productId);
		if (product != null && !currentStore.equals(product.getStore())) {
			throw new UnauthorizedException();
		}
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory != null && !storeService.productCategoryExists(currentStore, productCategory)) {
			throw new UnauthorizedException();
		}

		model.addAttribute("product", product);
		model.addAttribute("productCategory", productCategory);
	}

	/**
	 * 检查编号是否存在
	 */
	@GetMapping("/check_sn")
	public @ResponseBody boolean checkSn(String sn) {
		return StringUtils.isNotEmpty(sn) && !productService.snExists(sn);
	}

	/**
	 * 上传商品图片
	 */
	@PostMapping("/upload_product_image")
	public ResponseEntity<?> uploadProductImage(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (!fileService.isValid(FileType.image, file)) {
			return Results.unprocessableEntity("business.upload.invalid");
		}
		ProductImage productImage = productImageService.generate(file);
		if (productImage == null) {
			return Results.unprocessableEntity("business.upload.error");
		}

		return ResponseEntity.ok(productImage);
	}

	/**
	 * 删除商品图片
	 */
	@PostMapping("/delete_product_image")
	public ResponseEntity<?> deleteProductImage() {
		return Results.OK;
	}

	/**
	 * 获取参数
	 */
	@GetMapping("/parameters")
	public @ResponseBody List<Map<String, Object>> parameters(@ModelAttribute(binding = false) ProductCategory productCategory) {
		List<Map<String, Object>> data = new ArrayList<>();
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getParameters())) {
			return data;
		}
		for (Parameter parameter : productCategory.getParameters()) {
			Map<String, Object> item = new HashMap<>();
			item.put("group", parameter.getGroup());
			item.put("names", parameter.getNames());
			data.add(item);
		}
		return data;
	}

	/**
	 * 获取属性
	 */
	@GetMapping("/attributes")
	public @ResponseBody List<Map<String, Object>> attributes(@ModelAttribute(binding = false) ProductCategory productCategory) {
		List<Map<String, Object>> data = new ArrayList<>();
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getAttributes())) {
			return data;
		}
		for (Attribute attribute : productCategory.getAttributes()) {
			Map<String, Object> item = new HashMap<>();
			item.put("id", attribute.getId());
			item.put("name", attribute.getName());
			item.put("options", attribute.getOptions());
			data.add(item);
		}
		return data;
	}

	/**
	 * 获取规格
	 */
	@GetMapping("/specifications")
	public @ResponseBody List<Map<String, Object>> specifications(@ModelAttribute(binding = false) ProductCategory productCategory) {
		List<Map<String, Object>> data = new ArrayList<>();
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getSpecifications())) {
			return data;
		}
		for (Specification specification : productCategory.getSpecifications()) {
			Map<String, Object> item = new HashMap<>();
			item.put("name", specification.getName());
			item.put("options", specification.getOptions());
			data.add(item);
		}
		return data;
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(@CurrentStore Store currentStore, ModelMap model, RedirectAttributes redirectAttributes) {
		Long productCount = productService.count(null, currentStore, null, null, null, null, null, null);
		if (currentStore.getStoreRank() != null && currentStore.getStoreRank().getQuantity() != null && productCount >= currentStore.getStoreRank().getQuantity()) {
			addFlashMessage(redirectAttributes, "business.product.addCountNotAllowed", currentStore.getStoreRank().getQuantity());
			return "redirect:list";
		}

		model.addAttribute("types", Product.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("allowedProductCategories", productCategoryService.findList(currentStore, null, null, null));
		model.addAttribute("allowedProductCategoryParents", getAllowedProductCategoryParents(currentStore));
		model.addAttribute("storeProductCategoryTree", storeProductCategoryService.findTree(currentStore));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findList(currentStore, null, true));
		model.addAttribute("productTags", productTagService.findAll());
		model.addAttribute("storeProductTags", storeProductTagService.findList(currentStore, null));
		model.addAttribute("specifications", specificationService.findAll());
		return "business/product/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public String save(@ModelAttribute(name = "productForm") Product productForm, @ModelAttribute(binding = false) ProductCategory productCategory, SkuForm skuForm, SkuListForm skuListForm, Long brandId, Long[] promotionIds, Long[] productTagIds, Long[] storeProductTagIds,
			Long storeProductCategoryId, HttpServletRequest request, @CurrentStore Store currentStore, RedirectAttributes redirectAttributes) {
		productImageService.filter(productForm.getProductImages());
		parameterValueService.filter(productForm.getParameterValues());
		specificationItemService.filter(productForm.getSpecificationItems());
		skuService.filter(skuListForm.getSkuList());

		Long productCount = productService.count(null, currentStore, null, null, null, null, null, null);
		if (currentStore.getStoreRank() != null && currentStore.getStoreRank().getQuantity() != null && productCount >= currentStore.getStoreRank().getQuantity()) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}
		if (productCategory == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}
		if (storeProductCategoryId != null) {
			StoreProductCategory storeProductCategory = storeProductCategoryService.find(storeProductCategoryId);
			if (storeProductCategory == null || !currentStore.equals(storeProductCategory.getStore())) {
				return UNPROCESSABLE_ENTITY_VIEW;
			}
			productForm.setStoreProductCategory(storeProductCategory);
		}
		productForm.setStore(currentStore);
		productForm.setProductCategory(productCategory);
		productForm.setBrand(brandService.find(brandId));
		productForm.setPromotions(new HashSet<>(promotionService.findList(promotionIds)));
		productForm.setProductTags(new HashSet<>(productTagService.findList(productTagIds)));
		productForm.setStoreProductTags(new HashSet<>(storeProductTagService.findList(storeProductTagIds)));

		productForm.removeAttributeValue();
		for (Attribute attribute : productForm.getProductCategory().getAttributes()) {
			String value = request.getParameter("attribute_" + attribute.getId());
			String attributeValue = attributeService.toAttributeValue(attribute, value);
			productForm.setAttributeValue(attribute, attributeValue);
		}

		if (!isValid(productForm, BaseEntity.Save.class)) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}
		if (StringUtils.isNotEmpty(productForm.getSn()) && productService.snExists(productForm.getSn())) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}
		if (productForm.hasSpecification()) {
			List<Sku> skus = skuListForm.getSkuList();
			if (CollectionUtils.isEmpty(skus) || !isValid(skus, getValidationGroup(productForm.getType()), BaseEntity.Save.class)) {
				return UNPROCESSABLE_ENTITY_VIEW;
			}
			productService.create(productForm, skus);
		} else {
			Sku sku = skuForm.getSku();
			if (sku == null || !isValid(sku, getValidationGroup(productForm.getType()), BaseEntity.Save.class)) {
				return UNPROCESSABLE_ENTITY_VIEW;
			}
			productService.create(productForm, sku);
		}

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(@ModelAttribute(binding = false) Product product, @CurrentStore Store currentStore, ModelMap model) {
		if (product == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}

		model.addAttribute("types", Product.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("allowedProductCategories", productCategoryService.findList(currentStore, null, null, null));
		model.addAttribute("allowedProductCategoryParents", getAllowedProductCategoryParents(currentStore));
		model.addAttribute("storeProductCategoryTree", storeProductCategoryService.findTree(currentStore));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findList(currentStore, null, true));
		model.addAttribute("productTags", productTagService.findAll());
		model.addAttribute("storeProductTags", storeProductTagService.findList(currentStore, null));
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("product", product);
		return "business/product/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public String update(@ModelAttribute("productForm") Product productForm, @ModelAttribute(binding = false) Product product, @ModelAttribute(binding = false) ProductCategory productCategory, SkuForm skuForm, SkuListForm skuListForm, Long brandId, Long[] promotionIds, Long[] productTagIds,
			Long[] storeProductTagIds, Long storeProductCategoryId, HttpServletRequest request, @CurrentStore Store currentStore, RedirectAttributes redirectAttributes) {
		productImageService.filter(productForm.getProductImages());
		parameterValueService.filter(productForm.getParameterValues());
		specificationItemService.filter(productForm.getSpecificationItems());
		skuService.filter(skuListForm.getSkuList());
		if (product == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}
		if (productCategory == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}
		List<Promotion> promotions = promotionService.findList(promotionIds);
		if (CollectionUtils.isNotEmpty(promotions)) {
			if (currentStore.getPromotions() == null || !currentStore.getPromotions().containsAll(promotions)) {
				return UNPROCESSABLE_ENTITY_VIEW;
			}
		}
		if (storeProductCategoryId != null) {
			StoreProductCategory storeProductCategory = storeProductCategoryService.find(storeProductCategoryId);
			if (storeProductCategory == null || !currentStore.equals(storeProductCategory.getStore())) {
				return UNPROCESSABLE_ENTITY_VIEW;
			}
			productForm.setStoreProductCategory(storeProductCategory);
		}
		productForm.setId(product.getId());
		productForm.setType(product.getType());
		productForm.setIsActive(true);
		productForm.setProductCategory(productCategory);
		productForm.setBrand(brandService.find(brandId));
		productForm.setPromotions(new HashSet<>(promotions));
		productForm.setProductTags(new HashSet<>(productTagService.findList(productTagIds)));
		productForm.setStoreProductTags(new HashSet<>(storeProductTagService.findList(storeProductTagIds)));

		productForm.removeAttributeValue();
		for (Attribute attribute : productForm.getProductCategory().getAttributes()) {
			String value = request.getParameter("attribute_" + attribute.getId());
			String attributeValue = attributeService.toAttributeValue(attribute, value);
			productForm.setAttributeValue(attribute, attributeValue);
		}

		if (!isValid(productForm, BaseEntity.Update.class)) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}

		if (productForm.hasSpecification()) {
			List<Sku> skus = skuListForm.getSkuList();
			if (CollectionUtils.isEmpty(skus) || !isValid(skus, getValidationGroup(productForm.getType()), BaseEntity.Update.class)) {
				return UNPROCESSABLE_ENTITY_VIEW;
			}
			productService.modify(productForm, skus);
		} else {
			Sku sku = skuForm.getSku();
			if (sku == null || !isValid(sku, getValidationGroup(productForm.getType()), BaseEntity.Update.class)) {
				return UNPROCESSABLE_ENTITY_VIEW;
			}
			productService.modify(productForm, sku);
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(@ModelAttribute(binding = false) ProductCategory productCategory, Product.Type type, Long brandId, Long promotionId, Long productTagId, Long storeProductTagId, Boolean isActive, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
			Pageable pageable, @CurrentStore Store currentStore, ModelMap model) {
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		ProductTag productTag = productTagService.find(productTagId);
		StoreProductTag storeProductTag = storeProductTagService.find(storeProductTagId);
		if (promotion != null) {
			if (!currentStore.equals(promotion.getStore())) {
				return UNPROCESSABLE_ENTITY_VIEW;
			}
		}
		if (storeProductTag != null) {
			if (!currentStore.equals(storeProductTag.getStore())) {
				return UNPROCESSABLE_ENTITY_VIEW;
			}
		}

		model.addAttribute("types", Product.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("allowedProductCategories", productCategoryService.findList(currentStore, null, null, null));
		model.addAttribute("allowedProductCategoryParents", getAllowedProductCategoryParents(currentStore));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findList(currentStore, null, true));
		model.addAttribute("productTags", productTagService.findAll());
		model.addAttribute("storeProductTags", storeProductTagService.findList(currentStore, true));
		model.addAttribute("type", type);
		model.addAttribute("productCategoryId", productCategory != null ? productCategory.getId() : null);
		model.addAttribute("brandId", brandId);
		model.addAttribute("promotionId", promotionId);
		model.addAttribute("productTagId", productTagId);
		model.addAttribute("storeProductTagId", storeProductTagId);
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isList", isList);
		model.addAttribute("isTop", isTop);
		model.addAttribute("isActive", isActive);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("isStockAlert", isStockAlert);
		model.addAttribute("page", productService.findPage(type, currentStore, productCategory, null, brand, promotion, productTag, storeProductTag, null, null, null, isMarketable, isList, isTop, isActive, isOutOfStock, isStockAlert, null, null, pageable));
		return "business/product/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids, @CurrentStore Store currentStore) {
		for (Long id : ids) {
			Product product = productService.find(id);
			if (product == null) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			if (!currentStore.equals(product.getStore())) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			productService.delete(product);
		}
		return Results.OK;
	}

	/**
	 * 上架商品
	 */
	@PostMapping("/shelves")
	public ResponseEntity<?> shelves(Long[] ids, @CurrentStore Store currentStore) {
		if (ids == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		for (Long id : ids) {
			Product product = productService.find(id);
			if (product == null) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			if (!currentStore.equals(product.getStore())) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			if (!currentStore.getProductCategories().contains(product.getProductCategory())) {
				return Results.unprocessableEntity("business.product.isNotMarketable");
			}
			if (!product.getIsMarketable()) {
				product.setIsMarketable(true);
				productService.update(product);
			}
		}
		return Results.OK;
	}

	/**
	 * 下架商品
	 */
	@PostMapping("/shelf")
	public ResponseEntity<?> shelf(Long[] ids, @CurrentStore Store currentStore) {
		if (ids == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		for (Long id : ids) {
			Product product = productService.find(id);
			if (product == null) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			if (!currentStore.equals(product.getStore())) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			if (product.getIsMarketable()) {
				product.setIsMarketable(false);
				productService.update(product);
			}
		}
		return Results.OK;
	}

	/**
	 * 获取允许发布商品分类上级分类
	 * 
	 * @param store
	 *            店铺
	 * @return 允许发布商品分类上级分类
	 */
	private Set<ProductCategory> getAllowedProductCategoryParents(Store store) {
		Assert.notNull(store, "notNull");

		Set<ProductCategory> result = new HashSet<>();
		List<ProductCategory> allowedProductCategories = productCategoryService.findList(store, null, null, null);
		for (ProductCategory allowedProductCategory : allowedProductCategories) {
			result.addAll(allowedProductCategory.getParents());
		}
		return result;
	}

	/**
	 * 根据类型获取验证组
	 * 
	 * @param type
	 *            类型
	 * @return 验证组
	 */
	private Class<?> getValidationGroup(Product.Type type) {
		Assert.notNull(type, "notNull");

		switch (type) {
		case general:
			return Sku.General.class;
		case exchange:
			return Sku.Exchange.class;
		case gift:
			return Sku.Gift.class;
		}
		return null;
	}

	/**
	 * FormBean - SKU
	 * 
	 */
	public static class SkuForm {

		/**
		 * SKU
		 */
		private Sku sku;

		/**
		 * 获取SKU
		 * 
		 * @return SKU
		 */
		public Sku getSku() {
			return sku;
		}

		/**
		 * 设置SKU
		 * 
		 * @param sku
		 *            SKU
		 */
		public void setSku(Sku sku) {
			this.sku = sku;
		}

	}

	/**
	 * FormBean - SKU
	 * 
	 */
	public static class SkuListForm {

		/**
		 * SKU
		 */
		private List<Sku> skuList;

		/**
		 * 获取SKU
		 * 
		 * @return SKU
		 */
		public List<Sku> getSkuList() {
			return skuList;
		}

		/**
		 * 设置SKU
		 * 
		 * @param skuList
		 *            SKU
		 */
		public void setSkuList(List<Sku> skuList) {
			this.skuList = skuList;
		}

	}

}