package net.shopec.service.impl;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.IdWorker;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.DefaultFreightConfigDao;
import net.shopec.entity.Area;
import net.shopec.entity.BaseEntity;
import net.shopec.entity.DefaultFreightConfig;
import net.shopec.entity.ShippingMethod;
import net.shopec.entity.Store;
import net.shopec.service.DefaultFreightConfigService;

/**
 * Service - 默认运费配置
 * 
 */
@Service
public class DefaultFreightConfigServiceImpl extends ServiceImpl<DefaultFreightConfigDao, DefaultFreightConfig> implements DefaultFreightConfigService {

	/**
	 * 更新忽略属性
	 */
	protected static final String[] UPDATE_IGNORE_PROPERTIES = new String[] { BaseEntity.CREATED_DATE_PROPERTY_NAME, BaseEntity.LAST_MODIFIED_DATE_PROPERTY_NAME, BaseEntity.VERSION_PROPERTY_NAME };
	
	@Inject
	private DefaultFreightConfigDao defaultFreightConfigDao;

	@Override
	public DefaultFreightConfig find(Long id) {
		return defaultFreightConfigDao.find(id);
	}
	
	@Transactional(readOnly = true)
	public boolean exists(ShippingMethod shippingMethod, Area area) {
		return defaultFreightConfigDao.exists(shippingMethod, area);
	}

	@Transactional(readOnly = true)
	public boolean unique(ShippingMethod shippingMethod, Area previousArea, Area currentArea) {
		if (previousArea != null && previousArea.equals(currentArea)) {
			return true;
		}
		return !defaultFreightConfigDao.exists(shippingMethod, currentArea);
	}

	@Transactional(readOnly = true)
	public Page<DefaultFreightConfig> findPage(Store store, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<DefaultFreightConfig> plusPage = new com.baomidou.mybatisplus.plugins.Page<DefaultFreightConfig>(pageable.getPageNumber(), pageable.getPageSize());
		EntityWrapper<DefaultFreightConfig> entityWrapper = new EntityWrapper<DefaultFreightConfig>();
		// 增加搜索属性、搜索值
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if (StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)) {
			entityWrapper.like(searchProperty, searchValue);
		}
		plusPage.setRecords(defaultFreightConfigDao.findPage(plusPage, entityWrapper, store));
		Page<DefaultFreightConfig> page = new Page<DefaultFreightConfig>(plusPage.getRecords(), plusPage.getTotal(), pageable);
		return page;
	}

	@Transactional(readOnly = true)
	public DefaultFreightConfig find(ShippingMethod shippingMethod, Store store) {
		return defaultFreightConfigDao.findDefault(shippingMethod, store);
	}

	public void update(DefaultFreightConfig defaultFreightConfig, Store store, ShippingMethod shippingMethod) {
		Assert.notNull(store, "NotNull");
		Assert.notNull(shippingMethod, "NotNull");
		if (!defaultFreightConfig.isNew()) {
			DefaultFreightConfig persistant = defaultFreightConfigDao.find(defaultFreightConfig.getId());
			if (persistant != null) {
				BeanUtils.copyProperties(defaultFreightConfig, persistant, UPDATE_IGNORE_PROPERTIES);
				defaultFreightConfigDao.update(persistant);
			}
		} else {
			defaultFreightConfig.setStore(store);
			defaultFreightConfig.setShippingMethod(shippingMethod);
			defaultFreightConfig.setId(IdWorker.getId());
			defaultFreightConfigDao.save(defaultFreightConfig);
		}
	}

}