package net.shopec.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.SocialUserDao;
import net.shopec.entity.Member;
import net.shopec.entity.SocialUser;
import net.shopec.entity.User;
import net.shopec.service.SocialUserService;

/**
 * Service - 社会化用户
 * 
 */
@Service
public class SocialUserServiceImpl extends BaseServiceImpl<SocialUser> implements SocialUserService {

	@Inject
	private SocialUserDao socialUserDao;

	@Override
	public SocialUser find(Long id) {
		return socialUserDao.find(id);
	}
	
	@Transactional(readOnly = true)
	public SocialUser find(String loginPluginId, String uniqueId) {
		return socialUserDao.findPlugin(loginPluginId, uniqueId);
	}

	@Transactional(readOnly = true)
	public Page<SocialUser> findPage(User user, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<SocialUser> plusPage = getPlusPage(pageable);
		plusPage.setRecords(socialUserDao.findPage(plusPage, getWrapper(pageable), user));
		return super.findPage(plusPage, pageable);
	}

	public void bindUser(Member member, SocialUser socialUser, String uniqueId) {
		Assert.notNull(socialUser, "notNull");
		Assert.hasText(uniqueId, "hasText");

		if (!socialUser.getUniqueId().equals(uniqueId) || socialUser.getMember() != null) {
			return;
		}

		socialUser.setMember(member);
	}

}