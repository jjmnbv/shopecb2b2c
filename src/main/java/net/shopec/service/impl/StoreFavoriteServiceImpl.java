package net.shopec.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.StoreFavoriteDao;
import net.shopec.entity.Member;
import net.shopec.entity.Store;
import net.shopec.entity.StoreFavorite;
import net.shopec.service.StoreFavoriteService;

/**
 * Service - 店铺收藏
 * 
 */
@Service
public class StoreFavoriteServiceImpl extends BaseServiceImpl<StoreFavorite> implements StoreFavoriteService {

	@Inject
	private StoreFavoriteDao storeFavoriteDao;

	@Transactional(readOnly = true)
	public boolean exists(Member member, Store store) {
		return storeFavoriteDao.exists(member, store);
	}

	@Transactional(readOnly = true)
	public List<StoreFavorite> findList(Member member, Integer count, List<Filter> filters, List<Order> orders) {
		EntityWrapper<StoreFavorite> wrapper = toWrapper(null, count, filters, orders);
		return storeFavoriteDao.findList(wrapper, member);
	}

	@Transactional(readOnly = true)
	public Page<StoreFavorite> findPage(Member member, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<StoreFavorite> plusPage = getPlusPage(pageable);
		plusPage.setRecords(storeFavoriteDao.findPage(plusPage, getWrapper(pageable), member));
		return super.findPage(plusPage, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member) {
		return storeFavoriteDao.count(member);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "storeFavorite", condition = "#useCache")
	public List<StoreFavorite> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return super.findList(null, count, filters, orders);
	}

	@Override
	@CacheEvict(value = "storeFavorite", allEntries = true)
	public StoreFavorite save(StoreFavorite storeFavorite) {
		return super.save(storeFavorite);
	}

	@Override
	@CacheEvict(value = "storeFavorite", allEntries = true)
	public StoreFavorite update(StoreFavorite storeFavorite) {
		return super.update(storeFavorite);
	}

	@Override
	@CacheEvict(value = "storeFavorite", allEntries = true)
	public StoreFavorite update(StoreFavorite storeFavorite, String... ignoreProperties) {
		return super.update(storeFavorite, ignoreProperties);
	}

	@Override
	@CacheEvict(value = "storeFavorite", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@CacheEvict(value = "storeFavorite", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@CacheEvict(value = "storeFavorite", allEntries = true)
	public void delete(StoreFavorite storeFavorite) {
		super.delete(storeFavorite);
	}

}