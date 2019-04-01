package net.shopec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.shopec.entity.AdminRole;

/**
 * <p>
 * 管理员角色中间表 Mapper 接口
 * </p>
 *
 * @author 江南红衣
 * @since 2018-04-19
 */
public interface AdminRoleDao extends BaseDao<AdminRole> {

	/**
	 * 批量保存
	 * @param adminRoles
	 * @return
	 */
	int batchSave(@Param("adminRoles")List<AdminRole> adminRoles);
	
}
