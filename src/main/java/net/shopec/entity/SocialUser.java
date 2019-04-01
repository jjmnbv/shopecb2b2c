package net.shopec.entity;

import com.baomidou.mybatisplus.annotations.TableField;


/**
 * Entity - 社会化用户
 * 
 */
public class SocialUser extends BaseEntity<SocialUser> {

	private static final long serialVersionUID = 3962190474369671955L;

	/**
	 * 登录插件ID
	 */
	private String loginPluginId;

	/**
	 * 唯一ID
	 */
	private String uniqueId;

	/**
	 * 用户
	 */
	@TableField(exist = false)
	private Member member;

	/**
	 * 获取登录插件ID
	 * 
	 * @return 登录插件ID
	 */

	public String getLoginPluginId() {
		return loginPluginId;
	}

	/**
	 * 设置登录插件ID
	 * 
	 * @param loginPluginId
	 *            登录插件ID
	 */
	public void setLoginPluginId(String loginPluginId) {
		this.loginPluginId = loginPluginId;
	}

	/**
	 * 获取唯一ID
	 * 
	 * @return 唯一ID
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * 设置唯一ID
	 * 
	 * @param uniqueId
	 *            唯一ID
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * 获取用户
	 * 
	 * @return 用户
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * 设置用户
	 * 
	 * @param user
	 *            用户
	 */
	public void setMember(Member member) {
		this.member = member;
	}

}