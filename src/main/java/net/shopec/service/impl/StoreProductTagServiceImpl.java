package net.shopec.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.StoreProductTagDao;
import net.shopec.entity.Store;
import net.shopec.entity.StoreProductTag;
import net.shopec.service.StoreProductTagService;

/**
 * Service - 店铺商品标签
 * 
 */
@Service
public class StoreProductTagServiceImpl extends BaseServiceImpl<StoreProductTag> implements StoreProductTagService {

	@Inject
	private StoreProductTagDao storeProductTagDao;

	@Transactional(readOnly = true)
	public List<StoreProductTag> findList(Store store, Boolean isEnabled) {
		return storeProductTagDao.findList(store, isEnabled);
	}

	@Transactional(readOnly = true)
	public Page<StoreProductTag> findPage(Store store, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<StoreProductTag> plusPage = getPlusPage(pageable);
		plusPage.setRecords(storeProductTagDao.findPage(plusPage, getWrapper(pageable), store));
		return super.findPage(plusPage, pageable);
	}

	@Override
	public StoreProductTag save(StoreProductTag storeProductTag) {
		return super.save(storeProductTag);
	}
	
	@Override
	public StoreProductTag update(StoreProductTag storeProductTag) {
		return super.update(storeProductTag);
	}
	
}