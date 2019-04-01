package net.shopec.service.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.dao.BrandDao;
import net.shopec.dao.ProductCategoryDao;
import net.shopec.entity.Brand;
import net.shopec.entity.ProductCategory;
import net.shopec.service.BrandService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

/**
 * Service - 品牌
 * 
 */
@Service
public class BrandServiceImpl extends BaseServiceImpl<Brand> implements BrandService {

	@Inject
	private BrandDao brandDao;
	@Inject
	private ProductCategoryDao productCategoryDao;

	@Transactional(readOnly = true)
	public List<Brand> findList(ProductCategory productCategory, Integer count, List<Filter> filters, List<Order> orders) {
		EntityWrapper<Brand> wrapper = toWrapper(null, count, filters, orders);
		return brandDao.findList(wrapper, productCategory);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "brand", condition = "#useCache")
	public List<Brand> findList(Long productCategoryId, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		EntityWrapper<Brand> wrapper = toWrapper(null, count, filters, orders);
		return brandDao.findList(wrapper, productCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brand", allEntries = true)
	public Brand save(Brand brand) {
		return super.save(brand);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brand", allEntries = true)
	public Brand update(Brand brand) {
		return super.update(brand);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brand", allEntries = true)
	public Brand update(Brand brand, String... ignoreProperties) {
		return super.update(brand, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brand", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brand", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brand", allEntries = true)
	public void delete(Brand brand) {
		super.delete(brand);
	}

}