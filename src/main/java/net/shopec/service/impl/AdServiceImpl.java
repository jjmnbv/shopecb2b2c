package net.shopec.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.shopec.entity.Ad;
import net.shopec.service.AdService;

/**
 * Service - 广告
 * 
 */
@Service
public class AdServiceImpl extends BaseServiceImpl<Ad> implements AdService {

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public Ad save(Ad ad) {
		return super.save(ad);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public Ad update(Ad ad) {
		return super.update(ad);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public Ad update(Ad ad, String... ignoreProperties) {
		return super.update(ad, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public void delete(Ad ad) {
		super.delete(ad);
	}

}