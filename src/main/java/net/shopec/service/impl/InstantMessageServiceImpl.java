package net.shopec.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.InstantMessageDao;
import net.shopec.entity.InstantMessage;
import net.shopec.entity.Store;
import net.shopec.service.InstantMessageService;

/**
 * Service - 即时通讯
 * 
 */
@Service
public class InstantMessageServiceImpl extends BaseServiceImpl<InstantMessage> implements InstantMessageService {

	@Inject
	private InstantMessageDao instantMessageDao;

	@Transactional(readOnly = true)
	public Page<InstantMessage> findPage(Store store, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<InstantMessage> plusPage = getPlusPage(pageable);
		plusPage.setRecords(instantMessageDao.findPage(plusPage, getWrapper(pageable), store));
		return super.findPage(plusPage, pageable);
	}

	@Override
	public InstantMessage save(InstantMessage instantMessage) {
		return super.save(instantMessage);
	}
}