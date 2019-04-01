package net.shopec.service.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.BaseDao;
import net.shopec.entity.BaseEntity;
import net.shopec.entity.OrderedEntity;
import net.shopec.service.BaseService;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.ResolvableType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.IdWorker;

@Transactional
public class BaseServiceImpl<T extends BaseEntity<T>> extends ServiceImpl<BaseMapper<T>, T> implements BaseService<T> {

	/**
	 * 更新忽略属性
	 */
	protected static final String[] UPDATE_IGNORE_PROPERTIES = new String[] { BaseEntity.CREATED_DATE_PROPERTY_NAME, BaseEntity.LAST_MODIFIED_DATE_PROPERTY_NAME, BaseEntity.VERSION_PROPERTY_NAME };

	/**
	 * 实体类类型
	 */
	private Class<T> entityClass;
	
	/**
	 * BaseDao
	 */
	private BaseDao<T> baseDao;

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		ResolvableType resolvableType = ResolvableType.forClass(getClass());
		entityClass = (Class<T>) resolvableType.as(BaseServiceImpl.class).getGeneric().resolve();
	}
	
	@Inject
	protected void setBaseDao(BaseDao<T> baseDao) {
		this.baseDao = baseDao;
	}
	
	@Override
	@Transactional(readOnly = true)
	public T find(Long id) {
		return baseDao.find(id);
	}

	@Override
	@Transactional(readOnly = true)
	public T find(String attributeName, Object attributeValue) {
		Assert.hasText(attributeName, "HasText");
		
		EntityWrapper<T> wrapper = new EntityWrapper<T>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(attributeName, attributeValue);
		wrapper.allEq(params);
		return selectOne(wrapper);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<T> findAll() {
		return baseDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findList(Long... ids) {
		List<T> result = new ArrayList<>();
		if (ids != null) {
			for (Long id : ids) {
				T entity = find(id);
				if (entity != null) {
					result.add(entity);
				}
			}
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findList(Integer count, List<Filter> filters, List<Order> orders) {
		return findList(null, count, filters, orders);
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders) {
		EntityWrapper<T> wrapper = toWrapper(first, count, filters, orders);
		return selectList(wrapper);
	}

	@Transactional(readOnly = true)
	public Page<T> findPage(Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<T> plusPage = selectPage(new com.baomidou.mybatisplus.plugins.Page<T>(pageable.getPageNumber(), pageable.getPageSize()), getWrapper(pageable));
        Page<T> page = new Page<T>(plusPage.getRecords(), plusPage.getTotal(), pageable);
		return page;
	}

	@Transactional(readOnly = true)
	public Page<T> findPage(com.baomidou.mybatisplus.plugins.Page<T> plusPage, Pageable pageable) {
		Page<T> page = new Page<T>(plusPage.getRecords(), plusPage.getTotal(), pageable);
		return page;
	}
	
	@Override
	@Transactional(readOnly = true)
	public long count() {
		return count(new Filter[] {});
	}

	@Override
	@Transactional(readOnly = true)
	public long count(Filter... filters) {
		EntityWrapper<T> wrapper = new EntityWrapper<T>();
		wrapper = toFilter(filters);
		return selectCount(wrapper);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean exists(Long id) {
		return selectById(id) != null;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean exists(Filter... filters) {
		EntityWrapper<T> wrapper = new EntityWrapper<T>();
		wrapper = toFilter(filters);
		return selectCount(wrapper) > 0;
	}

	@Override
	public T save(T entity) {
		Assert.notNull(entity, "NotNull");
		Assert.isTrue(entity.isNew(), "IsTrue");

		entity.setId(IdWorker.getId());
		entity.setCreatedDate(new Date());
		entity.setVersion(0L);
		baseDao.save(entity);
		return entity;
	}

	@Override
	public T update(T entity) {
		Assert.notNull(entity, "notNull");
		Assert.isTrue(!entity.isNew(), "isTrue");

		T persistant = selectById(entity.getId());
		if (persistant != null) {
			copyProperties(entity, persistant, UPDATE_IGNORE_PROPERTIES);
			persistant.setLastModifiedDate(new Date());
			baseDao.update(persistant);
		}
		return entity;
	}

	@Override
	@Transactional
	public T update(T entity, String... ignoreProperties) {
		Assert.notNull(entity, "notNull");
		Assert.isTrue(!entity.isNew(), "isTrue");

		T persistant = selectById(entity.getId());
		if (persistant != null) {
			copyProperties(entity, persistant, (String[]) ArrayUtils.addAll(ignoreProperties, UPDATE_IGNORE_PROPERTIES));
			persistant.setLastModifiedDate(new Date());
			baseDao.update(persistant);
		}
		return entity;
	}

	@Override
	@Transactional
	public void delete(Long id) {
		delete(selectById(id));
	}

	@Override
	@Transactional
	public void delete(Long... ids) {
		if (ids != null) {
			for (Long id : ids) {
				delete(selectById(id));
			}
		}
	}

	@Override
	@Transactional
	public void delete(T entity) {
		if (entity != null) {
			deleteById(entity.getId());
		}
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
	protected void copyProperties(T source, T target, String... ignoreProperties) {
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

	/**
	 * 条件组合
	 * 
	 */
	protected EntityWrapper<T> getWrapper(Pageable pageable) {
		EntityWrapper<T> entityWrapper = new EntityWrapper<T>();
		// 增加搜索属性、搜索值
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if (StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)) {
			entityWrapper.like(searchProperty, searchValue);
		}
		if (StringUtils.isNotEmpty(pageable.getOrderProperty())) {
			if (pageable.getOrderDirection().equals(Order.Direction.asc)) {
				entityWrapper.orderBy(true, pageable.getOrderProperty(), true);
			}
			if (pageable.getOrderDirection().equals(Order.Direction.desc)) {
				entityWrapper.orderBy(true, pageable.getOrderProperty(), false);
			}
		}
		// 解析Filter过滤条件
		if (CollectionUtils.isNotEmpty(pageable.getFilters())) {
			for (Filter filter : pageable.getFilters()) {
				entityWrapper = toFilter(filter);
			}
		}
		return entityWrapper;
	}
	
	/**
	 * 分页插件
	 * 
	 */
	protected com.baomidou.mybatisplus.plugins.Page<T> getPlusPage(Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<T> plusPage = new com.baomidou.mybatisplus.plugins.Page<T>(pageable.getPageNumber(), pageable.getPageSize());
		return plusPage;
	}
	
	/**
	 * 条件构造器
	 * 
	 * @return
	 */
	protected EntityWrapper<T> toWrapper(Integer first, Integer count, List<Filter> filters, List<Order> orders) {
		EntityWrapper<T> wrapper = new EntityWrapper<T>();
		// 筛选
		if (CollectionUtils.isNotEmpty(filters)) {
			for (Filter filter : filters) {
				if (filter == null) {
					continue;
				}
				String property = filter.getProperty();
				Filter.Operator operator = filter.getOperator();
				Object value = filter.getValue();
				Boolean ignoreCase = filter.getIgnoreCase();

				switch (operator) {
				case eq:
					if (value != null) {
						if (BooleanUtils.isTrue(ignoreCase) && value instanceof String) {
							wrapper.eq(property, ((String) value).toLowerCase());
						} else {
							wrapper.eq(property, value);
						}
					} else {
						wrapper.isNull(property);
					}
					break;
				case ne:
					if (value != null) {
						if (BooleanUtils.isTrue(ignoreCase) && value instanceof String) {
							wrapper.ne(property, ((String) value).toLowerCase());
						} else {
							wrapper.ne(property, value);
						}
					} else {
						wrapper.isNotNull(property);
					}
					break;
				case gt:
					if (value instanceof Number) {
						wrapper.gt(property, (Number) value);
					}
					break;
				case lt:
					if (value instanceof Number) {
						wrapper.lt(property, (Number) value);
					}
					break;
				case ge:
					if (value instanceof Number) {
						wrapper.ge(property, (Number) value);
					}
					break;
				case le:
					if (value instanceof Number) {
						wrapper.le(property, (Number) value);
					}
					break;
				case like:
					if (value instanceof String) {
						if (BooleanUtils.isTrue(ignoreCase)) {
							wrapper.like(property, ((String) value).toLowerCase());
						} else {
							wrapper.like(property, (String) value);
						}
					}
					break;
				case in:
					wrapper.in(property, (String) value);
					break;
				case isNull:
					wrapper.isNull(property);
					break;
				case isNotNull:
					wrapper.isNotNull(property);
					break;
				}
			}
		}
		
		// 排序
		if (CollectionUtils.isNotEmpty(orders)) {
			for (Order order : orders) {
				if (order == null) {
					continue;
				}
				List<String> columns = new ArrayList<String>();
				String property = order.getProperty();
				Order.Direction direction = order.getDirection();
				columns.add(property);
				switch (direction) {
				case asc:
					wrapper.orderAsc(columns);
					break;
				case desc:
					wrapper.orderDesc(columns);
					break;
				}
			}
		} else {
			List<String> columns = new ArrayList<String>();
			if (OrderedEntity.class.isAssignableFrom(entityClass)) {
				columns.add(OrderedEntity.ORDER_PROPERTY_NAME);
				wrapper.orderAsc(columns);
			} else {
				columns.add(net.shopec.util.StringUtils.camel2Underline(OrderedEntity.CREATED_DATE_PROPERTY_NAME));
				wrapper.orderAsc(columns);
			}
		}
		
		// 限制行数
		if (first != null && count != null) {
			wrapper.last(" LIMIT " + first +", " + count);
		}
		if (first == null && count != null) {
			wrapper.last(" LIMIT 0, " + count);
		}
		return wrapper;
	}
	
	
	/**
	 * 条件构造器
	 * @param filters
	 * @return
	 */
	protected EntityWrapper<T> toFilter(Filter... filters) {
		EntityWrapper<T> wrapper = new EntityWrapper<T>();
		// 筛选
		for (Filter filter : filters) {
			if (filter == null) {
				continue;
			}
			String property = filter.getProperty();
			Filter.Operator operator = filter.getOperator();
			Object value = filter.getValue();
			Boolean ignoreCase = filter.getIgnoreCase();

			switch (operator) {
			case eq:
				if (value != null) {
					if (BooleanUtils.isTrue(ignoreCase) && value instanceof String) {
						wrapper.eq(property, ((String) value).toLowerCase());
					} else {
						wrapper.eq(property, value);
					}
				} else {
					wrapper.isNull(property);
				}
				break;
			case ne:
				if (value != null) {
					if (BooleanUtils.isTrue(ignoreCase) && value instanceof String) {
						wrapper.ne(property, ((String) value).toLowerCase());
					} else {
						wrapper.ne(property, value);
					}
				} else {
					wrapper.isNotNull(property);
				}
				break;
			case gt:
				if (value instanceof Number) {
					wrapper.gt(property, (Number) value);
				}
				break;
			case lt:
				if (value instanceof Number) {
					wrapper.lt(property, (Number) value);
				}
				break;
			case ge:
				if (value instanceof Number) {
					wrapper.ge(property, (Number) value);
				}
				break;
			case le:
				if (value instanceof Number) {
					wrapper.le(property, (Number) value);
				}
				break;
			case like:
				if (value instanceof String) {
					if (BooleanUtils.isTrue(ignoreCase)) {
						wrapper.like(property, ((String) value).toLowerCase());
					} else {
						wrapper.like(property, (String) value);
					}
				}
				break;
			case in:
				wrapper.in(property, (String) value);
				break;
			case isNull:
				wrapper.isNull(property);
				break;
			case isNotNull:
				wrapper.isNotNull(property);
				break;
			}
		}
		return wrapper;
	}
	
	
	/**
	 * 转换为Order
	 * 
	 *            Root
	 * @param orders
	 *            排序
	 * @return Order
	 */
	protected EntityWrapper<T> toOrders(List<Order> orders) {
		EntityWrapper<T> wrapper = new EntityWrapper<T>();
		// 排序
		for (Order order : orders) {
			if (order == null) {
				continue;
			}
			List<String> columns = new ArrayList<String>();
			String property = order.getProperty();
			Order.Direction direction = order.getDirection();
			columns.add(property);
			switch (direction) {
			case asc:
				wrapper.orderAsc(columns);
				break;
			case desc:
				wrapper.orderDesc(columns);
				break;
			}
		}
		return wrapper;
	}
	
}
