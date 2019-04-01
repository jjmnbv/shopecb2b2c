package net.shopec;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * <p>
 * 代码生成器
 * </p>
 *
 * @author 江南红衣
 * @since 2018-04-12
 */
public class GeneratorCode {

	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost:3306/shopecb2b2c?useUnicode=true&characterEncoding=UTF-8";
	private static final String username = "root";
	private static final String password = "278421";
	private static final String packageName = "net.shopec";
	private static final String[] tables = new String[]{"order_returns_item"};

	/**
	 * 代生成Main方法
	 * @param args
	 */
    public static void main(String[] args) {
    	
		// 数据库配置
        DataSourceConfig dbConfig = new DataSourceConfig();
        dbConfig.setUrl(url);
        dbConfig.setUsername(username);
        dbConfig.setPassword(password);
        dbConfig.setDriverName(driver);
        
        // 策略配置项
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true);
        strategyConfig.setEntityLombokModel(false);
        strategyConfig.setDbColumnUnderline(true);
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setInclude(tables);//修改替换成你需要的表名，多个表名传数组
        strategyConfig.setSuperEntityClass("net.shopec.entity.BaseEntity");
        strategyConfig.setSuperEntityColumns(new String[] { "id", "createdDate", "lastModifiedDate", "version" });
        strategyConfig.setSuperMapperClass("net.shopec.dao.BaseDao");
        strategyConfig.setSuperServiceClass("net.shopec.service.BaseService");
        strategyConfig.setSuperServiceImplClass("net.shopec.service.BaseServiceImpl");
        strategyConfig.setSuperControllerClass("net.shopec.controller.admin.BaseController");
        
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setActiveRecord(false);
        gc.setAuthor("江南红衣");
        gc.setOutputDir("/Users/lihongyuan/code");
        gc.setFileOverride(true);
        gc.setServiceName("%sService");
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        gc.setMapperName("%sDao");
        gc.setXmlName("%sDao");
        
        // 跟包相关的配置项
        PackageConfig packageConfig =  new PackageConfig();
        packageConfig.setParent(packageName);
        packageConfig.setController("controller");
        packageConfig.setEntity("entity");
        packageConfig.setMapper("dao");
        
        // 生成文件
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(gc);
		autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        autoGenerator.setDataSource(dbConfig);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setPackageInfo(packageConfig).execute();
    }

}