package net.shopec.service;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.Member;
import net.shopec.entity.MemberDepositLog;

/**
 * Service - 会员预存款记录
 * 
 */
public interface MemberDepositLogService extends BaseService<MemberDepositLog> {

	/**
	 * 查找会员预存款记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 会员预存款记录分页
	 */
	Page<MemberDepositLog> findPage(Member member, Pageable pageable);

}