package net.shopec.dao;

import net.shopec.entity.AuditLog;

/**
 * Dao - 审计日志
 * 
 */
public interface AuditLogDao extends BaseDao<AuditLog> {

	/**
	 * 删除所有
	 */
	void removeAll();

}