package net.shopec.service.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.ArticleCategoryDao;
import net.shopec.dao.ArticleDao;
import net.shopec.dao.ArticleTagDao;
import net.shopec.entity.Article;
import net.shopec.entity.ArticleCategory;
import net.shopec.entity.ArticleTag;
import net.shopec.service.ArticleService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

/**
 * Service - 文章
 * 
 */
@Service
public class ArticleServiceImpl extends BaseServiceImpl<Article> implements ArticleService {

	@Inject
	private CacheManager cacheManager;
	@Inject
	private ArticleDao articleDao;
	@Inject
	private ArticleCategoryDao articleCategoryDao;
	@Inject
	private ArticleTagDao articleTagDao;

	@Override
	public Article find(Long id) {
		return articleDao.find(id);
	}
	
	@Transactional(readOnly = true)
	public List<Article> findList(ArticleCategory articleCategory, ArticleTag articleTag, Boolean isPublication, Integer count, List<Filter> filters, List<Order> orders) {
		if (orders == null || orders.isEmpty()) {
			orders.add(new Order("is_top" , Order.Direction.desc));
			orders.add(new Order("created_date" , Order.Direction.desc));
		}
		EntityWrapper<Article> wrapper = toWrapper(null, count, filters, orders);
		return articleDao.findList(wrapper, articleCategory, articleTag, isPublication);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "article", condition = "#useCache")
	public List<Article> findList(Long articleCategoryId, Long articleTagId, Boolean isPublication, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		ArticleCategory articleCategory = articleCategoryDao.find(articleCategoryId);
		if (articleCategoryId != null && articleCategory == null) {
			return Collections.emptyList();
		}
		ArticleTag articleTag = articleTagDao.find(articleTagId);
		if (articleTagId != null && articleTag == null) {
			return Collections.emptyList();
		}
		if (orders == null || orders.isEmpty()) {
			orders.add(new Order("is_top" , Order.Direction.desc));
			orders.add(new Order("created_date" , Order.Direction.desc));
		}
		EntityWrapper<Article> wrapper = toWrapper(null, count, filters, orders);
		return articleDao.findList(wrapper, articleCategory, articleTag, isPublication);
	}

	@Transactional(readOnly = true)
	public Page<Article> findPage(ArticleCategory articleCategory, ArticleTag articleTag, Boolean isPublication, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<Article> plusPage = getPlusPage(pageable);
		plusPage.setRecords(articleDao.findPage(plusPage, getWrapper(pageable), articleCategory, articleTag, isPublication));
		return super.findPage(plusPage, pageable);
	}

	public long viewHits(Long id) {
		Assert.notNull(id, "notNull");

		Ehcache cache = cacheManager.getEhcache(Article.HITS_CACHE_NAME);
		cache.acquireWriteLockOnKey(id);
		try {
			Element element = cache.get(id);
			Long hits;
			if (element != null) {
				hits = (Long) element.getObjectValue() + 1;
			} else {
				Article article = articleDao.find(id);
				if (article == null) {
					return 0L;
				}
				hits = article.getHits() + 1;
			}
			cache.put(new Element(id, hits));
			return hits;
		} finally {
			cache.releaseWriteLockOnKey(id);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public Article save(Article article) {
		return super.save(article);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public Article update(Article article) {
		return super.update(article);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public Article update(Article article, String... ignoreProperties) {
		return super.update(article, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "article", "articleCategory" }, allEntries = true)
	public void delete(Article article) {
		super.delete(article);
	}

}