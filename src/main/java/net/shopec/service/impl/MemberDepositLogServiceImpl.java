package net.shopec.service.impl;

import javax.inject.Inject;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.MemberDepositLogDao;
import net.shopec.entity.Member;
import net.shopec.entity.MemberDepositLog;
import net.shopec.service.MemberDepositLogService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 会员预存款记录
 * 
 */
@Service
public class MemberDepositLogServiceImpl extends BaseServiceImpl<MemberDepositLog> implements MemberDepositLogService {

	@Inject
	private MemberDepositLogDao memberDepositLogDao;

	@Transactional(readOnly = true)
	public Page<MemberDepositLog> findPage(Member member, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<MemberDepositLog> plusPage = getPlusPage(pageable);
		plusPage.setRecords(memberDepositLogDao.findPage(plusPage, getWrapper(pageable), member));
		return super.findPage(plusPage, pageable);
	}

}