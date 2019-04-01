package net.shopec.dao;

import org.apache.ibatis.annotations.Param;

import net.shopec.entity.PluginConfig;

/**
 * Dao - 插件配置
 * 
 */
public interface PluginConfigDao extends BaseDao<PluginConfig> {

	/**
	 * 根据插件ID查找插件配置
	 * 
	 * @param pluginId
	 *            插件ID
	 * @return 插件配置，若不存在则返回null
	 */
	PluginConfig findByPluginId(@Param("pluginId")String pluginId);
	
}