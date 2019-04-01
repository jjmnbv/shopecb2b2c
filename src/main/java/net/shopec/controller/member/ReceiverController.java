package net.shopec.controller.member;

import javax.inject.Inject;

import net.shopec.Pageable;
import net.shopec.Results;
import net.shopec.entity.Area;
import net.shopec.entity.Member;
import net.shopec.entity.Receiver;
import net.shopec.exception.UnauthorizedException;
import net.shopec.security.CurrentUser;
import net.shopec.service.AreaService;
import net.shopec.service.ReceiverService;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 收货地址
 * 
 */
@Controller("memberReceiverController")
@RequestMapping("/member/receiver")
public class ReceiverController extends BaseController {

	/**
	 * 每页记录数
	 */
	private static final int PAGE_SIZE = 10;

	@Inject
	private AreaService areaService;
	@Inject
	private ReceiverService receiverService;

	/**
	 * 添加属性
	 */
	@ModelAttribute
	public void populateModel(Long receiverId, @CurrentUser Member currentUser, ModelMap model) {
		Receiver receiver = receiverService.find(receiverId);
		if (receiver != null && !currentUser.equals(receiver.getMember())) {
			throw new UnauthorizedException();
		}
		model.addAttribute("receiver", receiver);
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Integer pageNumber, @CurrentUser Member currentUser, ModelMap model) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", receiverService.findPage(currentUser, pageable));
		return "member/receiver/list";
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(@CurrentUser Member currentUser, RedirectAttributes redirectAttributes) {
		if (Receiver.MAX_RECEIVER_COUNT != null && currentUser.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			addFlashMessage(redirectAttributes, "member.receiver.addCountNotAllowed", Receiver.MAX_RECEIVER_COUNT);
			return "redirect:list";
		}
		return "member/receiver/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public String save(@ModelAttribute("receiverForm") Receiver receiverForm, Long areaId, @CurrentUser Member currentUser, RedirectAttributes redirectAttributes) {
		Area area = areaService.find(areaId);
		receiverForm.setArea(area);
		if (!isValid(receiverForm)) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}
		if (Receiver.MAX_RECEIVER_COUNT != null && currentUser.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}
		receiverForm.setAreaName(null);
		receiverForm.setMember(currentUser);
		receiverService.save(receiverForm);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(@ModelAttribute(binding = false) Receiver receiver, ModelMap model) {
		if (receiver == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}

		model.addAttribute("receiver", receiver);
		return "member/receiver/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public String update(@ModelAttribute("receiverForm") Receiver receiverForm, @ModelAttribute(binding = false) Receiver receiver, Long areaId, RedirectAttributes redirectAttributes) {
		if (receiver == null) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}

		Area area = areaService.find(areaId);
		receiverForm.setArea(area);
		if (!isValid(receiverForm)) {
			return UNPROCESSABLE_ENTITY_VIEW;
		}
		receiverForm.setAreaName(area.getFullName());
		
		BeanUtils.copyProperties(receiverForm, receiver, "id", "version", "member");
		receiverService.update(receiver);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(@ModelAttribute(binding = false) Receiver receiver) {
		if (receiver == null) {
			return Results.NOT_FOUND;
		}

		receiverService.delete(receiver);
		return Results.OK;
	}

}