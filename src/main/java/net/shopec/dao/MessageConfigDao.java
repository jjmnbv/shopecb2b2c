package net.shopec.dao;

import net.shopec.entity.MessageConfig;

/**
 * Dao - 消息配置
 * 
 */
public interface MessageConfigDao extends BaseDao<MessageConfig> {

	/**
	 * 查找消息配置
	 * 
	 * @param type
	 *            类型
	 * @return 消息配置
	 */
	MessageConfig find(MessageConfig.Type type);

}