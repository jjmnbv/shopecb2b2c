package net.shopec.service.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.AreaFreightConfigDao;
import net.shopec.entity.Area;
import net.shopec.entity.AreaFreightConfig;
import net.shopec.entity.BaseEntity;
import net.shopec.entity.ShippingMethod;
import net.shopec.entity.Store;
import net.shopec.service.AreaFreightConfigService;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.IdWorker;

/**
 * Service - 地区运费配置
 * 
 */
@Service
public class AreaFreightConfigServiceImpl extends ServiceImpl<AreaFreightConfigDao, AreaFreightConfig> implements AreaFreightConfigService {

	/**
	 * 更新忽略属性
	 */
	protected static final String[] UPDATE_IGNORE_PROPERTIES = new String[] { BaseEntity.CREATED_DATE_PROPERTY_NAME, BaseEntity.LAST_MODIFIED_DATE_PROPERTY_NAME, BaseEntity.VERSION_PROPERTY_NAME };
	
	@Inject
	private AreaFreightConfigDao areaFreightConfigDao;

	@Override
	public AreaFreightConfig find(Long id) {
		return areaFreightConfigDao.find(id);
	}
	
	@Override
	public List<AreaFreightConfig> findList(Long... ids) {
		List<AreaFreightConfig> result = new ArrayList<>();
		if (ids != null) {
			for (Long id : ids) {
				AreaFreightConfig areaFreightConfig = this.find(id);
				if (areaFreightConfig != null) {
					result.add(areaFreightConfig);
				}
			}
		}
		return result;
	}
	
	@Override
	public void delete(Long... ids) {
		if (ids != null) {
			for (Long id : ids) {
				super.deleteById(id);
			}
		}
	}
	
	@Override
	public AreaFreightConfig save(AreaFreightConfig areaFreightConfig) {
		Assert.notNull(areaFreightConfig, "NotNull");
		Assert.isTrue(areaFreightConfig.isNew(), "IsTrue");
		
		areaFreightConfig.setId(IdWorker.getId());
		areaFreightConfigDao.save(areaFreightConfig);
		return areaFreightConfig;
	}

	@Override
	public AreaFreightConfig update(AreaFreightConfig areaFreightConfig) {
		Assert.notNull(areaFreightConfig, "notNull");
		Assert.isTrue(!areaFreightConfig.isNew(), "isTrue");

		AreaFreightConfig persistant = areaFreightConfigDao.find(areaFreightConfig.getId());
		if (persistant != null) {
			copyProperties(areaFreightConfig, persistant, UPDATE_IGNORE_PROPERTIES);
			areaFreightConfigDao.update(persistant);
		}
		return persistant;
	}

	@Transactional(readOnly = true)
	public boolean exists(ShippingMethod shippingMethod, Store store, Area area) {
		return areaFreightConfigDao.exists(shippingMethod, store, area);
	}

	@Transactional(readOnly = true)
	public boolean unique(Long id, ShippingMethod shippingMethod, Store store, Area area) {
		return areaFreightConfigDao.unique(id, shippingMethod, store, area);
	}

	@Transactional(readOnly = true)
	public Page<AreaFreightConfig> findPage(ShippingMethod shippingMethod, Store store, Pageable pageable) {
		EntityWrapper<AreaFreightConfig> wrapper = new EntityWrapper<AreaFreightConfig>();
		// 搜索属性、搜索值
		String searchProperty = net.shopec.util.StringUtils.camel2Underline(pageable.getSearchProperty());
		String searchValue = pageable.getSearchValue();
		if (StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)) {
			wrapper.like(searchProperty, searchValue);
		}
		com.baomidou.mybatisplus.plugins.Page<AreaFreightConfig> plusPage = new com.baomidou.mybatisplus.plugins.Page<AreaFreightConfig>(pageable.getPageNumber(), pageable.getPageSize());
		plusPage.setRecords(areaFreightConfigDao.findPage(plusPage, wrapper, shippingMethod, store));
		Page<AreaFreightConfig> page = new Page<AreaFreightConfig>(plusPage.getRecords(), plusPage.getTotal(), pageable);
		return page;
	}

	/**
	 * 拷贝对象属性
	 * 
	 * @param source
	 *            源
	 * @param target
	 *            目标
	 * @param ignoreProperties
	 *            忽略属性
	 */
	protected void copyProperties(AreaFreightConfig source, AreaFreightConfig target, String... ignoreProperties) {
		Assert.notNull(source, "NotNull");
		Assert.notNull(target, "NotNull");

		PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(target);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			String propertyName = propertyDescriptor.getName();
			Method readMethod = propertyDescriptor.getReadMethod();
			Method writeMethod = propertyDescriptor.getWriteMethod();
			if (ArrayUtils.contains(ignoreProperties, propertyName) || readMethod == null || writeMethod == null) {
				continue;
			}
			try {
				Object sourceValue = readMethod.invoke(source);
				writeMethod.invoke(target, sourceValue);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

}