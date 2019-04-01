package net.shopec.controller.member;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonView;

import net.shopec.Pageable;
import net.shopec.Results;
import net.shopec.entity.BaseEntity;
import net.shopec.entity.Member;
import net.shopec.entity.Review;
import net.shopec.security.CurrentUser;
import net.shopec.service.ReviewService;

/**
 * Controller - 评论
 * 
 */
@Controller("memberReviewController")
@RequestMapping("/member/review")
public class ReviewController extends BaseController {

	/**
	 * 每页记录数
	 */
	private static final int PAGE_SIZE = 10;

	@Inject
	private ReviewService reviewService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Integer pageNumber, @CurrentUser Member currentUser, ModelMap model) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", reviewService.findPage(currentUser, null, null, null, null, pageable));
		return "member/review/list";
	}

	/**
	 * 列表
	 */
	@GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(BaseEntity.BaseView.class)
	public ResponseEntity<?> list(Integer pageNumber, @CurrentUser Member currentUser) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		return ResponseEntity.ok(reviewService.findPage(currentUser, null, null, null, null, pageable).getContent());
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long id, @CurrentUser Member currentUser) {
		if (id == null) {
			return Results.NOT_FOUND;
		}
		Review review = reviewService.find(id);
		if (review == null || !currentUser.equals(review.getMember())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		reviewService.delete(review);
		return Results.OK;
	}

}