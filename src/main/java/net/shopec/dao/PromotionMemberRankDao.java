package net.shopec.dao;

import java.util.List;

import net.shopec.entity.PromotionMemberRank;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 促销会员等级中间表 Mapper 接口
 * </p>
 *
 * @author 江南红衣
 * @since 2018-05-16
 */
public interface PromotionMemberRankDao extends BaseDao<PromotionMemberRank> {

	/**
	 * 批量保存
	 * @param promotionMemberRanks
	 * @return
	 */
	int batchSave(@Param("promotionMemberRanks")List<PromotionMemberRank> promotionMemberRanks);
}
