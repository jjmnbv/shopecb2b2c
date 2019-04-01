package net.shopec.service.impl;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import net.shopec.CommonAttributes;
import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.dao.AreaDao;
import net.shopec.dao.MemberAttributeDao;
import net.shopec.dao.MemberDao;
import net.shopec.entity.Area;
import net.shopec.entity.Member;
import net.shopec.entity.MemberAttribute;
import net.shopec.service.MemberAttributeService;

/**
 * Service - 会员注册项
 * 
 */
@Service
public class MemberAttributeServiceImpl extends BaseServiceImpl<MemberAttribute> implements MemberAttributeService {

	@Inject
	private MemberAttributeDao memberAttributeDao;
	@Inject
	private MemberDao memberDao;
	@Inject
	private AreaDao areaDao;

	@Transactional(readOnly = true)
	public Integer findUnusedPropertyIndex() {
		for (int i = 0; i < Member.ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
			Integer count = memberAttributeDao.findUnusedPropertyIndex(i);
			if (count == 0) {
				return i;
			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	public List<MemberAttribute> findList(Boolean isEnabled, Integer count, List<Filter> filters, List<Order> orders) {
		EntityWrapper<MemberAttribute> wrapper = toWrapper(null, count, filters, orders);
		return memberAttributeDao.findList(isEnabled, wrapper);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "memberAttribute", condition = "#useCache")
	public List<MemberAttribute> findList(Boolean isEnabled, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		EntityWrapper<MemberAttribute> wrapper = toWrapper(null, count, filters, orders);
		return memberAttributeDao.findList(isEnabled, wrapper);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "memberAttribute", condition = "#useCache")
	public List<MemberAttribute> findList(Boolean isEnabled, boolean useCache) {
		EntityWrapper<MemberAttribute> wrapper = toWrapper(null, null, null, null);
		return memberAttributeDao.findList(isEnabled, wrapper);
	}

	@Transactional(readOnly = true)
	public boolean isValid(MemberAttribute memberAttribute, String[] values) {
		Assert.notNull(memberAttribute, "NotNull");
		Assert.notNull(memberAttribute.getType(), "NotNull");

		String value = ArrayUtils.isNotEmpty(values) ? values[0].trim() : null;
		switch (memberAttribute.getType()) {
		case name:
		case address:
		case zipCode:
		case phone:
		case text:
			if (memberAttribute.getIsRequired() && StringUtils.isEmpty(value)) {
				return false;
			}
			if (StringUtils.isNotEmpty(memberAttribute.getPattern()) && StringUtils.isNotEmpty(value) && !Pattern.compile(memberAttribute.getPattern()).matcher(value).matches()) {
				return false;
			}
			break;
		case gender:
			if (memberAttribute.getIsRequired() && StringUtils.isEmpty(value)) {
				return false;
			}
			if (StringUtils.isNotEmpty(value)) {
				try {
					Member.Gender.valueOf(value);
				} catch (IllegalArgumentException e) {
					return false;
				}
			}
			break;
		case birth:
			if (memberAttribute.getIsRequired() && StringUtils.isEmpty(value)) {
				return false;
			}
			if (StringUtils.isNotEmpty(value)) {
				try {
					DateUtils.parseDate(value, CommonAttributes.DATE_PATTERNS);
				} catch (ParseException e) {
					return false;
				}
			}
			break;
		case area:
			Long id = NumberUtils.toLong(value, -1L);
			Area area = areaDao.find(id);
			if (memberAttribute.getIsRequired() && area == null) {
				return false;
			}
			break;
		case select:
			if (memberAttribute.getIsRequired() && StringUtils.isEmpty(value)) {
				return false;
			}
			if (CollectionUtils.isEmpty(memberAttribute.getOptions())) {
				return false;
			}
			if (StringUtils.isNotEmpty(value) && !memberAttribute.getOptions().contains(value)) {
				return false;
			}
			break;
		case checkbox:
			if (memberAttribute.getIsRequired() && ArrayUtils.isEmpty(values)) {
				return false;
			}
			if (CollectionUtils.isEmpty(memberAttribute.getOptions())) {
				return false;
			}
			if (ArrayUtils.isNotEmpty(values) && !memberAttribute.getOptions().containsAll(Arrays.asList(values))) {
				return false;
			}
			break;
		}
		return true;
	}

	@Transactional(readOnly = true)
	public Object toMemberAttributeValue(MemberAttribute memberAttribute, String[] values) {
		Assert.notNull(memberAttribute, "NotNull");
		Assert.notNull(memberAttribute.getType(), "NotNull");

		if (ArrayUtils.isEmpty(values)) {
			return null;
		}

		String value = values[0].trim();
		switch (memberAttribute.getType()) {
		case name:
		case address:
		case zipCode:
		case phone:
		case text:
			return StringUtils.isNotEmpty(value) ? value : null;
		case gender:
			if (StringUtils.isEmpty(value)) {
				return null;
			}
			try {
				return Member.Gender.valueOf(value);
			} catch (IllegalArgumentException e) {
				return null;
			}
		case birth:
			if (StringUtils.isEmpty(value)) {
				return null;
			}
			try {
				return DateUtils.parseDate(value, CommonAttributes.DATE_PATTERNS);
			} catch (ParseException e) {
				return null;
			}
		case area:
			Long id = NumberUtils.toLong(value, -1L);
			return areaDao.find(id);
		case select:
			if (CollectionUtils.isNotEmpty(memberAttribute.getOptions()) && memberAttribute.getOptions().contains(value)) {
				return value;
			}
			break;
		case checkbox:
			if (CollectionUtils.isNotEmpty(memberAttribute.getOptions()) && memberAttribute.getOptions().containsAll(Arrays.asList(values))) {
				return Arrays.asList(values);
			}
			break;
		}
		return null;
	}

	@Override
	@Transactional
	@CacheEvict(value = "memberAttribute", allEntries = true)
	public MemberAttribute save(MemberAttribute memberAttribute) {
		Assert.notNull(memberAttribute, "NotNull");

		Integer unusedPropertyIndex = findUnusedPropertyIndex();
		Assert.notNull(unusedPropertyIndex, "NotNull");

		memberAttribute.setPropertyIndex(unusedPropertyIndex);
		
		return super.save(memberAttribute);
	}

	@Override
	@Transactional
	@CacheEvict(value = "memberAttribute", allEntries = true)
	public MemberAttribute update(MemberAttribute memberAttribute) {
		return super.update(memberAttribute);
	}

	@Override
	@Transactional
	@CacheEvict(value = "memberAttribute", allEntries = true)
	public MemberAttribute update(MemberAttribute memberAttribute, String... ignoreProperties) {
		return super.update(memberAttribute, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "memberAttribute", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "memberAttribute", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "memberAttribute", allEntries = true)
	public void delete(MemberAttribute memberAttribute) {
		if (memberAttribute == null || memberAttribute.getType() == null || memberAttribute.getPropertyIndex() == null) {
			return;
		}
		String propertyName;
		switch (memberAttribute.getType()) {
		case text:
		case select:
		case checkbox:
			propertyName = Member.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + memberAttribute.getPropertyIndex();
			break;
		default:
			propertyName = String.valueOf(memberAttribute.getType());
			break;
		}
		if (propertyName != null) {
			propertyName = net.shopec.util.StringUtils.camel2Underline(propertyName);
			memberDao.clearAttributeValue(propertyName);
		}

		super.delete(memberAttribute);
	}

}