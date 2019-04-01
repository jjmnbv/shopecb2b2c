package net.shopec.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.shopec.entity.Attribute;
import net.shopec.entity.Brand;
import net.shopec.entity.Product;
import net.shopec.entity.ProductCategory;
import net.shopec.entity.ProductTag;
import net.shopec.entity.Promotion;
import net.shopec.entity.Store;
import net.shopec.entity.StoreProductCategory;
import net.shopec.entity.StoreProductTag;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 商品
 * 
 */
public interface ProductDao extends BaseDao<Product> {

	/**
	 * 查找商品
	 * 
	 * @param type
	 *            类型
	 * @param store
	 *            店铺
	 * @param productCategory
	 *            商品分类
	 * @param storeProductCategory
	 *            店铺商品分类
	 * @param brand
	 *            品牌
	 * @param promotion
	 *            促销
	 * @param productTag
	 *            商品标签
	 * @param storeProductTag
	 *            属性值Map
	 * @param isMarketable
	 *            是否上架
	 * @param isList
	 *            是否列出
	 * @param isTop
	 *            是否置顶
	 * @param isActive
	 *            是否有效
	 * @param isOutOfStock
	 *            是否缺货
	 * @param isStockAlert
	 *            是否库存警告
	 * @param hasPromotion
	 *            是否存在促销
	 * @param orderType
	 *            排序类型
	 * @return 商品
	 */
	List<Product> findList(@Param("ew") Wrapper<Product> wrapper, @Param("type")Product.Type type, @Param("store")Store store, @Param("productCategory")ProductCategory productCategory, @Param("storeProductCategory")StoreProductCategory storeProductCategory, @Param("brand")Brand brand, @Param("promotion")Promotion promotion, @Param("productTag")ProductTag productTag, @Param("storeProductTag")StoreProductTag storeProductTag, @Param("startPrice")BigDecimal startPrice,
			@Param("endPrice")BigDecimal endPrice, @Param("isMarketable")Boolean isMarketable, @Param("isList")Boolean isList, @Param("isTop")Boolean isTop, @Param("isActive")Boolean isActive, @Param("isOutOfStock")Boolean isOutOfStock, @Param("isStockAlert")Boolean isStockAlert, @Param("hasPromotion")Boolean hasPromotion, @Param("orderType")Product.OrderType orderType);

	/**
	 * 查找商品
	 * 
	 * @param productCategory
	 *            商品分类
	 * @param storeProductCategory
	 *            店铺商品分类
	 * @param isMarketable
	 *            是否上架
	 * @param isActive
	 *            是否有效
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @return 商品
	 */
	List<Product> findByCategoryList(@Param("productCategory")ProductCategory productCategory, @Param("storeProductCategory")StoreProductCategory storeProductCategory, @Param("isMarketable")Boolean isMarketable, @Param("isActive")Boolean isActive, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate, @Param("first")Integer first, @Param("count")Integer count);

	/**
	 * 查找商品
	 * 
	 * @param rankingType
	 *            排名类型
	 * @param store
	 *            店铺
	 * @param count
	 *            数量
	 * @return 商品
	 */
	List<Product> findByRankingTypeList(@Param("rankingType")Product.RankingType rankingType, @Param("store")Store store, @Param("count")Integer count);

	/**
	 * 查找商品分页
	 * 
	 * @param type
	 *            类型
	 * @param store
	 *            店铺
	 * @param productCategory
	 *            商品分类
	 * @param storeProductCategory
	 *            店铺商品分类
	 * @param brand
	 *            品牌
	 * @param promotion
	 *            促销
	 * @param productTag
	 *            商品标签
	 * @param storeProductTag
	 *            店铺商品标签
	 * @param startPrice
	 *            最低价格
	 * @param endPrice
	 *            最高价格
	 * @param isMarketable
	 *            是否上架
	 * @param isList
	 *            是否列出
	 * @param isTop
	 *            是否置顶
	 * @param isActive
	 *            是否有效
	 * @param isOutOfStock
	 *            是否缺货
	 * @param isStockAlert
	 *            是否库存警告
	 * @param hasPromotion
	 *            是否存在促销
	 * @param orderType
	 *            排序类型
	 * @param pageable
	 *            分页信息
	 * @return 商品分页
	 */
	List<Product> findPage(RowBounds rowBounds, @Param("ew")Wrapper<Product> wrapper, @Param("type")Product.Type type, @Param("store")Store store, @Param("productCategory")ProductCategory productCategory, @Param("storeProductCategory")StoreProductCategory storeProductCategory, @Param("brand")Brand brand, @Param("promotion")Promotion promotion, @Param("productTag")ProductTag productTag, @Param("storeProductTag")StoreProductTag storeProductTag, @Param("startPrice")BigDecimal startPrice,
			@Param("endPrice")BigDecimal endPrice, @Param("isMarketable")Boolean isMarketable, @Param("isList")Boolean isList, @Param("isTop")Boolean isTop, @Param("isActive")Boolean isActive, @Param("isOutOfStock")Boolean isOutOfStock, @Param("isStockAlert")Boolean isStockAlert, @Param("hasPromotion")Boolean hasPromotion, @Param("orderType")Product.OrderType orderType);

	/**
	 * 查询商品数量
	 * 
	 * @param type
	 *            类型
	 * @param store
	 *            店铺
	 * @param isMarketable
	 *            是否上架
	 * @param isList
	 *            是否列出
	 * @param isTop
	 *            是否置顶
	 * @param isActive
	 *            是否有效
	 * @param isOutOfStock
	 *            是否缺货
	 * @param isStockAlert
	 *            是否库存警告
	 * @return 商品数量
	 */
	Long count(@Param("type")Product.Type type, @Param("store")Store store, @Param("isMarketable")Boolean isMarketable, @Param("isList")Boolean isList, @Param("isTop")Boolean isTop, @Param("isActive")Boolean isActive, @Param("isOutOfStock")Boolean isOutOfStock, @Param("isStockAlert")Boolean isStockAlert);

	/**
	 * 清除商品属性值
	 * 
	 * @param attribute
	 *            属性
	 */
	void clearAttributeValue(@Param("attribute")Attribute attribute, @Param("propertyName")String propertyName);

	/**
	 * 刷新过期店铺商品有效状态
	 */
	void refreshExpiredStoreProductActive();

	/**
	 * 刷新商品有效状态
	 * 
	 * @param store
	 *            店铺
	 */
	void refreshActive(@Param("store")Store store);

}