package net.shopec.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.dao.StoreRankDao;
import net.shopec.entity.StoreRank;
import net.shopec.service.StoreRankService;

/**
 * Service - 店铺等级
 * 
 */
@Service
public class StoreRankServiceImpl extends BaseServiceImpl<StoreRank> implements StoreRankService {

	@Inject
	private StoreRankDao storeRankDao;

	@Transactional(readOnly = true)
	public boolean nameExists(String name) {
		return storeRankDao.exists("name", name);
	}

	@Transactional(readOnly = true)
	public boolean nameUnique(Long id, String name) {
		return storeRankDao.unique(id, "name", name);
	}

	@Transactional(readOnly = true)
	public List<StoreRank> findList(Boolean isAllowRegister, List<Filter> filters, List<Order> orders) {
		EntityWrapper<StoreRank> wrapper = toWrapper(null, null, filters, orders);
		return storeRankDao.findList(wrapper, isAllowRegister);
	}

	@Override
	public StoreRank save(StoreRank storeRank) {
		return super.save(storeRank);
	}
	
	@Override
	public StoreRank update(StoreRank storeRank) {
		return super.update(storeRank);
	}
	
	@Override
	@Transactional
	public StoreRank update(StoreRank storeRank, String... ignoreProperties) {
		return super.update(storeRank, ignoreProperties);
	}
	
}