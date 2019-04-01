package net.shopec.entity;

import com.baomidou.mybatisplus.annotations.TableField;


/**
 * Entity - 地区运费配置
 * 
 */
public class AreaFreightConfig extends FreightConfig {

	private static final long serialVersionUID = -1648340171356447281L;

	/**
	 * 地区
	 */
	@TableField(exist = false)
	private Area area;

	/**
	 * 获取地区
	 * 
	 * @return 地区
	 */
	public Area getArea() {
		return area;
	}

	/**
	 * 设置地区
	 * 
	 * @param area
	 *            地区
	 */
	public void setArea(Area area) {
		this.area = area;
	}

}