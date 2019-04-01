package net.shopec.dao;

import java.util.List;

import net.shopec.entity.SocialUser;
import net.shopec.entity.User;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * Dao - 社会化用户
 * 
 */
public interface SocialUserDao extends BaseDao<SocialUser> {

	/**
	 * 查找社会化用户
	 * 
	 * @param loginPluginId
	 *            登录插件ID
	 * @param uniqueId
	 *            唯一ID
	 * @return 社会化用户，若不存在则返回null
	 */
	SocialUser findPlugin(@Param("loginPluginId")String loginPluginId, @Param("uniqueId")String uniqueId);

	/**
	 * 查找社会化用户分页
	 * 
	 * @param user
	 *            用户
	 * @param pageable
	 *            分页信息
	 * @return 社会化用户分页
	 */
	List<SocialUser> findPage(RowBounds rowBounds, @Param("ew")Wrapper<SocialUser> wrapper, @Param("user")User user);

}