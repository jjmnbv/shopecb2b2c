package net.shopec.entity;

import java.io.Serializable;

/**
 * <p>
 * 文章文章标签中间表
 * </p>
 *
 * @author 江南红衣
 * @since 2018-05-21
 */
public class ArticleArticleTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章
     */
    private Long articlesId;

    /**
     * 文章标签
     */
    private Long articleTagsId;

    public Long getArticlesId() {
        return articlesId;
    }

    public void setArticlesId(Long articlesId) {
        this.articlesId = articlesId;
    }
    public Long getArticleTagsId() {
        return articleTagsId;
    }

    public void setArticleTagsId(Long articleTagsId) {
        this.articleTagsId = articleTagsId;
    }

    @Override
    public String toString() {
        return "ArticleArticleTag{" +
        "articlesId=" + articlesId +
        ", articleTagsId=" + articleTagsId +
        "}";
    }
}
