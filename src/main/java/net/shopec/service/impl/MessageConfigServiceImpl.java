package net.shopec.service.impl;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.shopec.dao.MessageConfigDao;
import net.shopec.entity.MessageConfig;
import net.shopec.service.MessageConfigService;

/**
 * Service - 消息配置
 * 
 */
@Service
public class MessageConfigServiceImpl extends BaseServiceImpl<MessageConfig> implements MessageConfigService {

	@Inject
	private MessageConfigDao messageConfigDao;

	@Transactional(readOnly = true)
	@Cacheable("messageConfig")
	public MessageConfig find(MessageConfig.Type type) {
		return messageConfigDao.findByAttribute("type", type);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public MessageConfig save(MessageConfig messageConfig) {
		return super.save(messageConfig);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public MessageConfig update(MessageConfig messageConfig) {
		return super.update(messageConfig);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public MessageConfig update(MessageConfig messageConfig, String... ignoreProperties) {
		return super.update(messageConfig, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "messageConfig", allEntries = true)
	public void delete(MessageConfig messageConfig) {
		super.delete(messageConfig);
	}

}