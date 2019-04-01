package net.shopec.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.shopec.dao.MemberDao;
import net.shopec.dao.OrderDao;
import net.shopec.dao.StatisticDao;
import net.shopec.dao.StoreDao;
import net.shopec.entity.Statistic;
import net.shopec.entity.Store;
import net.shopec.service.StatisticService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

/**
 * Service - 统计
 * 
 */
@Service
public class StatisticServiceImpl extends BaseServiceImpl<Statistic> implements StatisticService {

	@Inject
	private StatisticDao statisticDao;
	@Inject
	private MemberDao memberDao;
	@Inject
	private OrderDao orderDao;
	@Inject
	private StoreDao storeDao;

	@Transactional(readOnly = true)
	public boolean exists(Statistic.Type type, Store store, int year, int month, int day) {
		return statisticDao.exists(type, store, year, month, day);
	}

	public void collect(int year, int month, int day) {
		for (Statistic.Type type : Statistic.Type.values()) {
			collect(type, null, year, month, day);
		}
		for (int i = 0;; i += 100) {
			EntityWrapper<Store> wrapper = new EntityWrapper<Store>();
			wrapper.last("LIMIT " + i + ", " + 100);
			List<Store> stores = storeDao.findList(wrapper, null, Store.Status.success, null, null);
			for (Store store : stores) {
				for (Statistic.Type type : Statistic.Type.values()) {
					if (!Statistic.Type.registerMemberCount.equals(type)) {
						collect(type, store, year, month, day);
					}
				}
			}
			if (stores.size() < 100) {
				break;
			}
		}
	}

	public void collect(Statistic.Type type, Store store, int year, int month, int day) {
		Assert.notNull(type, "notNull");
		Assert.state(month >= 0, "state");
		Assert.state(day >= 0, "state");

		if (Statistic.Type.registerMemberCount.equals(type)) {
			if (statisticDao.exists(type, null, year, month, day)) {
				return;
			}
		} else if (statisticDao.exists(type, store, year, month, day)) {
			return;
		}

		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.set(year, month, day);
		beginCalendar.set(Calendar.HOUR_OF_DAY, beginCalendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		beginCalendar.set(Calendar.MINUTE, beginCalendar.getActualMinimum(Calendar.MINUTE));
		beginCalendar.set(Calendar.SECOND, beginCalendar.getActualMinimum(Calendar.SECOND));
		Date beginDate = beginCalendar.getTime();

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(year, month, day);
		endCalendar.set(Calendar.HOUR_OF_DAY, beginCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		endCalendar.set(Calendar.MINUTE, beginCalendar.getActualMaximum(Calendar.MINUTE));
		endCalendar.set(Calendar.SECOND, beginCalendar.getActualMaximum(Calendar.SECOND));
		Date endDate = endCalendar.getTime();

		BigDecimal value = null;
		switch (type) {
		case registerMemberCount:
			value = new BigDecimal(memberDao.registerMemberCount(beginDate, endDate));
			break;
		case createOrderCount:
			value = new BigDecimal(orderDao.createOrderCount(store, beginDate, endDate));
			break;
		case completeOrderCount:
			value = new BigDecimal(orderDao.completeOrderCount(store, beginDate, endDate));
			break;
		case createOrderAmount:
			value = orderDao.createOrderAmount(store, beginDate, endDate);
			break;
		case completeOrderAmount:
			value = orderDao.completeOrderAmount(store, beginDate, endDate);
			break;
		}

		Statistic statistic = new Statistic();
		statistic.setType(type);
		statistic.setYear(year);
		statistic.setMonth(month);
		statistic.setDay(day);
		statistic.setValue(value);
		statistic.setStore(store);
		statisticDao.save(statistic);
	}

	@Transactional(readOnly = true)
	public List<Statistic> analyze(Statistic.Type type, Store store, Statistic.Period period, Date beginDate, Date endDate) {
		return statisticDao.analyze(type, store, period, beginDate, endDate);
	}

}