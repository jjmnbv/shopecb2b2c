package net.shopec.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.baomidou.mybatisplus.enums.IEnum;

/**
 * Entity - 友情链接
 * 
 */
public class FriendLink extends OrderedEntity<FriendLink> {

	private static final long serialVersionUID = 3019642557500517628L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum {

		/**
		 * 文本
		 */
		text(0),

		/**
		 * 图片
		 */
		image(1);

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
	 * 名称
	 */
	private String name;

	/**
	 * 类型
	 */
	private FriendLink.Type type;

	/**
	 * logo
	 */
	private String logo;

	/**
	 * 链接地址
	 */
	private String url;

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public FriendLink.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(FriendLink.Type type) {
		this.type = type;
	}

	/**
	 * 获取logo
	 * 
	 * @return logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * 设置logo
	 * 
	 * @param logo
	 *            logo
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * 获取链接地址
	 * 
	 * @return 链接地址
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置链接地址
	 * 
	 * @param url
	 *            链接地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int compareTo(FriendLink o) {
		if (o == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), o.getOrder()).append(getId(), o.getId()).toComparison();
	}

}