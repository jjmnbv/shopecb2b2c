package net.shopec.service;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.Member;
import net.shopec.entity.PointLog;

/**
 * Service - 积分记录
 * 
 */
public interface PointLogService extends BaseService<PointLog> {

	/**
	 * 查找积分记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 积分记录分页
	 */
	Page<PointLog> findPage(Member member, Pageable pageable);

}