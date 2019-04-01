package net.shopec.controller.shop;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonView;

import net.shopec.Pageable;
import net.shopec.Results;
import net.shopec.Setting;
import net.shopec.entity.BaseEntity;
import net.shopec.entity.Member;
import net.shopec.entity.Product;
import net.shopec.entity.Review;
import net.shopec.exception.ResourceNotFoundException;
import net.shopec.security.CurrentUser;
import net.shopec.service.ProductService;
import net.shopec.service.ReviewService;
import net.shopec.util.SystemUtils;

/**
 * Controller - 评论
 * 
 */
@Controller("shopReviewController")
@RequestMapping("/review")
public class ReviewController extends BaseController {

	/**
	 * 每页记录数
	 */
	private static final int PAGE_SIZE = 10;

	@Inject
	private ReviewService reviewService;
	@Inject
	private ProductService productService;

	/**
	 * 列表
	 */
	@GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(BaseEntity.BaseView.class)
	public ResponseEntity<?> list(Long productId, Integer pageNumber) {
		Product product = productService.find(productId);
		if (product == null || BooleanUtils.isNotTrue(product.getIsActive()) || BooleanUtils.isNotTrue(product.getIsMarketable())) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		return ResponseEntity.ok(reviewService.findPage(null, product, null, null, true, pageable).getContent());
	}

	/**
	 * 发表
	 */
	@GetMapping("/add/{productId}")
	public String add(@PathVariable Long productId, ModelMap model) {
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsReviewEnabled()) {
			throw new ResourceNotFoundException();
		}
		Product product = productService.find(productId);
		if (product == null || BooleanUtils.isNotTrue(product.getIsActive()) || BooleanUtils.isNotTrue(product.getIsMarketable())) {
			throw new ResourceNotFoundException();
		}

		model.addAttribute("product", product);
		return "shop/review/add";
	}

	/**
	 * 详情
	 */
	@GetMapping("/detail/{productId}")
	public String detail(@PathVariable Long productId, Review.Type type, Integer pageNumber, ModelMap model) {
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsReviewEnabled()) {
			throw new ResourceNotFoundException();
		}
		Product product = productService.find(productId);
		if (product == null || BooleanUtils.isNotTrue(product.getIsActive()) || BooleanUtils.isNotTrue(product.getIsMarketable())) {
			throw new ResourceNotFoundException();
		}

		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("type", type);
		model.addAttribute("types", Review.Type.values());
		model.addAttribute("product", product);
		model.addAttribute("page", reviewService.findPage(null, product, product.getStore(), type, true, pageable));
		return "shop/review/detail";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(Long productId, Integer score, String content, @CurrentUser Member currentUser, HttpServletRequest request) {
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsReviewEnabled()) {
			return Results.unprocessableEntity("shop.review.disabled");
		}
		Product product = productService.find(productId);
		if (product == null || BooleanUtils.isNotTrue(product.getIsActive()) || BooleanUtils.isNotTrue(product.getIsMarketable())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (!isValid(Review.class, "score", score) || !isValid(Review.class, "content", content)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (currentUser != null && !reviewService.hasPermission(currentUser, product)) {
			return Results.unprocessableEntity("shop.review.noPermission");
		}

		Review review = new Review();
		review.setId(IdWorker.getId());
		review.setScore(score);
		review.setContent(content);
		review.setIp(request.getRemoteAddr());
		review.setMember(currentUser);
		review.setProduct(product);
		review.setStore(product.getStore());
		review.setReplyReviews(null);
		review.setForReview(null);
		if (setting.getIsReviewCheck()) {
			review.setIsShow(false);
			reviewService.save(review);
			return Results.ok("shop.review.check");
		} else {
			review.setIsShow(true);
			reviewService.save(review);
			return Results.ok("shop.review.success");
		}
	}

}