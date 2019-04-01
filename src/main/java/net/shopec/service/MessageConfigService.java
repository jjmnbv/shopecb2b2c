package net.shopec.service;

import net.shopec.entity.MessageConfig;

/**
 * Service - 消息配置
 * 
 */
public interface MessageConfigService extends BaseService<MessageConfig> {

	/**
	 * 查找消息配置
	 * 
	 * @param type
	 *            类型
	 * @return 消息配置
	 */
	MessageConfig find(MessageConfig.Type type);

}