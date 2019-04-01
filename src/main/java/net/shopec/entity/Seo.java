package net.shopec.entity;

import java.io.IOException;
import java.io.Serializable;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.enums.IEnum;

import net.shopec.util.FreeMarkerUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateException;

/**
 * Entity - SEO设置
 * 
 */
public class Seo extends BaseEntity<Seo> {

	private static final long serialVersionUID = -3503657242384822672L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum {

		/**
		 * 首页
		 */
		index(0),

		/**
		 * 文章列表
		 */
		articleList(1),

		/**
		 * 文章搜索
		 */
		articleSearch(2),

		/**
		 * 文章详情
		 */
		articleDetail(3),

		/**
		 * 商品列表
		 */
		productList(4),

		/**
		 * 商品搜索
		 */
		productSearch(5),

		/**
		 * 商品详情
		 */
		productDetail(6),

		/**
		 * 品牌列表
		 */
		brandList(7),

		/**
		 * 品牌详情
		 */
		brandDetail(8),

		/**
		 * 店铺首页
		 */
		storeIndex(9),

		/**
		 * 店铺搜索
		 */
		storeSearch(10);
		
		private int value;

		Type(final int value) {
			this.value = value;
		}
		
		@Override
		public Serializable getValue() {
			return this.value;
		}
	}

	/**
	 * 类型
	 */
	private Seo.Type type;

	/**
	 * 页面标题
	 */
	private String title;

	/**
	 * 页面关键词
	 */
	private String keywords;

	/**
	 * 页面描述
	 */
	private String description;

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public Seo.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Seo.Type type) {
		this.type = type;
	}

	/**
	 * 获取页面标题
	 * 
	 * @return 页面标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置页面标题
	 * 
	 * @param title
	 *            页面标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取页面关键词
	 * 
	 * @return 页面关键词
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * 设置页面关键词
	 * 
	 * @param keywords
	 *            页面关键词
	 */
	public void setKeywords(String keywords) {
		if (keywords != null) {
			keywords = keywords.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
		}
		this.keywords = keywords;
	}

	/**
	 * 获取页面描述
	 * 
	 * @return 页面描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置页面描述
	 * 
	 * @param description
	 *            页面描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 解析页面标题
	 * 
	 * @return 页面标题
	 */
	@Transient
	public String resolveTitle() {
		try {
			Environment environment = FreeMarkerUtils.getCurrentEnvironment();
			return FreeMarkerUtils.process(getTitle(), environment != null ? environment.getDataModel() : null);
		} catch (IOException e) {
			return null;
		} catch (TemplateException e) {
			return null;
		}
	}

	/**
	 * 解析页面关键词
	 * 
	 * @return 页面关键词
	 */
	@Transient
	public String resolveKeywords() {
		try {
			Environment environment = FreeMarkerUtils.getCurrentEnvironment();
			return FreeMarkerUtils.process(getKeywords(), environment != null ? environment.getDataModel() : null);
		} catch (IOException e) {
			return null;
		} catch (TemplateException e) {
			return null;
		}
	}

	/**
	 * 解析页面描述
	 * 
	 * @return 页面描述
	 */
	@Transient
	public String resolveDescription() {
		try {
			Environment environment = FreeMarkerUtils.getCurrentEnvironment();
			return FreeMarkerUtils.process(getDescription(), environment != null ? environment.getDataModel() : null);
		} catch (IOException e) {
			return null;
		} catch (TemplateException e) {
			return null;
		}
	}

}