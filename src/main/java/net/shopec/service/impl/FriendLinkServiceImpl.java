package net.shopec.service.impl;

import java.util.List;

import javax.inject.Inject;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.dao.FriendLinkDao;
import net.shopec.entity.FriendLink;
import net.shopec.service.FriendLinkService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 友情链接
 * 
 */
@Service
public class FriendLinkServiceImpl extends BaseServiceImpl<FriendLink> implements FriendLinkService {

	@Inject
	private FriendLinkDao friendLinkDao;

	@Transactional(readOnly = true)
	public List<FriendLink> findList(FriendLink.Type type) {
		return friendLinkDao.findList(type);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "friendLink", condition = "#useCache")
	public List<FriendLink> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return super.findList(null, count, filters, orders);
	}

	@Override
	@Transactional
	@CacheEvict(value = "friendLink", allEntries = true)
	public FriendLink save(FriendLink friendLink) {
		return super.save(friendLink);
	}

	@Override
	@Transactional
	@CacheEvict(value = "friendLink", allEntries = true)
	public FriendLink update(FriendLink friendLink) {
		return super.update(friendLink);
	}

	@Override
	@Transactional
	@CacheEvict(value = "friendLink", allEntries = true)
	public FriendLink update(FriendLink friendLink, String... ignoreProperties) {
		return super.update(friendLink, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "friendLink", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "friendLink", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "friendLink", allEntries = true)
	public void delete(FriendLink friendLink) {
		super.delete(friendLink);
	}

}