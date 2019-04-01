package net.shopec.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.mapper.ISqlInjector;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;

@Configuration
@MapperScan("net.shopec.dao")
public class MybatisPlusConfig {

	/**
	 * 乐观锁插件
	 */
	@Bean
	public OptimisticLockerInterceptor optimisticLockerInterceptor() {
		return new OptimisticLockerInterceptor();
	}

	/**
	 * 分页插件
	 * 
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		// paginationInterceptor.setLocalPage(true); // 开启 PageHelper 的支持
		paginationInterceptor.setDialectType(DBType.MYSQL.getDb());
		return paginationInterceptor;
	}

	/**
	 * 注入sql注入器
	 */
	@Bean
	public ISqlInjector sqlInjector() {
		return new LogicSqlInjector();
	}

	/*@Bean
	public GlobalConfiguration globalConfiguration() {
		GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
		conf.setLogicDeleteValue("-1");
		conf.setLogicNotDeleteValue("1");
		conf.setMetaObjectHandler(new MyMetaObjectHandler());
		return conf;
	}*/
}