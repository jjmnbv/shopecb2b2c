package net.shopec.service.impl;

import java.math.BigDecimal;
import java.util.Set;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.IdWorker;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.MemberDao;
import net.shopec.dao.MemberDepositLogDao;
import net.shopec.dao.MemberRankDao;
import net.shopec.dao.PointLogDao;
import net.shopec.dao.UserDao;
import net.shopec.entity.Member;
import net.shopec.entity.MemberDepositLog;
import net.shopec.entity.MemberRank;
import net.shopec.entity.PointLog;
import net.shopec.entity.User;
import net.shopec.service.MemberService;

/**
 * Service - 会员
 * 
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberDao, Member> implements MemberService {

	/**
	 * E-mail身份配比
	 */
	private static final Pattern EMAIL_PRINCIPAL_PATTERN = Pattern.compile(".*@.*");

	/**
	 * 手机身份配比
	 */
	private static final Pattern MOBILE_PRINCIPAL_PATTERN = Pattern.compile("\\d+");

	@Inject
	private MemberDao memberDao;
	@Inject
	private UserDao userDao;
	@Inject
	private MemberRankDao memberRankDao;
	@Inject
	private MemberDepositLogDao memberDepositLogDao;
	@Inject
	private PointLogDao pointLogDao;
//	@Inject
//	private MailService mailService;
//	@Inject
//	private SmsService smsService;

	@Override
	public Member find(Long id) {
		return memberDao.find(id);
	}
	
	@Override
	public long count() {
		return selectCount(null);
	}
	
	@Transactional(readOnly = true)
	public Member getUser(Object principal) {
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
		Assert.isInstanceOf(Member.class, user);

		return Member.PERMISSIONS;
	}

	@Transactional(readOnly = true)
	public boolean supports(Class<?> userClass) {
		return userClass != null && Member.class.isAssignableFrom(userClass);
	}

	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return memberDao.exists("username", StringUtils.lowerCase(username));
	}

	@Transactional(readOnly = true)
	public Member findByUsername(String username) {
		return memberDao.findByAttribute("username", StringUtils.lowerCase(username));
	}

	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return memberDao.exists("email", StringUtils.lowerCase(email));
	}

	@Transactional(readOnly = true)
	public boolean emailUnique(Long id, String email) {
		return !memberDao.unique(id, "email", StringUtils.lowerCase(email));
	}

	@Transactional(readOnly = true)
	public Member findByEmail(String email) {
		return memberDao.findByAttribute("email", StringUtils.lowerCase(email));
	}

	@Transactional(readOnly = true)
	public boolean mobileExists(String mobile) {
		return memberDao.exists("mobile", StringUtils.lowerCase(mobile));
	}

	@Transactional(readOnly = true)
	public boolean mobileUnique(Long id, String mobile) {
		return memberDao.unique(id, "mobile", StringUtils.lowerCase(mobile));
	}

	@Transactional(readOnly = true)
	public Member findByMobile(String mobile) {
		return memberDao.findByAttribute("mobile", StringUtils.lowerCase(mobile));
	}

	@Transactional(readOnly = true)
	public Page<Member> findPage(Member.RankingType rankingType, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<Member> plusPage = new com.baomidou.mybatisplus.plugins.Page<Member>(pageable.getPageNumber(), pageable.getPageSize());
		EntityWrapper<Member> entityWrapper = new EntityWrapper<Member>();
		// 增加搜索属性、搜索值
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if (StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)) {
			entityWrapper.like(searchProperty, searchValue);
		}
		plusPage.setRecords(memberDao.findPage(plusPage, entityWrapper, rankingType));
		Page<Member> page = new Page<Member>(plusPage.getRecords(), plusPage.getTotal(), pageable);
		return page;
	}

	public void addBalance(Member member, BigDecimal amount, MemberDepositLog.Type type, String memo) {
		Assert.notNull(member, "notNull");
		Assert.notNull(amount, "notNull");
		Assert.notNull(type, "notNull");

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		Assert.notNull(member.getBalance(), "notNull");
		Assert.state(member.getBalance().add(amount).compareTo(BigDecimal.ZERO) >= 0, "state");

		member.setBalance(member.getBalance().add(amount));
		memberDao.update(member);

		MemberDepositLog memberDepositLog = new MemberDepositLog();
		memberDepositLog.setId(IdWorker.getId());
		memberDepositLog.setType(type);
		memberDepositLog.setCredit(amount.compareTo(BigDecimal.ZERO) > 0 ? amount : BigDecimal.ZERO);
		memberDepositLog.setDebit(amount.compareTo(BigDecimal.ZERO) < 0 ? amount.abs() : BigDecimal.ZERO);
		memberDepositLog.setBalance(member.getBalance());
		memberDepositLog.setMemo(memo);
		memberDepositLog.setMember(member);
		memberDepositLogDao.save(memberDepositLog);
	}

	public void addPoint(Member member, long amount, PointLog.Type type, String memo) {
		Assert.notNull(member, "notNull");
		Assert.notNull(type, "notNull");

		if (amount == 0) {
			return;
		}

		Assert.notNull(member.getPoint(), "notNull");
		Assert.state(member.getPoint() + amount >= 0, "state");

		member.setPoint(member.getPoint() + amount);
		memberDao.update(member);

		PointLog pointLog = new PointLog();
		pointLog.setId(IdWorker.getId());
		pointLog.setType(type);
		pointLog.setCredit(amount > 0 ? amount : 0L);
		pointLog.setDebit(amount < 0 ? Math.abs(amount) : 0L);
		pointLog.setBalance(member.getPoint());
		pointLog.setMemo(memo);
		pointLog.setMember(member);
		pointLogDao.save(pointLog);
	}

	public void addAmount(Member member, BigDecimal amount) {
		Assert.notNull(member, "notNull");
		Assert.notNull(amount, "notNull");

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		Assert.notNull(member.getAmount(), "notNull");
		Assert.state(member.getAmount().add(amount).compareTo(BigDecimal.ZERO) >= 0, "state");

		member.setAmount(member.getAmount().add(amount));
		MemberRank memberRank = member.getMemberRank();
		if (memberRank != null && BooleanUtils.isFalse(memberRank.getIsSpecial())) {
			MemberRank newMemberRank = memberRankDao.findByAmount(member.getAmount());
			if (newMemberRank != null && newMemberRank.getAmount() != null && newMemberRank.getAmount().compareTo(memberRank.getAmount()) > 0) {
				member.setMemberRank(newMemberRank);
			}
		}
		memberDao.update(member);
	}

	@Override
	public Page<Member> findPage(Pageable pageable) {
		EntityWrapper<Member> wrapper = new EntityWrapper<Member>();
		return findPage(wrapper, pageable);
	}


	@Override
	@Transactional
	public Member save(Member member) {
		Assert.notNull(member, "NotNull");

		member.setUsername(StringUtils.lowerCase(member.getUsername()));
		member.setEmail(StringUtils.lowerCase(member.getEmail()));
		member.setMobile(StringUtils.lowerCase(member.getMobile()));
		memberDao.save(member);
		return member;
	}

	@Override
	public Member update(Member member) {
		member.setEmail(StringUtils.lowerCase(member.getEmail()));
		member.setMobile(StringUtils.lowerCase(member.getMobile()));
		
		userDao.update(member);
		memberDao.update(member);
		return member;
	}

	@Override
	public Member update(Member member, String... ignoreProperties) {
		Assert.notNull(member, "Member Not Null");
		Assert.isTrue(!member.isNew(), "Member is True");

		Member persistant = memberDao.find(member.getId());
		if (persistant != null) {
			BeanUtils.copyProperties(member, persistant, ignoreProperties);
		}
		return update(persistant);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		memberDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public void delete(Long... ids) {
		if (ids != null) {
			for (Long id : ids) {
				memberDao.deleteById(id);
				userDao.deleteById(id);
			}
		}
	}
	
	/**
	 * 分页
	 * @param wrapper
	 * @param pageable
	 * @return
	 */
	protected Page<Member> findPage(Wrapper<Member> wrapper, Pageable pageable) {
		// 搜索属性、搜索值
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if (StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)) {
			wrapper.like(searchProperty, searchValue);
		}
		com.baomidou.mybatisplus.plugins.Page<Member> plusPage = selectPage(new com.baomidou.mybatisplus.plugins.Page<Member>(pageable.getPageNumber(), pageable.getPageSize()), wrapper);
        Page<Member> page = new Page<Member>(plusPage.getRecords(), plusPage.getTotal(), pageable);
		return page;
	}
	
}