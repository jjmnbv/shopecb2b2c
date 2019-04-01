package net.shopec.service.impl;

import javax.inject.Inject;

import net.shopec.dao.StoreCategoryDao;
import net.shopec.entity.StoreCategory;
import net.shopec.service.StoreCategoryService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 店铺分类
 * 
 */
@Service
public class StoreCategoryServiceImpl extends BaseServiceImpl<StoreCategory> implements StoreCategoryService {

	@Inject
	private StoreCategoryDao storeCategoryDao;

	@Transactional(readOnly = true)
	public boolean nameExists(String name) {
		return storeCategoryDao.exists("name", name);
	}

}