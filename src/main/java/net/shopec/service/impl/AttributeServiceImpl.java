package net.shopec.service.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.dao.AttributeDao;
import net.shopec.dao.ProductCategoryDao;
import net.shopec.dao.ProductDao;
import net.shopec.entity.Attribute;
import net.shopec.entity.Product;
import net.shopec.entity.ProductCategory;
import net.shopec.service.AttributeService;

/**
 * Service - 属性
 * 
 */
@Service
public class AttributeServiceImpl extends BaseServiceImpl<Attribute> implements AttributeService {

	@Inject
	private AttributeDao attributeDao;
	@Inject
	private ProductCategoryDao productCategoryDao;
	@Inject
	private ProductDao productDao;

	@Transactional(readOnly = true)
	public Integer findUnusedPropertyIndex(ProductCategory productCategory) {
		Assert.notNull(productCategory, "ProductCategory Not Null");

		for (int i = 0; i < Product.ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
			Integer count = attributeDao.findUnusedPropertyIndex( productCategory, i);
			if (count == 0) {
				return i;
			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	public List<Attribute> findList(ProductCategory productCategory, Integer count, List<Filter> filters, List<Order> orders) {
		EntityWrapper<Attribute> wrapper = toWrapper(null, count, filters, orders);
		return attributeDao.findList(wrapper, productCategory);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "attribute", condition = "#useCache")
	public List<Attribute> findList(Long productCategoryId, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		EntityWrapper<Attribute> wrapper = toWrapper(null, count, filters, orders);
		return attributeDao.findList(wrapper, productCategory);
	}

	@Transactional(readOnly = true)
	public String toAttributeValue(Attribute attribute, String value) {
		Assert.notNull(attribute, "notNull");

		if (StringUtils.isEmpty(value) || CollectionUtils.isEmpty(attribute.getOptions()) || !attribute.getOptions().contains(value)) {
			return null;
		}
		return value;
	}

	@Override
	@Transactional
	@CacheEvict(value = "attribute", allEntries = true)
	public Attribute save(Attribute attribute) {
		Assert.notNull(attribute, "notNull");

		Integer unusedPropertyIndex = attributeDao.findUnusedPropertyIndex(attribute.getProductCategory(), null);
		Assert.notNull(unusedPropertyIndex, "notNull");

		attribute.setPropertyIndex(unusedPropertyIndex);
		return super.save(attribute);
	}

	@Override
	@Transactional
	@CacheEvict(value = "attribute", allEntries = true)
	public Attribute update(Attribute attribute) {
		return super.update(attribute);
	}

	@Override
	@Transactional
	@CacheEvict(value = "attribute", allEntries = true)
	public Attribute update(Attribute attribute, String... ignoreProperties) {
		return super.update(attribute, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "attribute", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "attribute", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "attribute", allEntries = true)
	public void delete(Attribute attribute) {
		if (attribute != null && attribute.getPropertyIndex() != null && attribute.getProductCategory() != null) {
			String propertyName  = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex();
			productDao.clearAttributeValue(attribute, net.shopec.util.StringUtils.camel2Underline(propertyName));
		}

		super.delete(attribute);
	}

}