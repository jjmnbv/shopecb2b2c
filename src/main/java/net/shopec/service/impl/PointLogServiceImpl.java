package net.shopec.service.impl;

import javax.inject.Inject;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.PointLogDao;
import net.shopec.entity.Member;
import net.shopec.entity.PointLog;
import net.shopec.service.PointLogService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 积分记录
 * 
 */
@Service
public class PointLogServiceImpl extends BaseServiceImpl<PointLog> implements PointLogService {

	@Inject
	private PointLogDao pointLogDao;

	@Transactional(readOnly = true)
	public Page<PointLog> findPage(Member member, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<PointLog> plusPage = getPlusPage(pageable);
		plusPage.setRecords(pointLogDao.findPage(plusPage, getWrapper(pageable), member));
		return super.findPage(plusPage, pageable);
	}

}