package net.shopec.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.MessageDao;
import net.shopec.entity.Member;
import net.shopec.entity.Message;
import net.shopec.service.MessageService;

/**
 * Service - 消息
 * 
 */
@Service
public class MessageServiceImpl extends BaseServiceImpl<Message> implements MessageService {

	@Inject
	private MessageDao messageDao;

	
	@Transactional(readOnly = true)
	public Page<Message> findPage(Member member, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<Message> plusPage = getPlusPage(pageable);
		plusPage.setRecords(messageDao.findPage(plusPage, getWrapper(pageable), member));
		return super.findPage(plusPage, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Message> findDraftPage(Member sender, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<Message> plusPage = getPlusPage(pageable);
		plusPage.setRecords(messageDao.findDraftPage(plusPage, getWrapper(pageable), sender));
		return super.findPage(plusPage, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member, Boolean read) {
		return messageDao.count(member, read);
	}

	@Override
	public Message save(Message message) {
		return super.save(message);
	}
	
	public void delete(Long id, Member member) {
		Message message = messageDao.find(id);
		if (message == null || message.getForMessage() != null) {
			return;
		}
		if ((member != null && member.equals(message.getReceiver())) || (member == null && message.getReceiver() == null)) {
			if (!message.getIsDraft()) {
				if (message.getSenderDelete()) {
					super.delete(message);
				} else {
					message.setReceiverDelete(true);
					super.update(message);
				}
			}
		} else if ((member != null && member.equals(message.getSender())) || (member == null && message.getSender() == null)) {
			if (message.getIsDraft()) {
				super.delete(message);
			} else {
				if (message.getReceiverDelete()) {
					super.delete(message);
				} else {
					message.setSenderDelete(true);
					super.update(message);
				}
			}
		}
	}

}