package net.shopec.service.impl;


import javax.inject.Inject;

import net.shopec.dao.AuditLogDao;
import net.shopec.entity.AuditLog;
import net.shopec.service.AuditLogService;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 审计日志
 * 
 */
@Service
public class AuditLogServiceImpl extends BaseServiceImpl<AuditLog> implements AuditLogService {

	@Inject
	private AuditLogDao auditLogDao;

	@Override
	@Transactional(readOnly = true)
	public AuditLog find(Long id) {
		return auditLogDao.find(id);
	}
	
	@Async
	public void create(AuditLog auditLog) {
		auditLogDao.save(auditLog);
	}

	public void clear() {
		auditLogDao.removeAll();
	}

}