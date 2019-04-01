package net.shopec.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.IdWorker;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.ProductFavoriteDao;
import net.shopec.entity.Member;
import net.shopec.entity.Product;
import net.shopec.entity.ProductFavorite;
import net.shopec.service.ProductFavoriteService;

/**
 * Service - 商品收藏
 * 
 */
@Service
public class ProductFavoriteServiceImpl extends BaseServiceImpl<ProductFavorite> implements ProductFavoriteService {

	@Inject
	private ProductFavoriteDao productFavoriteDao;

	@Override
	public ProductFavorite find(Long id) {
		return productFavoriteDao.find(id);
	}
	
	@Transactional(readOnly = true)
	public boolean exists(Member member, Product product) {
		return productFavoriteDao.exists(member, product);
	}

	@Transactional(readOnly = true)
	public List<ProductFavorite> findList(Member member, Integer count, List<Filter> filters, List<Order> orders) {
		EntityWrapper<ProductFavorite> wrapper = toWrapper(null, count, filters, orders);
		return productFavoriteDao.findList(wrapper, member);
	}

	@Transactional(readOnly = true)
	public Page<ProductFavorite> findPage(Member member, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<ProductFavorite> plusPage = getPlusPage(pageable);
		plusPage.setRecords(productFavoriteDao.findPage(plusPage, getWrapper(pageable), member));
		return super.findPage(plusPage, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member) {
		return productFavoriteDao.count(member);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "productFavorite", condition = "#useCache")
	public List<ProductFavorite> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return super.findList(null, count, filters, orders);
	}

	@Override
	@CacheEvict(value = "productFavorite", allEntries = true)
	public ProductFavorite save(ProductFavorite productFavorite) {
		productFavorite.setId(IdWorker.getId());
		productFavoriteDao.save(productFavorite);
		return productFavorite;
	}

	@Override
	@CacheEvict(value = "productFavorite", allEntries = true)
	public ProductFavorite update(ProductFavorite productFavorite) {
		return super.update(productFavorite);
	}

	@Override
	@CacheEvict(value = "productFavorite", allEntries = true)
	public ProductFavorite update(ProductFavorite productFavorite, String... ignoreProperties) {
		return super.update(productFavorite, ignoreProperties);
	}

	@Override
	@CacheEvict(value = "productFavorite", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@CacheEvict(value = "productFavorite", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@CacheEvict(value = "productFavorite", allEntries = true)
	public void delete(ProductFavorite productFavorite) {
		super.delete(productFavorite);
	}

}