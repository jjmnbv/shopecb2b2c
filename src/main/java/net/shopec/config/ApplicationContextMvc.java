package net.shopec.config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mobile.device.DeviceResolverRequestFilter;
import org.springframework.mobile.device.DeviceWebArgumentResolver;
import org.springframework.mobile.device.site.SitePreferenceWebArgumentResolver;
import org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver;
import org.springframework.stereotype.Controller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import net.shopec.Setting;
import net.shopec.audit.AuditLogInterceptor;
import net.shopec.audit.AuditLogMethodArgumentResolver;
import net.shopec.captcha.CaptchaInterceptor;
import net.shopec.entity.Admin;
import net.shopec.entity.Business;
import net.shopec.entity.Member;
import net.shopec.exception.RestfulAPIException;
import net.shopec.filter.PageCachingFilter;
import net.shopec.interceptor.ListInterceptor;
import net.shopec.interceptor.PromotionPluginInterceptor;
import net.shopec.interceptor.RestfulAPIInterceptor;
import net.shopec.interceptor.ValidateInterceptor;
import net.shopec.interceptor.ValidateInterceptor.WhitelistType;
import net.shopec.security.CsrfInterceptor;
import net.shopec.security.CurrentCartHandlerInterceptor;
import net.shopec.security.CurrentCartMethodArgumentResolver;
import net.shopec.security.CurrentStoreHandlerInterceptor;
import net.shopec.security.CurrentStoreMethodArgumentResolver;
import net.shopec.security.CurrentUserHandlerInterceptor;
import net.shopec.security.CurrentUserMethodArgumentResolver;

@Configuration
@PropertySource("classpath:shopec.properties")
@ComponentScan(includeFilters = {
		@org.springframework.context.annotation.ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Controller.class),
		@org.springframework.context.annotation.ComponentScan.Filter(type = FilterType.ANNOTATION, classes = ControllerAdvice.class) })
public class ApplicationContextMvc implements WebMvcConfigurer {

	@Value("${html_content_type}")
	private String contentType;
	
	@Value("${template.suffix}")
	private String suffix;
	
	@Value("${json_content_type}")
	private String jsonContentType;
	
	@Value("${html_content_type}")
	private String htmlContentType;
	
	
	@Inject
	private MessageSource messageSource;
	
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new ServletWebArgumentResolverAdapter(new DeviceWebArgumentResolver()));
		resolvers.add(new ServletWebArgumentResolverAdapter(new SitePreferenceWebArgumentResolver()));
		resolvers.add(currentUserMethodArgumentResolver());
		resolvers.add(currentCartMethodArgumentResolver());
		resolvers.add(currentStoreMethodArgumentResolver());
		resolvers.add(auditLogMethodArgumentResolver());
	}
	
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new BufferedImageHttpMessageConverter());
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(MediaType.parseMediaType(jsonContentType));
		supportedMediaTypes.add(MediaType.parseMediaType(htmlContentType));
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
		converters.add(mappingJackson2HttpMessageConverter);
	}
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:static/favicon.ico").setCachePeriod(86400);
		registry.addResourceHandler("/robots.txt").addResourceLocations("classpath:static/robots.txt").setCachePeriod(86400);
		registry.addResourceHandler("/resources/**").addResourceLocations("classpath:static/resources/").setCachePeriod(86400);
		//registry.addResourceHandler("/upload/**").addResourceLocations("classpath:static/upload/").setCachePeriod(86400);
		registry.addResourceHandler("/201601/**").addResourceLocations("classpath:static/201601/").setCachePeriod(86400);
		// 本地测试时要这样配置实际的目录
		//registry.addResourceHandler("/upload/**").addResourceLocations("file:/Users/lihongyuan/img/upload/").setCachePeriod(86400);
		registry.addResourceHandler("/upload/**").addResourceLocations("file:D:/img/upload/").setCachePeriod(86400);

	}
	
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(false);
	}
	
	@Bean(name = "resourcesInterceptor")
	public WebContentInterceptor resourcesInterceptor() {
		WebContentInterceptor resourcesInterceptor = new WebContentInterceptor();
		resourcesInterceptor.setCacheSeconds(86400);
		return resourcesInterceptor;
	}
	
	@Bean(name = "webContentInterceptor")
	public WebContentInterceptor webContentInterceptor() {
		WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
		webContentInterceptor.setCacheSeconds(0);
		return webContentInterceptor;
	}
	
	/**
	 * 当前用户MethodArgumentResolver
	 */
	@Bean(name = "currentUserMethodArgumentResolver")
	public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
		return new CurrentUserMethodArgumentResolver();
	}
	
	/**
	 * 当前购物车MethodArgumentResolver
	 */
	@Bean(name = "currentCartMethodArgumentResolver")
	public CurrentCartMethodArgumentResolver currentCartMethodArgumentResolver() {
		return new CurrentCartMethodArgumentResolver();
	}
	
	/**
	 * 当前店铺MethodArgumentResolver
	 */
	@Bean(name = "currentStoreMethodArgumentResolver")
	public CurrentStoreMethodArgumentResolver currentStoreMethodArgumentResolver() {
		return new CurrentStoreMethodArgumentResolver();
	}
	
	/**
	 * 审计日志MethodArgumentResolver
	 */
	@Bean(name = "auditLogMethodArgumentResolver")
	public AuditLogMethodArgumentResolver auditLogMethodArgumentResolver() {
		return new AuditLogMethodArgumentResolver();
	}
	
	/**
	 * 当前用户拦截器
	 */
	@Bean(name = "memberInterceptor")
	public CurrentUserHandlerInterceptor memberInterceptor() {
		CurrentUserHandlerInterceptor memberInterceptor = new CurrentUserHandlerInterceptor();
		memberInterceptor.setUserClass(Member.class);
		return memberInterceptor;
	}
	
	/**
	 * 当前商家拦截器
	 */
	@Bean(name = "businessInterceptor")
	public CurrentUserHandlerInterceptor businessInterceptor() {
		CurrentUserHandlerInterceptor businessInterceptor = new CurrentUserHandlerInterceptor();
		businessInterceptor.setUserClass(Business.class);
		return businessInterceptor;
	}
	
	/**
	 * 当前管理员拦截器
	 */
	@Bean(name = "adminInterceptor")
	public CurrentUserHandlerInterceptor adminInterceptor() {
		CurrentUserHandlerInterceptor adminInterceptor = new CurrentUserHandlerInterceptor();
		adminInterceptor.setUserClass(Admin.class);
		return adminInterceptor;
	}
	
	/**
	 * 当前购物车拦截器
	 */
	@Bean(name = "currentCartHandlerInterceptor")
	public CurrentCartHandlerInterceptor currentCartHandlerInterceptor() {
		return new CurrentCartHandlerInterceptor();
	}
	
	/**
	 * 当前店铺拦截器
	 */
	@Bean(name = "currentStoreHandlerInterceptor")
	public CurrentStoreHandlerInterceptor currentStoreHandlerInterceptor() {
		return new CurrentStoreHandlerInterceptor();
	}
	
	/**
	 * CSRF拦截器
	 */
	@Bean(name = "csrfInterceptor")
	public CsrfInterceptor csrfInterceptor() {
		return new CsrfInterceptor();
	}
	
	/**
	 * 验证
	 */
	@Bean(name = "validateInterceptor")
	public ValidateInterceptor validateInterceptor() {
		return new ValidateInterceptor();
	}
	
	/**
	 * 白名单验证
	 */
	@Bean(name = "whitelistValidateInterceptor")
	public ValidateInterceptor whitelistValidateInterceptor() {
		ValidateInterceptor whitelistValidateInterceptor = new ValidateInterceptor();
		whitelistValidateInterceptor.setWhitelistType(WhitelistType.relaxed);
		return whitelistValidateInterceptor;
	}
	
	/**
	 * 会员验证码拦截器
	 */
	@Bean(name = "memberRegister")
	public CaptchaInterceptor memberRegister() {
		CaptchaInterceptor memberRegister = new CaptchaInterceptor();
		memberRegister.setCaptchaType(Setting.CaptchaType.memberRegister);
		return memberRegister;
	}
	
	/**
	 * 商家验证码拦截器
	 */
	@Bean(name = "businessRegister")
	public CaptchaInterceptor businessRegister() {
		CaptchaInterceptor businessRegister = new CaptchaInterceptor();
		businessRegister.setCaptchaType(Setting.CaptchaType.businessRegister);
		return businessRegister;
	}
	
	/**
	 * 评论验证码拦截器
	 */
	@Bean(name = "review")
	public CaptchaInterceptor review() {
		CaptchaInterceptor review = new CaptchaInterceptor();
		review.setCaptchaType(Setting.CaptchaType.review);
		return review;
	}
	
	/**
	 * 咨询验证码拦截器
	 */
	@Bean(name = "consultation")
	public CaptchaInterceptor consultation() {
		CaptchaInterceptor consultation = new CaptchaInterceptor();
		consultation.setCaptchaType(Setting.CaptchaType.consultation);
		return consultation;
	}
	
	/**
	 * 找回密码验证码拦截器
	 */
	@Bean(name = "resetPassword")
	public CaptchaInterceptor resetPassword() {
		CaptchaInterceptor resetPassword = new CaptchaInterceptor();
		resetPassword.setCaptchaType(Setting.CaptchaType.forgotPassword);
		return resetPassword;
	}
	
	/**
	 * 列表查询
	 */
	@Bean(name = "listInterceptor")
	public ListInterceptor listInterceptor() {
		return new ListInterceptor();
	}
	
	/**
	 * 审计日志拦截器
	 */
	@Bean(name = "auditLogInterceptor")
	public AuditLogInterceptor auditLogInterceptor() {
		return new AuditLogInterceptor();
	}
	
	/**
	 * 促销插件
	 */
	@Bean(name = "promotionPluginInterceptor")
	public PromotionPluginInterceptor promotionPluginInterceptor() {
		return new PromotionPluginInterceptor();
	}
	
	/**
	 * 接口签名认证拦截器
	 */
	@Bean(name = "apiInterceptor")
	public RestfulAPIInterceptor restfulAPIInterceptor() {
		return new RestfulAPIInterceptor();
	}
	
	/**
	 * 接口签名认证拦截器
	 */
	@Bean(name = "restfulAPIException")
	public RestfulAPIException restfulAPIException() {
		return new RestfulAPIException();
	}
	
	/**
	 * 增加拦截器
	 */
	public void addInterceptors(InterceptorRegistry registry) {
		// resources
		registry.addInterceptor(resourcesInterceptor()).addPathPatterns("/resources/**");
		// web
		registry.addInterceptor(webContentInterceptor()).addPathPatterns("/cart/**", "/order/**", "/member/**", "/business/**", "/admin/**");
		// member
		registry.addInterceptor(memberInterceptor()).addPathPatterns("/cart/**", "/order/**", "/member/**");
		// business
		registry.addInterceptor(businessInterceptor()).addPathPatterns("/business/**");
		// admin
		registry.addInterceptor(adminInterceptor()).addPathPatterns("/admin/**");
		// cart
		registry.addInterceptor(currentCartHandlerInterceptor()).addPathPatterns("/cart/**", "/order/**");
		// store
		registry.addInterceptor(currentStoreHandlerInterceptor()).addPathPatterns("/business/**");
		// CSRF
		registry.addInterceptor(csrfInterceptor()).addPathPatterns("/**").excludePathPatterns("/payment/**", "/api/**");
		// 接口签名认证拦截器
		registry.addInterceptor(restfulAPIInterceptor()).addPathPatterns("/api/**");
				
		// validate
		List<String> exclude = new ArrayList<String>();
		exclude.add("/admin/**");
		exclude.add("/business/product/**");
		exclude.add("/business/delivery_template/**");
		exclude.add("/business/discount_promotion/**");
		exclude.add("/business/full_reduction_promotion/**");
		exclude.add("/business/coupon/**");
		registry.addInterceptor(validateInterceptor()).addPathPatterns("/**").excludePathPatterns(exclude);
		
		// validate
		List<String> mapping = new ArrayList<String>();
		mapping.add("/business/product/**");
		mapping.add("/business/delivery_template/**");
		mapping.add("/business/discount_promotion/**");
		mapping.add("/business/full_reduction_promotion/**");
		mapping.add("/business/coupon/**");
		registry.addInterceptor(whitelistValidateInterceptor()).addPathPatterns(mapping);
		
		// memberCaptcha
		registry.addInterceptor(memberRegister()).addPathPatterns("/member/register/submit");
		// businessCaptcha
		registry.addInterceptor(businessRegister()).addPathPatterns("/business/register/submit");
		// reviewCaptcha
		registry.addInterceptor(review()).addPathPatterns("/review/save");
		// consultationCaptcha
		registry.addInterceptor(consultation()).addPathPatterns("/consultation/save");
		// resetPasswordCaptcha
		registry.addInterceptor(resetPassword()).addPathPatterns("/password/reset");
		// list
		registry.addInterceptor(listInterceptor()).addPathPatterns("/admin/**");
		// auditLog
		registry.addInterceptor(auditLogInterceptor()).addPathPatterns("/admin/**");
		// promotionPlugin
		List<String> pMapping = new ArrayList<String>();
		pMapping.add("/business/discount_promotion/**");
		pMapping.add("/business/full_reduction_promotion/**");
		
		List<String> pExcludeMapping = new ArrayList<String>();
		pExcludeMapping.add("/business/*/calculate");
		pExcludeMapping.add("/business/*/end_date");
		pExcludeMapping.add("/business/*/buy");
		registry.addInterceptor(promotionPluginInterceptor()).addPathPatterns(pMapping).excludePathPatterns(pExcludeMapping);
		
	}

	@Bean(name="validator")
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource);
		return localValidatorFactoryBean;
	}
	
	@Bean(name="viewResolver")
	public LiteDeviceDelegatingViewResolver viewResolver(){
		FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
		resolver.setContentType(contentType);
		resolver.setSuffix(suffix);
		
		LiteDeviceDelegatingViewResolver viewResolver = new LiteDeviceDelegatingViewResolver(resolver);
		viewResolver.setMobilePrefix("mobile/");
		viewResolver.setTabletPrefix("tablet/");
		viewResolver.setEnableFallback(true);
	    return viewResolver;
	}
	
	@Bean
	public FilterRegistrationBean<PageCachingFilter> pageCachingFilterRegistration() {
		FilterRegistrationBean<PageCachingFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new PageCachingFilter());
		registration.addUrlPatterns("/", "/article/*", "/product_category/list", "/product/list", "/review/list", "/consultation/list", "/friend_link/*", "/resources/shop/js/*", "/resources/mobile/shop/js/*");
		registration.setOrder(5);
		return registration;
	}

	@Bean
	public FilterRegistrationBean<DeviceResolverRequestFilter> deviceResolverRequestFilterRegistration() {
		FilterRegistrationBean<DeviceResolverRequestFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new DeviceResolverRequestFilter());
		registration.addUrlPatterns("/", "/article/*", "/product_category/list", "/product/list", "/review/list", "/consultation/list", "/friend_link/*", "/resources/shop/js/*", "/resources/mobile/shop/js/*");
		registration.setOrder(4);
		return registration;
	}

	@Bean
	public FilterRegistrationBean<CharacterEncodingFilter> characterEncodingFilterRegistration() {
		FilterRegistrationBean<CharacterEncodingFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new CharacterEncodingFilter());
		registration.addUrlPatterns("/*");
		registration.addInitParameter("encoding", "UTF-8");
		registration.addInitParameter("forceEncoding", "true");
		registration.setName("encodingFilter");
		return registration;
	}
	
	//统一异常处理
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(restfulAPIException());
    }
    
    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //registry.addMapping("/**");
    }
}
