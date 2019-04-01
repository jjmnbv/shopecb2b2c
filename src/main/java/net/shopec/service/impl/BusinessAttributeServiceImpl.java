package net.shopec.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.dao.BusinessAttributeDao;
import net.shopec.entity.Business;
import net.shopec.entity.BusinessAttribute;
import net.shopec.entity.Member;
import net.shopec.service.BusinessAttributeService;

/**
 * Service - 商家注册项
 * 
 */
@Service
public class BusinessAttributeServiceImpl extends BaseServiceImpl<BusinessAttribute> implements BusinessAttributeService {

	@Inject
	private BusinessAttributeDao businessAttributeDao;

	@Transactional(readOnly = true)
	public Integer findUnusedPropertyIndex() {
		for (int i = 0; i < Member.ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
			Integer count = businessAttributeDao.findUnusedPropertyIndex(i);
			if (count == 0) {
				return i;
			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "businessAttribute", condition = "#useCache")
	public List<BusinessAttribute> findList(Boolean isEnabled, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return businessAttributeDao.findList(isEnabled, count, filters, orders);

	}

	@Transactional(readOnly = true)
	@Cacheable(value = "businessAttribute", condition = "#useCache")
	public List<BusinessAttribute> findList(Boolean isEnabled, boolean useCache) {
		return businessAttributeDao.findList(isEnabled, null, null, null);
	}

	@Transactional(readOnly = true)
	public boolean isValid(BusinessAttribute businessAttribute, String[] values) {
		Assert.notNull(businessAttribute, "notNull");
		Assert.notNull(businessAttribute.getType(), "notNull");

		String value = ArrayUtils.isNotEmpty(values) ? values[0].trim() : null;
		switch (businessAttribute.getType()) {
		case name:
		case licenseNumber:
		case licenseImage:
		case legalPerson:
		case idCard:
		case idCardImage:
		case phone:
		case organizationCode:
		case organizationImage:
		case identificationNumber:
		case taxImage:
		case bankName:
		case bankAccount:
		case text:
		case image:
		case date:
			if (businessAttribute.getIsRequired() && StringUtils.isEmpty(value)) {
				return false;
			}
			if (StringUtils.isNotEmpty(businessAttribute.getPattern()) && StringUtils.isNotEmpty(value) && !Pattern.compile(businessAttribute.getPattern()).matcher(value).matches()) {
				return false;
			}
			break;
		case select:
			if (businessAttribute.getIsRequired() && StringUtils.isEmpty(value)) {
				return false;
			}
			if (CollectionUtils.isEmpty(businessAttribute.getOptions())) {
				return false;
			}
			if (StringUtils.isNotEmpty(value) && !businessAttribute.getOptions().contains(value)) {
				return false;
			}
			break;
		case checkbox:
			if (businessAttribute.getIsRequired() && ArrayUtils.isEmpty(values)) {
				return false;
			}
			if (CollectionUtils.isEmpty(businessAttribute.getOptions())) {
				return false;
			}
			if (ArrayUtils.isNotEmpty(values) && !businessAttribute.getOptions().containsAll(Arrays.asList(values))) {
				return false;
			}
			break;
		}
		return true;
	}

	@Transactional(readOnly = true)
	public Object toBusinessAttributeValue(BusinessAttribute businessAttribute, String[] values) {
		Assert.notNull(businessAttribute, "notNull");
		Assert.notNull(businessAttribute.getType(), "notNull");

		if (ArrayUtils.isEmpty(values)) {
			return null;
		}

		String value = values[0].trim();
		switch (businessAttribute.getType()) {
		case name:
		case licenseNumber:
		case licenseImage:
		case legalPerson:
		case idCard:
		case idCardImage:
		case phone:
		case organizationCode:
		case organizationImage:
		case identificationNumber:
		case taxImage:
		case bankName:
		case bankAccount:
		case text:
		case image:
		case date:
			return StringUtils.isNotEmpty(value) ? value : null;
		case select:
			if (CollectionUtils.isNotEmpty(businessAttribute.getOptions()) && businessAttribute.getOptions().contains(value)) {
				return value;
			}
			break;
		case checkbox:
			if (CollectionUtils.isNotEmpty(businessAttribute.getOptions()) && businessAttribute.getOptions().containsAll(Arrays.asList(values))) {
				return Arrays.asList(values);
			}
			break;
		}
		return null;
	}

	@Override
	@Transactional
	@CacheEvict(value = "businessAttribute", allEntries = true)
	public BusinessAttribute save(BusinessAttribute businessAttribute) {
		Assert.notNull(businessAttribute, "notNull");

		Integer unusedPropertyIndex = findUnusedPropertyIndex();
		Assert.notNull(unusedPropertyIndex, "notNull");

		businessAttribute.setPropertyIndex(unusedPropertyIndex);

		return super.save(businessAttribute);
	}

	@Override
	@Transactional
	@CacheEvict(value = "businessAttribute", allEntries = true)
	public BusinessAttribute update(BusinessAttribute businessAttribute) {
		return super.update(businessAttribute);
	}

	@Override
	@Transactional
	@CacheEvict(value = "businessAttribute", allEntries = true)
	public BusinessAttribute update(BusinessAttribute businessAttribute, String... ignoreProperties) {
		return super.update(businessAttribute, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "businessAttribute", allEntries = true)
	public void delete(Long id) {
		businessAttributeDao.deleteById(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "businessAttribute", allEntries = true)
	public void delete(Long... ids) {
		if (ids != null) {
			for (Long id : ids) {
				delete(id);
			}
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = "businessAttribute", allEntries = true)
	public void delete(BusinessAttribute businessAttribute) {
		if (businessAttribute == null || businessAttribute.getType() == null || businessAttribute.getPropertyIndex() == null) {
			return;
		}

		String propertyName;
		switch (businessAttribute.getType()) {
		case text:
		case select:
		case checkbox:
		case image:
		case date:
			propertyName = Business.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + businessAttribute.getPropertyIndex();
			break;
		default:
			propertyName = String.valueOf(businessAttribute.getType());
			break;
		}
		
		if (propertyName != null) {
			propertyName = net.shopec.util.StringUtils.camel2Underline(propertyName);
			businessAttributeDao.clearAttributeValue(propertyName);
		}
		
		super.delete(businessAttribute);
	}

}