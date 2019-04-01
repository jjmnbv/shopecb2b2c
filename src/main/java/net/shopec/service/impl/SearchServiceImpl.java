package net.shopec.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.Article;
import net.shopec.entity.Product;
import net.shopec.entity.Sku;
import net.shopec.entity.Store;
import net.shopec.repository.ArticleRepository;
import net.shopec.repository.ProductRepository;
import net.shopec.repository.StoreRepository;
import net.shopec.service.ArticleService;
import net.shopec.service.ProductService;
import net.shopec.service.SearchService;
import net.shopec.service.SkuService;
import net.shopec.service.StoreService;

/**
 * Service - 搜索
 * 
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class SearchServiceImpl implements SearchService {

	@Inject
	private ArticleRepository articleRepository;
	@Inject
	private StoreRepository storeRepository;
	@Inject
	private ProductRepository productRepository;
	@Inject
	private ArticleService articleService;
	@Inject
	private StoreService storeService;
	@Inject
	private ProductService productService;
	@Inject
	private SkuService skuService;

	public void index(Class<?> type) {
		index(type, true);
	}

	public void index(Class<?> type, boolean purgeAll) {
		Assert.notNull(type, "NotNull");

		if (type.isAssignableFrom(Article.class)) {
			List<Article> articles = articleService.findAll();
			articleRepository.saveAll(articles);
		} else if (type.isAssignableFrom(Product.class)) {
			List<Product> products = new ArrayList<>();
			for (Product product : productService.findAll()) {
				Product persistant = productService.find(product.getId());
				Store store = storeService.selectById(persistant.getStore().getId());
				
				EntityWrapper<Sku> wrapper = new EntityWrapper<>();
				wrapper.where("product_id = {0}", product.getId());
				Set<Sku> skus = new HashSet<>(skuService.selectList(wrapper));
				
				product.setStore(store);
				product.setSkus(skus);
				product.setProductImages(persistant.getProductImages());
				products.add(product);
			}
			productRepository.saveAll(products);
		} else if (type.isAssignableFrom(Store.class)) {
			List<Store> stores = storeService.findAll();
			storeRepository.saveAll(stores);
		}
	}

	public Page<Article> search(String keyword, Pageable pageable) {
		if (StringUtils.isEmpty(keyword)) {
			return new Page<>();
		}

		if (pageable == null) {
			pageable = new Pageable();
		}

		//多条件设置
		MatchPhraseQueryBuilder titleQuery = QueryBuilders.matchPhraseQuery("title", keyword).boost(1.5F);
		MatchPhraseQueryBuilder contentQuery = QueryBuilders.matchPhraseQuery("content", keyword);
		MatchPhraseQueryBuilder isPublicationQuery = QueryBuilders.matchPhraseQuery("isPublication", true);
		BoolQueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.boolQuery().should(titleQuery).should(contentQuery)).must(isPublicationQuery);
        
		Sort sort = Sort.by(Direction.DESC, "isTop");
		org.springframework.data.domain.Page<Article> articles = articleRepository.search(query, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort));
		return new Page<>(articles.getContent(), articles.getSize(), pageable);
	}

	public Page<Product> search(String keyword, Store store, BigDecimal startPrice, BigDecimal endPrice, Product.OrderType orderType, Pageable pageable) {
		if (StringUtils.isEmpty(keyword)) {
			return new Page<>();
		}

		if (pageable == null) {
			pageable = new Pageable();
		}

		//多条件设置
		QueryBuilder snQuery = QueryBuilders.matchPhraseQuery("sn", keyword);
		QueryBuilder nameQuery = QueryBuilders.matchQuery("name", keyword).boost(1.5F);
		QueryBuilder keywordQuery = QueryBuilders.matchQuery("keyword", keyword).boost(1.5F);
		QueryBuilder introductionQuery = QueryBuilders.matchQuery("introduction", keyword);
		MatchPhraseQueryBuilder isMarketableQuery = QueryBuilders.matchPhraseQuery("isMarketable", true);
		MatchPhraseQueryBuilder isListQuery = QueryBuilders.matchPhraseQuery("isList", true);
		MatchPhraseQueryBuilder isActiveQuery = QueryBuilders.matchPhraseQuery("isActive", true);
		BoolQueryBuilder booleanJunction = QueryBuilders.boolQuery().must(QueryBuilders.boolQuery().should(snQuery).should(nameQuery).should(keywordQuery).should(introductionQuery)).must(isMarketableQuery).must(isListQuery).must(isActiveQuery);
		
		if (store != null) {
			MatchPhraseQueryBuilder storeQuery = QueryBuilders.matchPhraseQuery("store.id", String.valueOf(store.getId()));
			booleanJunction = booleanJunction.must(storeQuery);
		}
		if (startPrice != null && endPrice != null) {
			RangeQueryBuilder priceQuery = QueryBuilders.rangeQuery("price").from(startPrice.doubleValue()).to(endPrice.doubleValue());
			booleanJunction = booleanJunction.must(priceQuery);
		} else if (startPrice != null) {
			RangeQueryBuilder priceQuery = QueryBuilders.rangeQuery("price").gt(startPrice.doubleValue());
			booleanJunction = booleanJunction.must(priceQuery);
		} else if (endPrice != null) {
			RangeQueryBuilder priceQuery = QueryBuilders.rangeQuery("price").lt(endPrice.doubleValue());
			booleanJunction = booleanJunction.must(priceQuery);
		}
		
		Sort sort = null;
		if (orderType != null) {
			switch (orderType) {
			case topDesc:
				sort = Sort.by(Direction.DESC, "isTop");
				break;
			case priceAsc:
				sort = Sort.by(Direction.ASC, "price");
				break;
			case priceDesc:
				sort = Sort.by(Direction.DESC, "price");
				break;
			case salesDesc:
				sort = Sort.by(Direction.DESC, "sales");
				break;
			case scoreDesc:
				sort = Sort.by(Direction.DESC, "score");
				break;
			case dateDesc:
				sort = Sort.by(Direction.DESC, "createdDate");
				break;
			}
		} else {
			sort = Sort.by(Direction.DESC, "isTop");
		}
		org.springframework.data.domain.Page<Product> products = productRepository.search(booleanJunction, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort));
		return new Page<>(products.getContent(), products.getSize(), pageable);
	}

	public List<Store> searchStore(String keyword) {
		if (StringUtils.isEmpty(keyword)) {
			return new ArrayList<>();
		}

		//多条件设置
		MatchPhraseQueryBuilder nameQuery = QueryBuilders.matchPhraseQuery("name", keyword).boost(1.5F);
		MatchPhraseQueryBuilder keywordQuery = QueryBuilders.matchPhraseQuery("keyword", keyword);
		MatchPhraseQueryBuilder statusQuery = QueryBuilders.matchPhraseQuery("status", String.valueOf(Store.Status.success));
		MatchPhraseQueryBuilder isEnabledQuery = QueryBuilders.matchPhraseQuery("isEnabled", true);
		BoolQueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.boolQuery().should(nameQuery).should(keywordQuery)).must(statusQuery).must(isEnabledQuery);
				
		Iterator<Store> iterator = storeRepository.search(query).iterator();
		if (iterator != null) {
			List<Store> list = new ArrayList<Store>();
			while (iterator.hasNext()) {
				list.add(iterator.next());
			}
			return list;
		}
		return null;
	}
	
}