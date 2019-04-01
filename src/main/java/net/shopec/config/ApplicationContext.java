package net.shopec.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.perf4j.log4j.aop.TimingAspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.jagregory.shiro.freemarker.ShiroTags;

import freemarker.template.utility.StandardCompress;
import net.sf.ehcache.event.CacheEventListener;
import net.shopec.Setting;
import net.shopec.captcha.CaptchaFilter;
import net.shopec.template.directive.AdPositionDirective;
import net.shopec.template.directive.ArticleCategoryChildrenListDirective;
import net.shopec.template.directive.ArticleCategoryParentListDirective;
import net.shopec.template.directive.ArticleCategoryRootListDirective;
import net.shopec.template.directive.ArticleListDirective;
import net.shopec.template.directive.ArticleTagListDirective;
import net.shopec.template.directive.AttributeListDirective;
import net.shopec.template.directive.BrandListDirective;
import net.shopec.template.directive.BusinessAttributeListDirective;
import net.shopec.template.directive.ConsultationListDirective;
import net.shopec.template.directive.FlashMessageDirective;
import net.shopec.template.directive.FriendLinkListDirective;
import net.shopec.template.directive.MemberAttributeListDirective;
import net.shopec.template.directive.NavigationListDirective;
import net.shopec.template.directive.OrderCountDirective;
import net.shopec.template.directive.PaginationDirective;
import net.shopec.template.directive.ProductCategoryChildrenListDirective;
import net.shopec.template.directive.ProductCategoryParentListDirective;
import net.shopec.template.directive.ProductCategoryRootListDirective;
import net.shopec.template.directive.ProductCountDirective;
import net.shopec.template.directive.ProductFavoriteDirective;
import net.shopec.template.directive.ProductListDirective;
import net.shopec.template.directive.ProductTagListDirective;
import net.shopec.template.directive.PromotionListDirective;
import net.shopec.template.directive.PromotionPluginDirective;
import net.shopec.template.directive.ReviewListDirective;
import net.shopec.template.directive.SeoDirective;
import net.shopec.template.directive.StoreFavoriteDirective;
import net.shopec.template.directive.StoreProductCategoryChildrenListDirective;
import net.shopec.template.directive.StoreProductCategoryParentListDirective;
import net.shopec.template.directive.StoreProductCategoryRootListDirective;
import net.shopec.template.method.AbbreviateMethod;
import net.shopec.template.method.CurrencyMethod;
import net.shopec.template.method.MessageMethod;
import net.shopec.util.SystemUtils;

@Configuration
@PropertySource("classpath:shopec.properties")
@EnableCaching
@ComponentScan(excludeFilters = {
		@org.springframework.context.annotation.ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Controller.class),
		@org.springframework.context.annotation.ComponentScan.Filter(type = FilterType.ANNOTATION, classes = ControllerAdvice.class) })
public class ApplicationContext {

	@Value("${template.datetime_format}")
	private String dateFormat;
	
	@Value("${template.time_format}")
	private String timeFormat;
	
	@Value("${template.datetime_format}")
	private String dateTimeFormat;
	
	@Value("${template.update_delay}")
	private String update_delay;
	
	@Value("${template.encoding}")
	private String encoding;
	
	@Value("${template.number_format}")
	private String numberFormat;
	
	@Value("${template.boolean_format}")
	private String booleanFormat;
	
	@Value("${template.loader_path}")
	private String templateLoaderPath;
	
	@Value("${url_escaping_charset}")
	private String urlEscapingCharset;

	@Value("${task.pool_size}")
	private int corePoolSize;
	
	@Value("${task.max_pool_size}")
	private int maxPoolSize;
	
	@Value("${task.queue_capacity}")
	private int queueCapacity;
	
	@Value("${message.cache_seconds}")
	private int cacheSeconds;
	
	@Value("${message.basenames}")
	private String basenames;
	
	@Value("${captcha.imageWidth}")
	private String imageWidth;
	
	@Value("${captcha.imageHeight}")
	private String imageHeight;
	
	@Value("${captcha.charString}")
	private String charString;
	
	@Value("${captcha.charLength}")
	private String charLength;
	
	@Value("${captcha.charSpace}")
	private String charSpace;
	
	@Value("${captcha.fontColor}")
	private String fontColor;
	
	@Value("${captcha.fontSize}")
	private String fontSize;
	
	@Value("${captcha.background_image_path}")
	private String background_image_path;
	
	@Inject
	private WebApplicationContext webApplicationContext;
	
	@Bean(name = "servletContext")
	public ServletContext servletContext() {
		return webApplicationContext.getServletContext();
	}
	
	@Bean(name = "ehCacheManager")
    public EhCacheManagerFactoryBean ehCacheManager() {
        EhCacheManagerFactoryBean ehCacheManager = new EhCacheManagerFactoryBean();
        ehCacheManager.setConfigLocation(new ClassPathResource("ehcache.xml"));
        ehCacheManager.setShared(true);
        return ehCacheManager;
    }
	
	@Primary
    @Bean(name = "cacheManager")
	public CacheManager ehCacheCacheManager() {
		return new EhCacheCacheManager(ehCacheManager().getObject());
	}
	
	@Lazy
	public EhCacheFactoryBean articleHits () {
		EhCacheFactoryBean articleHitsCacheEventListener = new EhCacheFactoryBean();
		articleHitsCacheEventListener.setCacheManager(ehCacheManager().getObject());
		articleHitsCacheEventListener.setCacheName("articleHits");
		
		Set<CacheEventListener> cacheEventListeners = new HashSet<CacheEventListener>();
		cacheEventListeners.add(new net.shopec.listener.CacheEventListener());
		articleHitsCacheEventListener.setCacheEventListeners(cacheEventListeners);
		return articleHitsCacheEventListener;
	}
	
	@Lazy
	public EhCacheFactoryBean productHits () {
		EhCacheFactoryBean productHitsCacheEventListener = new EhCacheFactoryBean();
		productHitsCacheEventListener.setCacheManager(ehCacheManager().getObject());
		productHitsCacheEventListener.setCacheName("productHits");
		
		Set<CacheEventListener> cacheEventListeners = new HashSet<CacheEventListener>();
		cacheEventListeners.add(new net.shopec.listener.CacheEventListener());
		productHitsCacheEventListener.setCacheEventListeners(cacheEventListeners);
		return productHitsCacheEventListener;
		
	}
	
	@Bean(name = "messageMethod")
	public MessageMethod messageMethod() {
		return new MessageMethod();
	}

	@Bean(name = "abbreviateMethod")
	public AbbreviateMethod abbreviateMethod() {
		return new AbbreviateMethod();
	}

	@Bean(name = "currencyMethod")
	public CurrencyMethod currencyMethod() {
		return new CurrencyMethod();
	}

	@Bean(name = "flashMessageDirective")
	public FlashMessageDirective flashMessageDirective() {
		return new FlashMessageDirective();
	}

	@Bean(name = "paginationDirective")
	public PaginationDirective paginationDirective() {
		return new PaginationDirective();
	}
	
	@Bean(name = "seoDirective")
	public SeoDirective seoDirective() {
		return new SeoDirective();
	}
	
	@Bean(name = "adPositionDirective")
	public AdPositionDirective adPositionDirective() {
		return new AdPositionDirective();
	}
	
	@Bean(name = "memberAttributeListDirective")
	public MemberAttributeListDirective memberAttributeListDirective() {
		return new MemberAttributeListDirective();
	}
	
	@Bean(name = "businessAttributeListDirective")
	public BusinessAttributeListDirective businessAttributeListDirective() {
		return new BusinessAttributeListDirective();
	}
	
	@Bean(name = "navigationListDirective")
	public NavigationListDirective navigationListDirective() {
		return new NavigationListDirective();
	}
	
	@Bean(name = "friendLinkListDirective")
	public FriendLinkListDirective friendLinkListDirective() {
		return new FriendLinkListDirective();
	}
	
	@Bean(name = "brandListDirective")
	public BrandListDirective brandListDirective() {
		return new BrandListDirective();
	}
	
	@Bean(name = "attributeListDirective")
	public AttributeListDirective attributeListDirective() {
		return new AttributeListDirective();
	}
	
	@Bean(name = "articleListDirective")
	public ArticleListDirective articleListDirective() {
		return new ArticleListDirective();
	}
	
	@Bean(name = "articleCategoryRootListDirective")
	public ArticleCategoryRootListDirective articleCategoryRootListDirective() {
		return new ArticleCategoryRootListDirective();
	}
	
	@Bean(name = "articleCategoryParentListDirective")
	public ArticleCategoryParentListDirective articleCategoryParentListDirective() {
		return new ArticleCategoryParentListDirective();
	}
	
	@Bean(name = "articleCategoryChildrenListDirective")
	public ArticleCategoryChildrenListDirective articleCategoryChildrenListDirective() {
		return new ArticleCategoryChildrenListDirective();
	}
	
	@Bean(name = "articleTagListDirective")
	public ArticleTagListDirective articleTagListDirective() {
		return new ArticleTagListDirective();
	}
	
	@Bean(name = "productListDirective")
	public ProductListDirective productListDirective() {
		return new ProductListDirective();
	}
	
	@Bean(name = "productCountDirective")
	public ProductCountDirective productCountDirective() {
		return new ProductCountDirective();
	}
	
	@Bean(name = "productCategoryRootListDirective")
	public ProductCategoryRootListDirective productCategoryRootListDirective() {
		return new ProductCategoryRootListDirective();
	}
	
	@Bean(name = "productCategoryParentListDirective")
	public ProductCategoryParentListDirective productCategoryParentListDirective() {
		return new ProductCategoryParentListDirective();
	}
	
	@Bean(name = "productCategoryChildrenListDirective")
	public ProductCategoryChildrenListDirective productCategoryChildrenListDirective() {
		return new ProductCategoryChildrenListDirective();
	}
	
	@Bean(name = "storeProductCategoryRootListDirective")
	public StoreProductCategoryRootListDirective storeProductCategoryRootListDirective() {
		return new StoreProductCategoryRootListDirective();
	}
	
	@Bean(name = "storeProductCategoryParentListDirective")
	public StoreProductCategoryParentListDirective storeProductCategoryParentListDirective() {
		return new StoreProductCategoryParentListDirective();
	}
	
	@Bean(name = "storeProductCategoryChildrenListDirective")
	public StoreProductCategoryChildrenListDirective storeProductCategoryChildrenListDirective() {
		return new StoreProductCategoryChildrenListDirective();
	}
	
	@Bean(name = "productTagListDirective")
	public ProductTagListDirective productTagListDirective() {
		return new ProductTagListDirective();
	}
	
	@Bean(name = "productFavoriteDirective")
	public ProductFavoriteDirective productFavoriteDirective() {
		return new ProductFavoriteDirective();
	}
	
	@Bean(name = "storeFavoriteDirective")
	public StoreFavoriteDirective storeFavoriteDirective() {
		return new StoreFavoriteDirective();
	}
	
	@Bean(name = "reviewListDirective")
	public ReviewListDirective reviewListDirective() {
		return new ReviewListDirective();
	}
	
	@Bean(name = "consultationListDirective")
	public ConsultationListDirective consultationListDirective() {
		return new ConsultationListDirective();
	}
	
	@Bean(name = "promotionListDirective")
	public PromotionListDirective promotionListDirective() {
		return new PromotionListDirective();
	}
	
	@Bean(name = "promotionPluginDirective")
	public PromotionPluginDirective promotionPluginDirective() {
		return new PromotionPluginDirective();
	}
	
	@Bean(name = "orderCountDirective")
	public OrderCountDirective orderCountDirective() {
		return new OrderCountDirective();
	}
	
	@Bean(name = "shiroTags")
	public ShiroTags shiroTags() {
		return new ShiroTags();
	}
	
	@Bean(name = "freeMarkerConfigurer")
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		freeMarkerConfigurer.setTemplateLoaderPaths("classpath:/", templateLoaderPath);
		freeMarkerConfigurer.setServletContext(servletContext());
		
		Properties settings = new Properties();
		settings.setProperty("default_encoding", encoding);
		settings.setProperty("url_escaping_charset", urlEscapingCharset);
		settings.setProperty("output_format", "HTMLOutputFormat");
		settings.setProperty("template_update_delay", update_delay);
		settings.setProperty("tag_syntax", "auto_detect");
		settings.setProperty("classic_compatible", "true");
		settings.setProperty("number_format", numberFormat);
		settings.setProperty("boolean_format", booleanFormat);
		settings.setProperty("datetime_format", dateTimeFormat);
		settings.setProperty("date_format", dateFormat);
		settings.setProperty("time_format", timeFormat);
		settings.setProperty("object_wrapper", "freemarker.ext.beans.BeansWrapper");
		freeMarkerConfigurer.setFreemarkerSettings(settings);
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("base", servletContext().getContextPath());
		variables.put("showPowered", true);
		variables.put("setting", SystemUtils.getSetting());
		variables.put("message", messageMethod());
		variables.put("abbreviate", abbreviateMethod());
		variables.put("currency", currencyMethod());
		variables.put("flash_message", flashMessageDirective());
		variables.put("pagination", paginationDirective());
		variables.put("seo", seoDirective());
		variables.put("ad_position", adPositionDirective());
		variables.put("member_attribute_list", memberAttributeListDirective());
		variables.put("business_attribute_list", businessAttributeListDirective());
		variables.put("navigation_list", navigationListDirective());
		variables.put("friend_link_list", friendLinkListDirective());
		variables.put("brand_list", brandListDirective());
		variables.put("attribute_list", attributeListDirective());
		variables.put("article_list", articleListDirective());
		variables.put("article_category_root_list", articleCategoryRootListDirective());
		variables.put("article_category_parent_list", articleCategoryParentListDirective());
		variables.put("article_category_children_list", articleCategoryChildrenListDirective());
		variables.put("article_tag_list", articleTagListDirective());
		variables.put("product_list", productListDirective());
		variables.put("product_count", productCountDirective());
		variables.put("product_category_root_list", productCategoryRootListDirective());
		variables.put("product_category_parent_list", productCategoryParentListDirective());
		variables.put("product_category_children_list", productCategoryChildrenListDirective());
		variables.put("store_product_category_root_list", storeProductCategoryRootListDirective());
		variables.put("store_product_category_parent_list", storeProductCategoryParentListDirective());
		variables.put("store_product_category_children_list", storeProductCategoryChildrenListDirective());
		variables.put("product_tag_list", productTagListDirective());
		variables.put("product_favorite", productFavoriteDirective());
		variables.put("store_favorite", storeFavoriteDirective());
		variables.put("review_list", reviewListDirective());
		variables.put("consultation_list", consultationListDirective());
		variables.put("promotion_list", promotionListDirective());
		variables.put("promotion_plugin", promotionPluginDirective());
		variables.put("order_count", orderCountDirective());
		variables.put("shiro", shiroTags());
		variables.put("compress", StandardCompress.INSTANCE);
		freeMarkerConfigurer.setFreemarkerVariables(variables);
		return freeMarkerConfigurer;
	}
	
	@Bean(name = "taskExecutor")
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		return executor;
	}
	
	@Bean(name = "localeResolver")
	public FixedLocaleResolver localeResolver() {
		FixedLocaleResolver fixedLocaleResolver = new FixedLocaleResolver();
		return fixedLocaleResolver;
	}
	
	//ResourceBundleMessageSource
	@Bean(name = "messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setCacheSeconds(cacheSeconds);
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setBasenames(StringUtils.split(basenames, ","));
		return messageSource;
	}
	
	
	@Bean(name = "captchaProducer")
	public DefaultKaptcha captchaProducer() {
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		Properties properties = new Properties();
		properties.setProperty("kaptcha.border", "no");
		properties.setProperty("kaptcha.image.width", imageWidth);
		properties.setProperty("kaptcha.image.height", imageHeight);
		properties.setProperty("kaptcha.textproducer.char.string", charString);
		properties.setProperty("kaptcha.textproducer.char.length", charLength);
		properties.setProperty("kaptcha.textproducer.char.space", charSpace);
		properties.setProperty("kaptcha.textproducer.font.color", fontColor);
		properties.setProperty("kaptcha.textproducer.font.size", fontSize);
		properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
		properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
		properties.setProperty("kaptcha.background.impl", "net.shopec.captcha.CaptchaBackground");
		properties.setProperty("kaptcha.background.imagePath", background_image_path);
		Config config = new Config(properties);
		defaultKaptcha.setConfig(config);
		return defaultKaptcha;
	}
	
	@Bean(name = "timingAspect")
	public TimingAspect timingAspect() {
		return new TimingAspect();
	}
	
	@Bean(name = "adminCaptchaFilter")
	@Scope(value = "prototype")
	public CaptchaFilter adminCaptchaFilter() {
		CaptchaFilter captchaFilter = new CaptchaFilter();
		captchaFilter.setCaptchaType(Setting.CaptchaType.adminLogin);
		return captchaFilter;
	}
	
	@Bean(name = "businessCaptchaFilter")
	@Scope(value = "prototype")
	public CaptchaFilter businessCaptchaFilter() {
		CaptchaFilter captchaFilter = new CaptchaFilter();
		captchaFilter.setCaptchaType(Setting.CaptchaType.businessLogin);
		return captchaFilter;
	}
	
	@Bean(name = "memberCaptchaFilter")
	@Scope(value = "prototype")
	public CaptchaFilter memberCaptchaFilter() {
		CaptchaFilter captchaFilter = new CaptchaFilter();
		captchaFilter.setCaptchaType(Setting.CaptchaType.memberLogin);
		return captchaFilter;
	}
	
	@Bean
	public FilterRegistrationBean<CaptchaFilter> adminCaptchaBean() {
		FilterRegistrationBean<CaptchaFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(adminCaptchaFilter());
		registration.addUrlPatterns("/admin/login");
		registration.setName("adminCaptchaFilter");
		return registration;
	}
	
	@Bean
	public FilterRegistrationBean<CaptchaFilter> businessCaptchaBean() {
		FilterRegistrationBean<CaptchaFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(businessCaptchaFilter());
		registration.addUrlPatterns("/business/login");
		registration.setName("businessCaptchaFilter");
		return registration;
	}
	
	@Bean
	public FilterRegistrationBean<CaptchaFilter> memberCaptchaBean() {
		FilterRegistrationBean<CaptchaFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(memberCaptchaFilter());
		registration.addUrlPatterns("/member/login");
		registration.setName("memberCaptchaFilter");
		return registration;
	}
	
}
