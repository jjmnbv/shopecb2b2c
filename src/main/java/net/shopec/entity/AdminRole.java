package net.shopec.entity;

import java.io.Serializable;

/**
 * <p>
 * 管理员角色中间表
 * </p>
 *
 * @author 江南红衣
 * @since 2018-04-19
 */
public class AdminRole implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long adminsId;

	private Long rolesId;

	public Long getAdminsId() {
		return adminsId;
	}

	public void setAdminsId(Long adminsId) {
		this.adminsId = adminsId;
	}

	public Long getRolesId() {
		return rolesId;
	}

	public void setRolesId(Long rolesId) {
		this.rolesId = rolesId;
	}

	@Override
	public String toString() {
		return "AdminRole{" + "adminsId=" + adminsId + ", rolesId=" + rolesId
				+ "}";
	}
}
