package net.shopec.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.shopec.entity.Statistic;
import net.shopec.entity.Store;

/**
 * Dao - 统计
 * 
 */
public interface StatisticDao extends BaseDao<Statistic> {

	/**
	 * 判断统计是否存在
	 * 
	 * @param type
	 *            类型
	 * @param store
	 *            店铺
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return 统计是否存在
	 */
	boolean exists(@Param("type")Statistic.Type type, @Param("store")Store store, @Param("year")int year, @Param("month")int month, @Param("day")int day);

	/**
	 * 分析
	 * 
	 * @param type
	 *            类型
	 * @param store
	 *            店铺
	 * @param period
	 *            周期
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 统计
	 */
	List<Statistic> analyze(@Param("type")Statistic.Type type, @Param("store")Store store, @Param("period")Statistic.Period period, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate);

}