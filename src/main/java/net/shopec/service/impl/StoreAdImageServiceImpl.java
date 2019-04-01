package net.shopec.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.StoreAdImageDao;
import net.shopec.entity.Store;
import net.shopec.entity.StoreAdImage;
import net.shopec.service.StoreAdImageService;

/**
 * Service - 店铺广告图片
 * 
 */
@Service
public class StoreAdImageServiceImpl extends BaseServiceImpl<StoreAdImage> implements StoreAdImageService {

	@Inject
	private StoreAdImageDao storeAdImageDao;

	@Transactional(readOnly = true)
	public Page<StoreAdImage> findPage(Store store, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<StoreAdImage> plusPage = getPlusPage(pageable);
		plusPage.setRecords(storeAdImageDao.findPage(plusPage, getWrapper(pageable), store));
		return super.findPage(plusPage, pageable);
	}

	@Override
	public StoreAdImage save(StoreAdImage storeAdImage) {
		return super.save(storeAdImage);
	}
	
}