package net.shopec.service;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.Member;
import net.shopec.entity.SocialUser;
import net.shopec.entity.User;

/**
 * Service - 社会化用户
 * 
 */
public interface SocialUserService extends BaseService<SocialUser> {

	/**
	 * 查找社会化用户
	 * 
	 * @param loginPluginId
	 *            登录插件ID
	 * @param uniqueId
	 *            唯一ID
	 * @return 社会化用户，若不存在则返回null
	 */
	SocialUser find(String loginPluginId, String uniqueId);

	/**
	 * 查找社会化用户分页
	 * 
	 * @param user
	 *            用户
	 * @param pageable
	 *            分页信息
	 * @return 社会化用户分页
	 */
	Page<SocialUser> findPage(User user, Pageable pageable);

	/**
	 * 绑定用户
	 * 
	 * @param user
	 *            用户
	 * @param socialUser
	 *            社会化用户
	 * @param uniqueId
	 *            唯一ID
	 */
	void bindUser(Member member, SocialUser socialUser, String uniqueId);

}