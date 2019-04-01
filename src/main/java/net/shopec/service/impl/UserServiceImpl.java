package net.shopec.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.shopec.Setting;
import net.shopec.dao.AdminDao;
import net.shopec.dao.BusinessDao;
import net.shopec.dao.MemberDao;
import net.shopec.dao.UserDao;
import net.shopec.entity.Admin;
import net.shopec.entity.Business;
import net.shopec.entity.Member;
import net.shopec.entity.User;
import net.shopec.event.UserLoggedInEvent;
import net.shopec.event.UserLoggedOutEvent;
import net.shopec.event.UserRegisteredEvent;
import net.shopec.security.AuthenticationProvider;
import net.shopec.security.SocialUserAuthenticationToken;
import net.shopec.security.UserAuthenticationToken;
import net.shopec.service.UserService;
import net.shopec.util.SystemUtils;

/**
 * Service - 用户
 * 
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	/**
	 * 认证Provider缓存
	 */
	private static final Map<Class<?>, AuthenticationProvider> AUTHENTICATION_PROVIDER_CACHE = new ConcurrentHashMap<>();

	@Inject
	private ApplicationEventPublisher applicationEventPublisher;
	@Inject
	private List<AuthenticationProvider> authenticationProviders;
	@Inject
	private CacheManager cacheManager;
	@Inject
	private UserDao userDao;
	@Inject
	private MemberDao memberDao;
	@Inject
	private BusinessDao businessDao;
	@Inject
	private AdminDao adminDao;
	

	@Transactional(readOnly = true)
	public User getCurrentAuditor() {
		return getCurrent();
	}

	@Transactional(noRollbackFor = AuthenticationException.class)
	public User getUser(AuthenticationToken authenticationToken) {
		Assert.notNull(authenticationToken, "notNull");
		Assert.state(authenticationToken instanceof UserAuthenticationToken || authenticationToken instanceof SocialUserAuthenticationToken, "state");

		User user = null;
		if (authenticationToken instanceof UserAuthenticationToken) {
			UserAuthenticationToken userAuthenticationToken = (UserAuthenticationToken) authenticationToken;
			Class<?> userClass = userAuthenticationToken.getUserClass();
			Object principal = userAuthenticationToken.getPrincipal();

			if (userClass == null || principal == null) {
				throw new UnknownAccountException();
			}
			AuthenticationProvider authenticationProvider = getAuthenticationProvider(userClass);
			user = authenticationProvider != null ? authenticationProvider.getUser(principal) : null;
		} else if (authenticationToken instanceof SocialUserAuthenticationToken) {
			SocialUserAuthenticationToken socialUserAuthenticationToken = (SocialUserAuthenticationToken) authenticationToken;
			user = socialUserAuthenticationToken.getSocialUser() != null ? socialUserAuthenticationToken.getSocialUser().getMember() : null;
		}

		if (user == null) {
			throw new UnknownAccountException();
		}
		if (BooleanUtils.isNotTrue(user.getIsEnabled())) {
			throw new DisabledAccountException();
		}
		if (BooleanUtils.isTrue(user.getIsLocked()) && !tryUnlock(user)) {
			throw new LockedAccountException();
		}
		if (authenticationToken instanceof UserAuthenticationToken) {
			Object credentials = authenticationToken.getCredentials();
			if (!user.isValidCredentials(credentials)) {
				addFailedLoginAttempt(user);
				tryLock(user);
				throw new IncorrectCredentialsException();
			}
		}
		if (authenticationToken instanceof HostAuthenticationToken) {
			HostAuthenticationToken hostAuthenticationToken = (HostAuthenticationToken) authenticationToken;
			user.setLastLoginIp(hostAuthenticationToken.getHost());
		}
		user.setLastLoginDate(new Date());
		resetFailedLoginAttempts(user);
		return user;
	}

	@Transactional(readOnly = true)
	public Set<String> getPermissions(User user) {
		Assert.notNull(user, "notNull");

		AuthenticationProvider authenticationProvider = getAuthenticationProvider(user.getClass());
		return authenticationProvider != null ? authenticationProvider.getPermissions(user) : null;
	}

	public void register(User user) {
		Assert.notNull(user, "notNull");

		userDao.save(user);

		applicationEventPublisher.publishEvent(new UserRegisteredEvent(this, user));
	}

	public void login(AuthenticationToken authenticationToken) {
		Assert.notNull(authenticationToken, "notNull");

		Subject subject = SecurityUtils.getSubject();
		subject.login(authenticationToken);

		applicationEventPublisher.publishEvent(new UserLoggedInEvent(this, getCurrent()));
	}

	public void logout() {
		applicationEventPublisher.publishEvent(new UserLoggedOutEvent(this, getCurrent()));

		Subject subject = SecurityUtils.getSubject();
		subject.logout();
	}

	@Transactional(readOnly = true)
	public User getCurrent() {
		return getCurrent(null);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <T extends User> T getCurrent(Class<T> userClass) {
		Subject subject = SecurityUtils.getSubject();
		User principal = subject != null && subject.getPrincipal() instanceof User ? (User) subject.getPrincipal() : null;
		if (principal != null) {
			if (principal instanceof Member) {
				User user = memberDao.find(principal.getId());
				if (userClass == null || (user != null && userClass.isAssignableFrom(user.getClass()))) {
					return (T) user;
				}
			} else if (principal instanceof Business) {
				User user = businessDao.find(principal.getId());
				if (userClass == null || (user != null && userClass.isAssignableFrom(user.getClass()))) {
					return (T) user;
				}
			} else if (principal instanceof Admin) {
				User user = adminDao.find(principal.getId());
				if (userClass == null || (user != null && userClass.isAssignableFrom(user.getClass()))) {
					return (T) user;
				}
			}
			//User user = userDao.find(principal.getId());
//			if (userClass == null || (user != null && userClass.isAssignableFrom(user.getClass()))) {
//				return (T) user;
//			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	public int getFailedLoginAttempts(User user) {
		Assert.notNull(user, "notNull");
		Assert.isTrue(!user.isNew(), "isTrue");

		Ehcache cache = cacheManager.getEhcache(User.FAILED_LOGIN_ATTEMPTS_CACHE_NAME);
		Element element = cache.get(user.getId());
		AtomicInteger failedLoginAttempts = element != null ? (AtomicInteger) element.getObjectValue() : null;
		return failedLoginAttempts != null ? failedLoginAttempts.get() : 0;
	}

	@Transactional(readOnly = true)
	public void addFailedLoginAttempt(User user) {
		Assert.notNull(user, "notNull");
		Assert.isTrue(!user.isNew(), "isTrue");

		Long userId = user.getId();
		Ehcache cache = cacheManager.getEhcache(User.FAILED_LOGIN_ATTEMPTS_CACHE_NAME);
		cache.acquireWriteLockOnKey(userId);
		try {
			Element element = cache.get(userId);
			AtomicInteger failedLoginAttempts = element != null ? (AtomicInteger) element.getObjectValue() : null;
			if (failedLoginAttempts != null) {
				failedLoginAttempts.incrementAndGet();
			} else {
				cache.put(new Element(userId, new AtomicInteger(1)));
			}
		} finally {
			cache.releaseWriteLockOnKey(userId);
		}
	}

	@Transactional(readOnly = true)
	public void resetFailedLoginAttempts(User user) {
		Assert.notNull(user, "notNull");
		Assert.isTrue(!user.isNew(), "isTrue");

		Ehcache cache = cacheManager.getEhcache(User.FAILED_LOGIN_ATTEMPTS_CACHE_NAME);
		cache.remove(user.getId());
	}

	public boolean tryLock(User user) {
		Assert.notNull(user, "notNull");
		Assert.isTrue(!user.isNew(), "isTrue");

		if (BooleanUtils.isTrue(user.getIsLocked())) {
			return true;
		}

		Setting setting = SystemUtils.getSetting();
		if (setting.getMaxFailedLoginAttempts() != null) {
			int failedLoginAttempts = getFailedLoginAttempts(user);
			if (failedLoginAttempts >= setting.getMaxFailedLoginAttempts()) {
				user.setIsLocked(true);
				user.setLockDate(new Date());
				return true;
			}
		}
		return false;
	}

	public boolean tryUnlock(User user) {
		Assert.notNull(user, "notNull");
		Assert.isTrue(!user.isNew(), "isTrue");

		if (BooleanUtils.isFalse(user.getIsLocked())) {
			return true;
		}

		Setting setting = SystemUtils.getSetting();
		if (setting.getPasswordLockTime() != null) {
			Date lockDate = user.getLockDate();
			Date unlockDate = DateUtils.addMinutes(lockDate, setting.getPasswordLockTime());
			if (new Date().after(unlockDate)) {
				unlock(user);
				return true;
			}
		}
		return false;
	}

	public void unlock(User user) {
		Assert.notNull(user, "notNull");
		Assert.isTrue(!user.isNew(), "isTrue");

		if (BooleanUtils.isFalse(user.getIsLocked())) {
			return;
		}

		user.setIsLocked(false);
		user.setLockDate(null);
		userDao.update(user);
		resetFailedLoginAttempts(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public User save(User user) {
		userDao.save(user);
		return user;
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public User update(User user) {
		userDao.update(user);
		return user;
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public User update(User user, String... ignoreProperties) {
		return super.update(user, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(User user) {
		super.delete(user);
	}

	/**
	 * 获取认证Provider
	 * 
	 * @param userClass
	 *            用户类型
	 * @return 认证Provider，若不存在则返回null
	 */
	private AuthenticationProvider getAuthenticationProvider(Class<?> userClass) {
		Assert.notNull(userClass, "notNull");

		if (AUTHENTICATION_PROVIDER_CACHE.containsKey(userClass)) {
			return AUTHENTICATION_PROVIDER_CACHE.get(userClass);
		}

		if (CollectionUtils.isNotEmpty(authenticationProviders)) {
			for (AuthenticationProvider authenticationProvider : authenticationProviders) {
				if (authenticationProvider != null && authenticationProvider.supports(userClass)) {
					AUTHENTICATION_PROVIDER_CACHE.put(userClass, authenticationProvider);
					return authenticationProvider;
				}
			}
		}
		return null;
	}

}