package net.shopec.service.impl;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.CategoryApplicationDao;
import net.shopec.dao.ProductCategoryStoreDao;
import net.shopec.dao.ProductDao;
import net.shopec.entity.CategoryApplication;
import net.shopec.entity.ProductCategory;
import net.shopec.entity.ProductCategoryStore;
import net.shopec.entity.Store;
import net.shopec.service.CategoryApplicationService;

/**
 * Service - 经营分类申请
 * 
 */
@Service
public class CategoryApplicationServiceImpl extends BaseServiceImpl<CategoryApplication> implements CategoryApplicationService {

	@Inject
	private CategoryApplicationDao categoryApplicationDao;
	@Inject
	private ProductDao productDao;
	@Inject
	private ProductCategoryStoreDao productCategoryStoreDao;
	
	@Override
	public CategoryApplication save(CategoryApplication categoryApplication) {
		return super.save(categoryApplication);
	}
	
	@Transactional(readOnly = true)
	public boolean exist(Store store, ProductCategory productCategory, CategoryApplication.Status status) {
		Assert.notNull(status, "notNull");
		Assert.notNull(store, "notNull");
		Assert.notNull(productCategory, "notNull");

		return categoryApplicationDao.findList(store, productCategory, status).size() > 0;
	}

	@Transactional(readOnly = true)
	public Page<CategoryApplication> findPage(Store store, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<CategoryApplication> plusPage = getPlusPage(pageable);
		plusPage.setRecords(categoryApplicationDao.findPage(plusPage, getWrapper(pageable), store));
		return super.findPage(plusPage, pageable);
	}

	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public void review(CategoryApplication categoryApplication, boolean isPassed) {
		Assert.notNull(categoryApplication, "notNull");

		if (isPassed) {
			Store store = categoryApplication.getStore();
			ProductCategory productCategory = categoryApplication.getProductCategory();

			categoryApplication.setStatus(CategoryApplication.Status.approved);
			store.getProductCategories().add(productCategory);
			Set<ProductCategory> productCategories = new HashSet<>();
			productCategories.add(productCategory);
			
			ProductCategoryStore productCategoryStore = new ProductCategoryStore();
			productCategoryStore.setProductCategoriesId(productCategory.getId());
			productCategoryStore.setStoresId(store.getId());
			productCategoryStoreDao.save(productCategoryStore);
			productDao.refreshActive(store);
		} else {
			categoryApplication.setStatus(CategoryApplication.Status.failed);
		}
		categoryApplicationDao.updateById(categoryApplication);
	}

}