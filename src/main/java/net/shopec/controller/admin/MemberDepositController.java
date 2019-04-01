/*
 * Copyright 2005-2017 shopec.net. All rights reserved.
 * Support: http://www.shopec.net
 * License: http://www.shopec.net/license
 */
package net.shopec.controller.admin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.shopec.Message;
import net.shopec.Pageable;
import net.shopec.entity.Admin;
import net.shopec.entity.Member;
import net.shopec.entity.MemberDepositLog;
import net.shopec.security.CurrentUser;
import net.shopec.service.MemberDepositLogService;
import net.shopec.service.MemberService;

/**
 * Controller - 会员预存款
 * 
 */
@Controller("adminMemberDepositController")
@RequestMapping("/admin/member_deposit")
public class MemberDepositController extends BaseController {

	@Inject
	private MemberDepositLogService memberDepositLogService;
	@Inject
	private MemberService memberService;

	/**
	 * 检查会员
	 */
	@GetMapping("/check_member")
	public @ResponseBody Map<String, Object> checkMember(String username) {
		Map<String, Object> data = new HashMap<>();
		Member member = memberService.findByUsername(username);
		if (member == null) {
			data.put("message", Message.warn("admin.memberDeposit.memberNotExist"));
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("balance", member.getBalance());
		return data;
	}

	/**
	 * 调整
	 */
	@GetMapping("/adjust")
	public String adjust() {
		return "admin/member_deposit/adjust";
	}

	/**
	 * 调整
	 */
	@PostMapping("/adjust")
	public String adjust(String username, BigDecimal amount, String memo, @CurrentUser Admin currentUser, RedirectAttributes redirectAttributes) {
		Member member = memberService.findByUsername(username);
		if (member == null) {
			return ERROR_VIEW;
		}
		if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
			return ERROR_VIEW;
		}
		if (member.getBalance() == null || member.getBalance().add(amount).compareTo(BigDecimal.ZERO) < 0) {
			return ERROR_VIEW;
		}
		memberService.addBalance(member, amount, MemberDepositLog.Type.adjustment, memo);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:log";
	}

	/**
	 * 记录
	 */
	@GetMapping("/log")
	public String log(Long memberId, Pageable pageable, ModelMap model) {
		Member member = memberService.find(memberId);
		if (member != null) {
			model.addAttribute("member", member);
			model.addAttribute("page", memberDepositLogService.findPage(member, pageable));
		} else {
			model.addAttribute("page", memberDepositLogService.findPage(pageable));
		}
		return "admin/member_deposit/log";
	}

}