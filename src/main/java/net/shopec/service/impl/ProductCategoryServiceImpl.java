package net.shopec.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.dao.ProductCategoryBrandDao;
import net.shopec.dao.ProductCategoryDao;
import net.shopec.dao.ProductCategoryPromotionDao;
import net.shopec.entity.Brand;
import net.shopec.entity.ProductCategory;
import net.shopec.entity.ProductCategoryBrand;
import net.shopec.entity.ProductCategoryPromotion;
import net.shopec.entity.Promotion;
import net.shopec.entity.Store;
import net.shopec.service.ProductCategoryService;

/**
 * Service - 商品分类
 * 
 */
@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategory> implements ProductCategoryService {

	@Inject
	private ProductCategoryDao productCategoryDao;
	@Inject
	private ProductCategoryBrandDao productCategoryBrandDao;
	@Inject
	private ProductCategoryPromotionDao productCategoryPromotionDao;

	@Transactional(readOnly = true)
	public List<ProductCategory> findList(Store store, Integer count, List<Filter> filters, List<Order> orders) {
		EntityWrapper<ProductCategory> wrapper = toWrapper(null, count, filters, orders);
		return productCategoryDao.findList(store, wrapper);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findRoots() {
		return productCategoryDao.findRoots(null);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findRoots(Integer count) {
		return productCategoryDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "productCategory", condition = "#useCache")
	public List<ProductCategory> findRoots(Integer count, boolean useCache) {
		return productCategoryDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findParents(ProductCategory productCategory, boolean recursive, Integer count) {
		return productCategoryDao.findParents(productCategory, recursive, count);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "productCategory", condition = "#useCache")
	public List<ProductCategory> findParents(Long productCategoryId, boolean recursive, Integer count, boolean useCache) {
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		return productCategoryDao.findParents(productCategory, recursive, count);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findTree() {
		List<ProductCategory> productCategories = productCategoryDao.findChildren(null, true, null);
		sort(productCategories);
		return productCategories;
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findChildren(ProductCategory productCategory, boolean recursive, Integer count) {
		List<ProductCategory> productCategories = productCategoryDao.findChildren(productCategory, recursive, count);
		sort(productCategories);
		return productCategories;
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "productCategory", condition = "#useCache")
	public List<ProductCategory> findChildren(Long productCategoryId, boolean recursive, Integer count, boolean useCache) {
		ProductCategory productCategory = productCategoryDao.find(productCategoryId);
		if (productCategoryId != null && productCategory == null) {
			return Collections.emptyList();
		}
		List<ProductCategory> productCategories = productCategoryDao.findChildren(productCategory, recursive, count);
		sort(productCategories);
		return productCategories;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public ProductCategory save(ProductCategory productCategory) {
		Assert.notNull(productCategory, "notNull");

		setValue(productCategory);
		super.save(productCategory);
		setProductCategoryBrand(productCategory);
		setProductCategoryPromotion(productCategory);
		return productCategory;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public ProductCategory update(ProductCategory productCategory) {
		Assert.notNull(productCategory, "notNull");

		setValue(productCategory);
		List<ProductCategory> productCategories = productCategoryDao.findChildren(productCategory, true, null);
		sort(productCategories);
		for (ProductCategory children : productCategories) {
			setValue(children);
		}
		setProductCategoryBrand(productCategory);
		setProductCategoryPromotion(productCategory);
		return super.update(productCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public ProductCategory update(ProductCategory productCategory, String... ignoreProperties) {
		setProductCategoryBrand(productCategory);
		setProductCategoryPromotion(productCategory);
		return super.update(productCategory, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public void delete(Long id) {
		// 分类品牌中间表
		EntityWrapper<ProductCategoryBrand> brandWrapper = new EntityWrapper<ProductCategoryBrand>();
		brandWrapper.where("product_categories_id = {0}", id);
		productCategoryBrandDao.delete(brandWrapper);
		
		// 分类促销中间表
		EntityWrapper<ProductCategoryPromotion> promotionWrapper = new EntityWrapper<ProductCategoryPromotion>();
		promotionWrapper.where("product_categories_id = {0}", id);
		productCategoryPromotionDao.delete(promotionWrapper);
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public void delete(Long... ids) {
		if (ids != null) {
			for (Long id : ids) {
				delete(id);
			}
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public void delete(ProductCategory productCategory) {
		super.delete(productCategory);
	}

	/**
	 * 设置商品分类与品牌中间表
	 * 
	 * @param product
	 *            商品
	 */
	private void setProductCategoryBrand(ProductCategory productCategory) {
		EntityWrapper<ProductCategoryBrand> wrapper = new EntityWrapper<ProductCategoryBrand>();
		wrapper.where("product_categories_id = {0}", productCategory.getId());
		productCategoryBrandDao.delete(wrapper);
		List<ProductCategoryBrand> productCategoryBrands = new ArrayList<ProductCategoryBrand>();
		for (Brand brand : productCategory.getBrands()) {
			ProductCategoryBrand productCategoryBrand = new ProductCategoryBrand();
			productCategoryBrand.setBrandsId(brand.getId());
			productCategoryBrand.setProductCategoriesId(productCategory.getId());
			productCategoryBrands.add(productCategoryBrand);
		}
		if (CollectionUtils.isNotEmpty(productCategoryBrands)) {
			productCategoryBrandDao.batchSave(productCategoryBrands);
		}
	}
	
	/**
	 * 设置商品分类与促销中间表
	 * 
	 * @param product
	 *            商品
	 */
	private void setProductCategoryPromotion(ProductCategory productCategory) {
		 EntityWrapper<ProductCategoryPromotion> wrapper = new EntityWrapper<ProductCategoryPromotion>();
			wrapper.where("product_categories_id = {0}", productCategory.getId());
			productCategoryPromotionDao.delete(wrapper);
			List<ProductCategoryPromotion> productCategoryPromotions = new ArrayList<ProductCategoryPromotion>();
			for (Promotion promotion : productCategory.getPromotions()) {
				ProductCategoryPromotion productCategoryPromotion = new ProductCategoryPromotion();
				productCategoryPromotion.setProductCategoriesId(productCategory.getId());
				productCategoryPromotion.setPromotionsId(promotion.getId());
				productCategoryPromotions.add(productCategoryPromotion);
			}
			if (CollectionUtils.isNotEmpty(productCategoryPromotions)) {
				productCategoryPromotionDao.batchSave(productCategoryPromotions);
			}
	}
	
	/**
	 * 设置值
	 * 
	 * @param productCategory
	 *            商品分类
	 */
	private void setValue(ProductCategory productCategory) {
		if (productCategory == null) {
			return;
		}
		ProductCategory parent = productCategory.getParent();
		if (parent != null) {
			productCategory.setTreePath(parent.getTreePath() + parent.getId() + ProductCategory.TREE_PATH_SEPARATOR);
		} else {
			productCategory.setTreePath(ProductCategory.TREE_PATH_SEPARATOR);
		}
		productCategory.setGrade(productCategory.getParentIds().length);
	}

	/**
	 * 排序商品分类
	 * 
	 * @param productCategories
	 *            商品分类
	 */
	private void sort(List<ProductCategory> productCategories) {
		if (CollectionUtils.isEmpty(productCategories)) {
			return;
		}
		final Map<Long, Integer> orderMap = new HashMap<>();
		for (ProductCategory productCategory : productCategories) {
			orderMap.put(productCategory.getId(), productCategory.getOrder());
		}
		Collections.sort(productCategories, new Comparator<ProductCategory>() {
			@Override
			public int compare(ProductCategory productCategory1, ProductCategory productCategory2) {
				Long[] ids1 = (Long[]) ArrayUtils.add(productCategory1.getParentIds(), productCategory1.getId());
				Long[] ids2 = (Long[]) ArrayUtils.add(productCategory2.getParentIds(), productCategory2.getId());
				Iterator<Long> iterator1 = Arrays.asList(ids1).iterator();
				Iterator<Long> iterator2 = Arrays.asList(ids2).iterator();
				CompareToBuilder compareToBuilder = new CompareToBuilder();
				while (iterator1.hasNext() && iterator2.hasNext()) {
					Long id1 = iterator1.next();
					Long id2 = iterator2.next();
					Integer order1 = orderMap.get(id1);
					Integer order2 = orderMap.get(id2);
					compareToBuilder.append(order1, order2).append(id1, id2);
					if (!iterator1.hasNext() || !iterator2.hasNext()) {
						compareToBuilder.append(productCategory1.getGrade(), productCategory2.getGrade());
					}
				}
				return compareToBuilder.toComparison();
			}
		});
	}
}