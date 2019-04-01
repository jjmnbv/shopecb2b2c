package net.shopec.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.baomidou.mybatisplus.enums.IEnum;


/**
 * Entity - 导航
 * 
 */
public class Navigation extends OrderedEntity<Navigation> {

	private static final long serialVersionUID = -7635757647887646795L;

	/**
	 * 位置
	 */
	public enum Position implements IEnum {

		/**
		 * 顶部
		 */
		top(0),

		/**
		 * 中间
		 */
		middle(1),

		/**
		 * 底部
		 */
		bottom(2);

		private int value;

		Position(final int value) {
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
	 * 位置
	 */
	private Navigation.Position position;

	/**
	 * 链接地址
	 */
	private String url;

	/**
	 * 是否新窗口打开
	 */
	private Boolean isBlankTarget;

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
	 * 获取位置
	 * 
	 * @return 位置
	 */
	public Navigation.Position getPosition() {
		return position;
	}

	/**
	 * 设置位置
	 * 
	 * @param position
	 *            位置
	 */
	public void setPosition(Navigation.Position position) {
		this.position = position;
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

	/**
	 * 获取是否新窗口打开
	 * 
	 * @return 是否新窗口打开
	 */
	public Boolean getIsBlankTarget() {
		return isBlankTarget;
	}

	/**
	 * 设置是否新窗口打开
	 * 
	 * @param isBlankTarget
	 *            是否新窗口打开
	 */
	public void setIsBlankTarget(Boolean isBlankTarget) {
		this.isBlankTarget = isBlankTarget;
	}

	@Override
	public int compareTo(Navigation o) {
		if (o == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), o.getOrder()).append(getId(), o.getId()).toComparison();
	}

}