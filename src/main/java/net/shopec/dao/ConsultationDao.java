package net.shopec.dao;

import java.util.List;

import net.shopec.entity.Consultation;
import net.shopec.entity.Member;
import net.shopec.entity.Product;
import net.shopec.entity.Store;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 咨询
 * 
 */
public interface ConsultationDao extends BaseDao<Consultation> {

	/**
	 * 查找咨询
	 * 
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param isShow
	 *            是否显示
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 咨询，不包含咨询回复
	 */
	List<Consultation> findList(@Param("ew") Wrapper<Consultation> wrapper, @Param("member")Member member, @Param("product")Product product, @Param("isShow")Boolean isShow);

	/**
	 * 查找咨询分页
	 * 
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param store
	 *            店铺
	 * @param isShow
	 *            是否显示
	 * @param pageable
	 *            分页信息
	 * @return 咨询分页，不包含咨询回复
	 */
	List<Consultation> findPage(RowBounds rowBounds, @Param("ew")Wrapper<Consultation> wrapper, @Param("member")Member member, @Param("product")Product product, @Param("store")Store store, @Param("isShow")Boolean isShow);

	/**
	 * 查找咨询数量
	 * 
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param isShow
	 *            是否显示
	 * @return 咨询数量，不包含咨询回复
	 */
	Long count(@Param("member")Member member, @Param("product")Product product, @Param("isShow")Boolean isShow);

}