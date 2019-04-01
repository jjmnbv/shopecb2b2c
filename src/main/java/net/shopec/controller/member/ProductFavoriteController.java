package net.shopec.controller.member;

import javax.inject.Inject;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonView;

import net.shopec.Pageable;
import net.shopec.Results;
import net.shopec.entity.BaseEntity;
import net.shopec.entity.Member;
import net.shopec.entity.Product;
import net.shopec.entity.ProductFavorite;
import net.shopec.exception.UnauthorizedException;
import net.shopec.security.CurrentUser;
import net.shopec.service.ProductFavoriteService;
import net.shopec.service.ProductService;

/**
 * Controller - 商品收藏
 * 
 */
@Controller("memberProductFavoriteController")
@RequestMapping("/member/product_favorite")
public class ProductFavoriteController extends BaseController {

	/**
	 * 每页记录数
	 */
	private static final int PAGE_SIZE = 10;

	@Inject
	private ProductFavoriteService productFavoriteService;
	@Inject
	private ProductService productService;

	/**
	 * 添加属性
	 */
	@ModelAttribute
	public void populateModel(Long productId, Long productFavoriteId, @CurrentUser Member currentUser, ModelMap model) {
		model.addAttribute("product", productService.find(productId));

		ProductFavorite productFavorite = productFavoriteService.find(productFavoriteId);
		if (productFavorite != null && !currentUser.equals(productFavorite.getMember())) {
			throw new UnauthorizedException();
		}
		model.addAttribute("productFavorite", productFavorite);
	}

	/**
	 * 添加
	 */
	@PostMapping("/add")
	public ResponseEntity<?> add(@ModelAttribute(binding = false) Product product, @CurrentUser Member currentUser) {
		if (product == null || BooleanUtils.isNotTrue(product.getIsActive())) {
			return Results.NOT_FOUND;
		}
		if (productFavoriteService.exists(currentUser, product)) {
			return Results.unprocessableEntity("member.productFavorite.exist");
		}
		if (BooleanUtils.isNotTrue(product.getIsMarketable())) {
			return Results.unprocessableEntity("member.productFavorite.notMarketable");
		}
		if (ProductFavorite.MAX_PRODUCT_FAVORITE_SIZE != null && productFavoriteService.count(currentUser) >= ProductFavorite.MAX_PRODUCT_FAVORITE_SIZE) {
			return Results.unprocessableEntity("member.productFavorite.addCountNotAllowed", ProductFavorite.MAX_PRODUCT_FAVORITE_SIZE);
		}
		ProductFavorite productFavorite = new ProductFavorite();
		productFavorite.setMember(currentUser);
		productFavorite.setProduct(product);
		productFavoriteService.save(productFavorite);
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Integer pageNumber, @CurrentUser Member currentUser, ModelMap model) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", productFavoriteService.findPage(currentUser, pageable));
		return "member/product_favorite/list";
	}

	/**
	 * 列表
	 */
	@GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(BaseEntity.BaseView.class)
	public ResponseEntity<?> list(Integer pageNumber, @CurrentUser Member currentUser) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		return ResponseEntity.ok(productFavoriteService.findPage(currentUser, pageable).getContent());
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(@ModelAttribute(binding = false) ProductFavorite productFavorite) {
		if (productFavorite == null) {
			return Results.NOT_FOUND;
		}

		productFavoriteService.delete(productFavorite);
		return Results.OK;
	}

}