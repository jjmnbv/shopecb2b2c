package net.shopec.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.Attribute;
import net.shopec.entity.Brand;
import net.shopec.entity.Product;
import net.shopec.entity.ProductCategory;
import net.shopec.entity.ProductTag;
import net.shopec.entity.Promotion;
import net.shopec.entity.Sku;
import net.shopec.entity.Store;
import net.shopec.entity.StoreProductCategory;
import net.shopec.entity.StoreProductTag;

/**
 * Service - 商品
 * 
 */
public interface ProductService extends BaseService<Product> {

	/**
	 * 判断编号是否存在
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 编号是否存在
	 */
	boolean snExists(String sn);

	/**
	 * 根据编号查找商品
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 商品，若不存在则返回null
	 */
	Product findBySn(String sn);

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
	 *            店铺商品标签
	 * @param attributeValueMap
	 *            属性值Map
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
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 商品
	 */
	List<Product> findList(Product.Type type, Store store, ProductCategory productCategory, StoreProductCategory storeProductCategory, Brand brand, Promotion promotion, ProductTag productTag, StoreProductTag storeProductTag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isActive, Boolean isOutOfStock, Boolean isStockAlert, Boolean hasPromotion, Product.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找商品
	 * 
	 * @param type
	 *            类型
	 * @param storeId
	 *            店铺ID
	 * @param productCategoryId
	 *            商品分类ID
	 * @param storeProductCategoryId
	 *            店铺商品分类ID
	 * @param brandId
	 *            品牌ID
	 * @param promotionId
	 *            促销ID
	 * @param productTagId
	 *            商品标签ID
	 * @param storeProductTagId
	 *            店铺商品标签ID
	 * @param attributeValueMap
	 *            属性值Map
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
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @param useCache
	 *            是否使用缓存
	 * @return 商品
	 */
	List<Product> findList(Product.Type type, Long storeId, Long productCategoryId, Long storeProductCategoryId, Long brandId, Long promotionId, Long productTagId, Long storeProductTagId, Map<Long, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable,
			Boolean isList, Boolean isTop, Boolean isActive, Boolean isOutOfStock, Boolean isStockAlert, Boolean hasPromotion, Product.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders, boolean useCache);

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
	List<Product> findList(Product.RankingType rankingType, Store store, Integer count);

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
	 * @param attributeValueMap
	 *            属性值Map
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
	Page<Product> findPage(Product.Type type, Store store, ProductCategory productCategory, StoreProductCategory storeProductCategory, Brand brand, Promotion promotion, ProductTag productTag, StoreProductTag storeProductTag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isActive, Boolean isOutOfStock, Boolean isStockAlert, Boolean hasPromotion, Product.OrderType orderType, Pageable pageable);

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
	Long count(Product.Type type, Store store, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isActive, Boolean isOutOfStock, Boolean isStockAlert);

	/**
	 * 查看点击数
	 * 
	 * @param id
	 *            ID
	 * @return 点击数
	 */
	long viewHits(Long id);

	/**
	 * 增加点击数
	 * 
	 * @param product
	 *            商品
	 * @param amount
	 *            值
	 */
	void addHits(Product product, long amount);

	/**
	 * 增加销量
	 * 
	 * @param product
	 *            商品
	 * @param amount
	 *            值
	 */
	void addSales(Product product, long amount);

	/**
	 * 创建
	 * 
	 * @param product
	 *            商品
	 * @param sku
	 *            SKU
	 * @return 商品
	 */
	Product create(Product product, Sku sku);

	/**
	 * 创建
	 * 
	 * @param product
	 *            商品
	 * @param skus
	 *            SKU
	 * @return 商品
	 */
	Product create(Product product, List<Sku> skus);

	/**
	 * 修改
	 * 
	 * @param product
	 *            商品
	 * @param sku
	 *            SKU
	 * @return 商品
	 */
	Product modify(Product product, Sku sku);

	/**
	 * 修改
	 * 
	 * @param product
	 *            商品
	 * @param skus
	 *            SKU
	 * @return 商品
	 */
	Product modify(Product product, List<Sku> skus);

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
	void refreshActive(Store store);

}