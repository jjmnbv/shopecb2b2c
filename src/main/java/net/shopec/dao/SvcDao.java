package net.shopec.dao;

import java.util.List;

import net.shopec.Order;
import net.shopec.entity.Store;
import net.shopec.entity.StoreRank;
import net.shopec.entity.Svc;

/**
 * Dao - 服务
 * 
 */
public interface SvcDao extends BaseDao<Svc> {

	/**
	 * 查找服务
	 * 
	 * @param store
	 *            店铺
	 * @param promotionPluginId
	 *            促销插件Id
	 * @param storeRank
	 *            店铺等级
	 * @param orders
	 *            排序
	 * @return 服务
	 */
	List<Svc> find(Store store, String promotionPluginId, StoreRank storeRank, List<Order> orders);

}