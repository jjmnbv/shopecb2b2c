package net.shopec.controller.member;

import javax.inject.Inject;

import net.shopec.Pageable;
import net.shopec.Results;
import net.shopec.entity.BaseEntity;
import net.shopec.entity.Consultation;
import net.shopec.entity.Member;
import net.shopec.security.CurrentUser;
import net.shopec.service.ConsultationService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Controller - 咨询
 * 
 */
@Controller("memberConsultationController")
@RequestMapping("/member/consultation")
public class ConsultationController extends BaseController {

	/**
	 * 每页记录数
	 */
	private static final int PAGE_SIZE = 10;

	@Inject
	private ConsultationService consultationService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Integer pageNumber, @CurrentUser Member currentUser, ModelMap model) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", consultationService.findPage(currentUser, null, null, null, pageable));
		return "member/consultation/list";
	}

	/**
	 * 列表
	 */
	@GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(BaseEntity.BaseView.class)
	public ResponseEntity<?> list(Integer pageNumber, @CurrentUser Member currentUser) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		return ResponseEntity.ok(consultationService.findPage(currentUser, null, null, null, pageable).getContent());
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long id, @CurrentUser Member currentUser) {
		if (id == null) {
			return Results.NOT_FOUND;
		}
		Consultation consultation = consultationService.find(id);
		if (consultation == null || !currentUser.equals(consultation.getMember())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		consultationService.delete(id);
		return Results.OK;
	}

}