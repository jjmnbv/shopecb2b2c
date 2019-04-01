package net.shopec.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.ReceiverDao;
import net.shopec.entity.Member;
import net.shopec.entity.Receiver;
import net.shopec.service.ReceiverService;

/**
 * Service - 收货地址
 * 
 */
@Service
public class ReceiverServiceImpl extends BaseServiceImpl<Receiver> implements ReceiverService {

	@Inject
	private ReceiverDao receiverDao;

	@Transactional(readOnly = true)
	public Receiver findDefault(Member member) {
		return receiverDao.findDefault(member);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Receiver> findList(Member member) {
		return receiverDao.findList(member);
	}

	@Transactional(readOnly = true)
	public Page<Receiver> findPage(Member member, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<Receiver> plusPage = getPlusPage(pageable);
		plusPage.setRecords(receiverDao.findPage(plusPage, getWrapper(pageable), member));
		return super.findPage(plusPage, pageable);
	}

	@Override
	@Transactional
	public Receiver save(Receiver receiver) {
		Assert.notNull(receiver, "notNull");

		if (BooleanUtils.isTrue(receiver.getIsDefault()) && receiver.getMember() != null) {
			receiverDao.clearDefault(receiver.getMember());
		}
		receiver.setAreaName(receiver.getArea().getFullName());
		return super.save(receiver);
	}

	@Override
	@Transactional
	public Receiver update(Receiver receiver) {
		Assert.notNull(receiver, "notNull");

		Receiver pReceiver = super.update(receiver);
		if (BooleanUtils.isTrue(pReceiver.getIsDefault()) && pReceiver.getMember() != null) {
			receiverDao.clearDefaultExclude(pReceiver.getMember(), pReceiver);
		}
		return pReceiver;
	}

}