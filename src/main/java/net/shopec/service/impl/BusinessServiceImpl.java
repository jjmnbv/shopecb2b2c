package net.shopec.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.IdWorker;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.BusinessDao;
import net.shopec.dao.BusinessDepositLogDao;
import net.shopec.dao.UserDao;
import net.shopec.entity.Business;
import net.shopec.entity.BusinessDepositLog;
import net.shopec.entity.Store;
import net.shopec.entity.User;
import net.shopec.service.BusinessService;

/**
 * Service - 商家
 * 
 */
@Service
public class BusinessServiceImpl extends ServiceImpl<BusinessDao, Business> implements BusinessService {

	/**
	 * E-mail身份配比
	 */
	private static final Pattern EMAIL_PRINCIPAL_PATTERN = Pattern.compile(".*@.*");

	/**
	 * 手机身份配比
	 */
	private static final Pattern MOBILE_PRINCIPAL_PATTERN = Pattern.compile("\\d+");

	@Inject
	private BusinessDao businessDao;
	@Inject
	private UserDao userDao;
	@Inject
	private BusinessDepositLogDao businessDepositLogDao;
//	@Inject
//	private MailService mailService;
//	@Inject
//	private SmsService smsService;

	@Transactional(readOnly = true)
	public Business find(Long id) {
		return businessDao.find(id);
	}
	
	@Transactional(readOnly = true)
	public List<Business> findAll() {
		return businessDao.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<Business> findPage(Pageable pageable) {
		EntityWrapper<Business> wrapper = new EntityWrapper<Business>();
		// 搜索属性、搜索值
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if (StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)) {
			wrapper.like(searchProperty, searchValue);
		}
		com.baomidou.mybatisplus.plugins.Page<Business> plusPage = selectPage(new com.baomidou.mybatisplus.plugins.Page<Business>(pageable.getPageNumber(), pageable.getPageSize()), wrapper);
        Page<Business> page = new Page<Business>(plusPage.getRecords(), plusPage.getTotal(), pageable);
		return page;
	}
	
	
	
	@Override
	@Transactional
	public void delete(Long... ids) {
		if (ids != null) {
			for (Long id : ids) {
				businessDao.deleteById(id);
				userDao.deleteById(id);
			}
		}
	}
	
	@Transactional(readOnly = true)
	public Business getUser(Object principal) {
		Assert.notNull(principal, "notNull");
		Assert.isInstanceOf(String.class, principal);

		String value = (String) principal;
		if (EMAIL_PRINCIPAL_PATTERN.matcher(value).matches()) {
			return findByEmail(value);
		} else if (MOBILE_PRINCIPAL_PATTERN.matcher(value).matches()) {
			return findByMobile(value);
		} else {
			return findByUsername(value);
		}
	}

	@Transactional(readOnly = true)
	public Set<String> getPermissions(User user) {
		Assert.notNull(user, "notNull");
		Assert.isInstanceOf(Business.class, user);

		Business business = (Business) user;
		Business pBusiness = businessDao.find(business.getId());
		Store store = pBusiness.getStore();
		return store != null && store.isActive() ? Business.NORMAL_BUSINESS_PERMISSIONS : Business.RESTRICT_BUSINESS_PERMISSIONS;
	}

	@Transactional(readOnly = true)
	public boolean supports(Class<?> userClass) {
		return userClass != null && Business.class.isAssignableFrom(userClass);
	}

	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return businessDao.exists("username", StringUtils.lowerCase(username));
	}

	@Transactional(readOnly = true)
	public Business findByUsername(String username) {
		return businessDao.findByAttribute("username", StringUtils.lowerCase(username));
	}

	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return businessDao.exists("email", StringUtils.lowerCase(email));
	}

	@Transactional(readOnly = true)
	public boolean emailUnique(Long id, String email) {
		return businessDao.unique(id, "email", StringUtils.lowerCase(email));
	}

	@Transactional(readOnly = true)
	public Business findByEmail(String email) {
		return businessDao.findByAttribute("email", StringUtils.lowerCase(email));
	}

	@Transactional(readOnly = true)
	public boolean mobileExists(String mobile) {
		return businessDao.exists("mobile", StringUtils.lowerCase(mobile));
	}

	@Transactional(readOnly = true)
	public boolean mobileUnique(Long id, String mobile) {
		return businessDao.unique(id, "mobile", StringUtils.lowerCase(mobile));
	}

	@Transactional(readOnly = true)
	public Business findByMobile(String mobile) {
		return businessDao.findByAttribute("mobile", StringUtils.lowerCase(mobile));
	}

	@Transactional(readOnly = true)
	public List<Business> search(String keyword, Integer count) {
		return businessDao.search(keyword, count);
	}

	@Transactional
	public void addBalance(Business business, BigDecimal amount, BusinessDepositLog.Type type, String memo) {
		Assert.notNull(business, "notNull");
		Assert.notNull(amount, "notNull");
		Assert.notNull(type, "notNull");

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		Assert.notNull(business.getBalance(), "notNull");
		Assert.state(business.getBalance().add(amount).compareTo(BigDecimal.ZERO) >= 0, "state");

		business.setBalance(business.getBalance().add(amount));
		businessDao.update(business);

		BusinessDepositLog businessDepositLog = new BusinessDepositLog();
		businessDepositLog.setId(IdWorker.getId());
		businessDepositLog.setType(type);
		businessDepositLog.setCredit(amount.compareTo(BigDecimal.ZERO) > 0 ? amount : BigDecimal.ZERO);
		businessDepositLog.setDebit(amount.compareTo(BigDecimal.ZERO) < 0 ? amount.abs() : BigDecimal.ZERO);
		businessDepositLog.setBalance(business.getBalance());
		businessDepositLog.setMemo(memo);
		businessDepositLog.setBusiness(business);
		businessDepositLogDao.save(businessDepositLog);
	}

	@Transactional
	public void addFrozenFund(Business business, BigDecimal amount) {
		Assert.notNull(business, "notNull");
		Assert.notNull(amount, "notNull");

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		Assert.notNull(business.getFrozenFund(), "notNull");
		Assert.state(business.getFrozenFund().add(amount).compareTo(BigDecimal.ZERO) >= 0, "notNull");

		business.setFrozenFund(business.getFrozenFund().add(amount));
		businessDao.update(business);
	}

	@Override
	@Transactional
	public Business save(Business business) {
		Assert.notNull(business, "notNull");
		
		business.setUsername(StringUtils.lowerCase(business.getUsername()));
		business.setEmail(StringUtils.lowerCase(business.getEmail()));
		business.setMobile(StringUtils.lowerCase(business.getMobile()));
		businessDao.save(business);
		//mailService.sendRegisterBusinessMail(pBusiness);
		//smsService.sendRegisterBusinessSms(pBusiness);
		return business;
	}
	
	@Override
	public int update(Business business) {
		Assert.notNull(business, "notNull");
		Assert.isTrue(!business.isNew(), "isTrue");
		
		business.setEmail(StringUtils.lowerCase(business.getEmail()));
		business.setMobile(StringUtils.lowerCase(business.getMobile()));
		return businessDao.update(business);
	}
	
	@Override
	@Transactional
	public int update(Business business, String... ignoreProperties) {
		Assert.notNull(business, "notNull");
		Assert.isTrue(!business.isNew(), "isTrue");

		Business persistant = find(business.getId());
		if (persistant != null) {
			BeanUtils.copyProperties(business, persistant, ignoreProperties);
		}
		return update(persistant);
	}
}