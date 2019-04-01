package net.shopec.dao;

import java.util.Date;
import java.util.List;

import net.shopec.entity.Article;
import net.shopec.entity.ArticleCategory;
import net.shopec.entity.ArticleTag;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 文章
 * 
 */
public interface ArticleDao extends BaseDao<Article> {

	/**
	 * 查找文章
	 * 
	 * @param articleCategory
	 *            文章分类
	 * @param articleTag
	 *            文章标签
	 * @param isPublication
	 *            是否发布
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 文章
	 */
	List<Article> findList(@Param("ew") Wrapper<Article> wrapper, @Param("articleCategory")ArticleCategory articleCategory, @Param("articleTag")ArticleTag articleTag, @Param("isPublication")Boolean isPublication);

	/**
	 * 查找文章
	 * 
	 * @param articleCategory
	 *            文章分类
	 * @param isPublication
	 *            是否发布
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @return 文章
	 */
	List<Article> findList(ArticleCategory articleCategory, Boolean isPublication, Date beginDate, Date endDate, Integer first, Integer count);

	/**
	 * 查找文章分页
	 * 
	 * @param articleCategory
	 *            文章分类
	 * @param articleTag
	 *            文章标签
	 * @param isPublication
	 *            是否发布
	 * @param pageable
	 *            分页信息
	 * @return 文章分页
	 */
	List<Article> findPage(RowBounds rowBounds, @Param("ew")Wrapper<Article> wrapper, @Param("articleCategory")ArticleCategory articleCategory, @Param("articleTag")ArticleTag articleTag, @Param("isPublication")Boolean isPublication);

}