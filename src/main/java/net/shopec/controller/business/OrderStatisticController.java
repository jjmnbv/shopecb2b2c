package net.shopec.controller.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.shopec.entity.Statistic;
import net.shopec.entity.Store;
import net.shopec.security.CurrentStore;
import net.shopec.service.StatisticService;

/**
 * Controller - 订单统计
 * 
 */
@Controller("businessOrderStatisticController")
@RequestMapping("/business/order_statistic")
public class OrderStatisticController extends BaseController {

	/**
	 * 默认类型
	 */
	private static final Statistic.Type DEFAULT_TYPE = Statistic.Type.createOrderCount;

	/**
	 * 默认周期
	 */
	private static final Statistic.Period DEFAULT_PERIOD = Statistic.Period.day;

	@Inject
	private StatisticService statisticService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Model model) {
		List<Statistic.Type> types = new ArrayList<>();
		types.add(Statistic.Type.createOrderCount);
		types.add(Statistic.Type.completeOrderCount);
		types.add(Statistic.Type.createOrderAmount);
		types.add(Statistic.Type.completeOrderAmount);
		model.addAttribute("types", types);
		model.addAttribute("type", DEFAULT_TYPE);
		model.addAttribute("periods", Statistic.Period.values());
		model.addAttribute("period", DEFAULT_PERIOD);
		model.addAttribute("beginDate", DateUtils.addMonths(new Date(), -1));
		model.addAttribute("endDate", new Date());
		return "business/order_statistic/list";
	}

	/**
	 * 数据
	 */
	@GetMapping("/data")
	public ResponseEntity<?> data(Statistic.Type type, Statistic.Period period, Date beginDate, Date endDate, @CurrentStore Store currentStore) {
		if (type == null) {
			type = DEFAULT_TYPE;
		}
		if (period == null) {
			period = DEFAULT_PERIOD;
		}
		if (beginDate == null) {
			switch (period) {
			case year:
				beginDate = DateUtils.addYears(new Date(), -10);
				break;
			case month:
				beginDate = DateUtils.addYears(new Date(), -1);
				break;
			case day:
				beginDate = DateUtils.addMonths(new Date(), -1);
				break;
			}
		}
		if (endDate == null) {
			endDate = new Date();
		}
		Calendar beginCalendar = DateUtils.toCalendar(beginDate);
		Calendar endCalendar = DateUtils.toCalendar(endDate);
		switch (period) {
		case year:
			beginCalendar.set(Calendar.MONTH, beginCalendar.getActualMinimum(Calendar.MONTH));
			beginCalendar.set(Calendar.DAY_OF_MONTH, beginCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			endCalendar.set(Calendar.MONTH, endCalendar.getActualMaximum(Calendar.MONTH));
			endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			break;
		case month:
			beginCalendar.set(Calendar.DAY_OF_MONTH, beginCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			break;
		case day:
		}
		return ResponseEntity.ok(statisticService.analyze(type, currentStore, period, beginCalendar.getTime(), endCalendar.getTime()));
	}

}