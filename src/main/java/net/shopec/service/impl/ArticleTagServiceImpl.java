package net.shopec.service.impl;

import java.util.List;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.entity.ArticleTag;
import net.shopec.service.ArticleTagService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 文章标签
 * 
 */
@Service
public class ArticleTagServiceImpl extends BaseServiceImpl<ArticleTag> implements ArticleTagService {

	@Transactional(readOnly = true)
	@Cacheable(value = "articleTag", condition = "#useCache")
	public List<ArticleTag> findList(Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		return super.findList(count, filters, orders);
	}

	@Override
	@Transactional
	@CacheEvict(value = "articleTag", allEntries = true)
	public ArticleTag save(ArticleTag articleTag) {
		return super.save(articleTag);
	}

	@Override
	@Transactional
	@CacheEvict(value = "articleTag", allEntries = true)
	public ArticleTag update(ArticleTag articleTag) {
		return super.update(articleTag);
	}

	@Override
	@Transactional
	@CacheEvict(value = "articleTag", allEntries = true)
	public ArticleTag update(ArticleTag articleTag, String... ignoreProperties) {
		return super.update(articleTag, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "articleTag", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "articleTag", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "articleTag", allEntries = true)
	public void delete(ArticleTag articleTag) {
		super.delete(articleTag);
	}

}