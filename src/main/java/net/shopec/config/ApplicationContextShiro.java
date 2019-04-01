package net.shopec.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.ehcache.CacheManager;
import net.shopec.entity.Admin;
import net.shopec.entity.Business;
import net.shopec.entity.Member;
import net.shopec.security.AuthenticationFilter;
import net.shopec.security.AuthorizingRealm;
import net.shopec.security.LogoutFilter;


@Configuration
public class ApplicationContextShiro {

	@Inject
	private CacheManager cacheManager;

	
	@Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        // 未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/common/error/unauthorized");
        
        // 拦截器
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("/member/register", "anon");
		map.put("/member/register/**", "anon");
		map.put("/member/login", "memberAuthc");
		map.put("/member/logout", "logout");

		map.put("/order/**", "memberAuthc,perms[member]");

		map.put("/consultation/add/*", "memberAuthc,perms[member]");
		map.put("/consultation/save", "memberAuthc,perms[member]");

		map.put("/review/add/*", "memberAuthc,perms[member]");
		map.put("/review/save", "memberAuthc,perms[member]");

		map.put("/member/**", "memberAuthc,perms[member]");

		map.put("/business/register/**", "anon");
		map.put("/business/login", "businessAuthc");
		map.put("/business/logout", "logout");
		map.put("/business/file/**", "anon");

		map.put("/business/index", "businessAuthc,perms[business:index]");

		map.put("/business/index/**", "businessAuthc,perms[business:index]");
		map.put("/business/product/**", "businessAuthc,perms[business:product]");
		map.put("/business/stock/**", "businessAuthc,perms[business:stock]");
		map.put("/business/product_notify/**", "businessAuthc,perms[business:productNotify]");
		map.put("/business/consultation/**", "businessAuthc,perms[business:consultation]");
		map.put("/business/review/**", "businessAuthc,perms[business:review]");
		
		map.put("/business/order/**", "businessAuthc,perms[business:order]");
		map.put("/business/print/**", "businessAuthc,perms[business:print]");
		map.put("/business/delivery_template/**", "businessAuthc,perms[business:deliveryTemplate]");
		map.put("/business/delivery_center/**", "businessAuthc,perms[business:deliveryCenter]");
		
		map.put("/business/store/register", "businessAuthc,perms[business:storeRegister]");
		map.put("/business/store/setting", "businessAuthc,perms[business:storeSetting]");
		map.put("/business/store/payment", "businessAuthc,perms[business:storePayment]");
		map.put("/business/store/reapply", "businessAuthc,perms[business:storeReapply]");
		map.put("/business/store_product_category/**", "businessAuthc,perms[business:storeProductCategory]");
		map.put("/business/store_product_tag/**", "businessAuthc,perms[business:storeProductTag]");
		map.put("/business/category_application/**", "businessAuthc,perms[business:categoryApplication]");
		map.put("/business/shipping_method/**", "businessAuthc,perms[business:shippingMethod]");
		map.put("/business/area_freight_config/**", "businessAuthc,perms[business:areaFreightConfig]");
		map.put("/business/store_ad_image/**", "businessAuthc,perms[business:storeAdImage]");
		
		map.put("/business/discount_promotion/**", "businessAuthc,perms[business:discountPromotion]");
		map.put("/business/full_reduction_promotion/**", "businessAuthc,perms[business:fullReductionPromotion]");
		map.put("/business/coupon/**", "businessAuthc,perms[business:coupon]");
		
		map.put("/business/deposit/**", "businessAuthc,perms[business:deposit]");
		map.put("/business/cash/**", "businessAuthc,perms[business:cash]");
		
		map.put("/business/profile/**", "businessAuthc,perms[business:profile]");
		map.put("/business/password/**", "businessAuthc,perms[business:password]");
		
		map.put("/business/instant_message/**", "businessAuthc,perms[business:instantMessage]");
		map.put("/business/order_statistic/**", "businessAuthc,perms[business:orderStatistic]");
		map.put("/business/product_ranking/**", "businessAuthc,perms[business:productRanking]");

		map.put("/business/**", "businessAuthc");

		map.put("/admin", "anon");
		map.put("/admin/", "anon");
		map.put("/admin/login", "adminAuthc");
		map.put("/admin/logout", "logout");

		map.put("/admin/business/**", "adminAuthc,perms[admin:business]");
		map.put("/admin/store/**", "adminAuthc,perms[admin:store]");
		map.put("/admin/store_category/**", "adminAuthc,perms[admin:storeCategory]");
		map.put("/admin/store_rank/**", "adminAuthc,perms[admin:storeRank]");
		map.put("/admin/business_attribute/**", "adminAuthc,perms[admin:businessAttribute]");
		map.put("/admin/cash/**", "adminAuthc,perms[admin:cash]");
		map.put("/admin/category_application/**", "adminAuthc,perms[admin:categoryApplication]");
		map.put("/admin/business_deposit/**", "adminAuthc,perms[admin:businessDeposit]");

		map.put("/admin/product/**", "adminAuthc,perms[admin:product]");
		map.put("/admin/stock/**", "adminAuthc,perms[admin:stock]");
		map.put("/admin/product_category/**", "adminAuthc,perms[admin:productCategory]");
		map.put("/admin/product_tag/**", "adminAuthc,perms[admin:productTag]");
		map.put("/admin/parameter/**", "adminAuthc,perms[admin:parameter]");
		map.put("/admin/attribute/**", "adminAuthc,perms[admin:attribute]");
		map.put("/admin/specification/**", "adminAuthc,perms[admin:specification]");
		map.put("/admin/brand/**", "adminAuthc,perms[admin:brand]");

		map.put("/admin/order/**", "adminAuthc,perms[admin:order]");
		map.put("/admin/print/**", "adminAuthc,perms[admin:print]");
		map.put("/admin/order_payment/**", "adminAuthc,perms[admin:orderPayment]");
		map.put("/admin/order_refunds/**", "adminAuthc,perms[admin:orderRefunds]");
		map.put("/admin/order_shipping/**", "adminAuthc,perms[admin:orderShipping]");
		map.put("/admin/order_returns/**", "adminAuthc,perms[admin:orderReturns]");
		map.put("/admin/delivery_center/**", "adminAuthc,perms[admin:deliveryCenter]");
		map.put("/admin/delivery_template/**", "adminAuthc,perms[admin:deliveryTemplate]");

		map.put("/admin/member/**", "adminAuthc,perms[admin:member]");
		map.put("/admin/member_rank/**", "adminAuthc,perms[admin:memberRank]");
		map.put("/admin/member_attribute/**", "adminAuthc,perms[admin:memberAttribute]");
		map.put("/admin/point/**", "adminAuthc,perms[admin:point]");
		map.put("/admin/member_deposit/**", "adminAuthc,perms[admin:memberDeposit]");
		map.put("/admin/review/**", "adminAuthc,perms[admin:review]");
		map.put("/admin/consultation/**", "adminAuthc,perms[admin:consultation]");
		map.put("/admin/message_config/**", "adminAuthc,perms[admin:messageConfig]");

		map.put("/admin/navigation/**", "adminAuthc,perms[admin:navigation]");
		map.put("/admin/article/**", "adminAuthc,perms[admin:article]");
		map.put("/admin/article_category/**", "adminAuthc,perms[admin:articleCategory]");
		map.put("/admin/article_tag/**", "adminAuthc,perms[admin:articleTag]");
		map.put("/admin/friend_link/**", "adminAuthc,perms[admin:friendLink]");
		map.put("/admin/ad_position/**", "adminAuthc,perms[admin:adPosition]");
		map.put("/admin/ad/**", "adminAuthc,perms[admin:ad]");
		map.put("/admin/template/**", "adminAuthc,perms[admin:template]");
		map.put("/admin/cache/**", "adminAuthc,perms[admin:cache]");

		map.put("/admin/promotion/**", "adminAuthc,perms[admin:promotion]");
		map.put("/admin/coupon/**", "adminAuthc,perms[admin:coupon]");
		map.put("/admin/seo/**", "adminAuthc,perms[admin:seo]");

		map.put("/admin/setting/**", "adminAuthc,perms[admin:setting]");
		map.put("/admin/area/**", "adminAuthc,perms[admin:area]");
		map.put("/admin/payment_method/**", "adminAuthc,perms[admin:paymentMethod]");
		map.put("/admin/shipping_method/**", "adminAuthc,perms[admin:shippingMethod]");
		map.put("/admin/delivery_corp/**", "adminAuthc,perms[admin:deliveryCorp]");
		map.put("/admin/payment_plugin/**", "adminAuthc,perms[admin:paymentPlugin]");
		map.put("/admin/storage_plugin/**", "adminAuthc,perms[admin:storagePlugin]");
		map.put("/admin/login_plugin/**", "adminAuthc,perms[admin:loginPlugin]");
		map.put("/admin/promotion_plugin/**", "adminAuthc,perms[admin:promotionPlugin]");
		map.put("/admin/admin/**", "adminAuthc,perms[admin:admin]");
		map.put("/admin/role/**", "adminAuthc,perms[admin:role]");
		map.put("/admin/message/**", "adminAuthc,perms[admin:message]");
		map.put("/admin/audit_log/**", "adminAuthc,perms[admin:auditLog]");

		map.put("/admin/**", "adminAuthc");
		
		Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
        filters.put("businessAuthc", businessAuthc());
        filters.put("adminAuthc", adminAuthc());
        filters.put("memberAuthc", memberAuthc());
        filters.put("logout", logout());
        shiroFilterFactoryBean.setFilters(filters);
	        
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }
	
	@Bean("adminAuthc")
	public AuthenticationFilter adminAuthc() {
		AuthenticationFilter adminAuthc = new AuthenticationFilter();
        adminAuthc.setUserClass(Admin.class);
        adminAuthc.setLoginUrl("/admin/login");
        adminAuthc.setSuccessUrl("/admin/index");
		return adminAuthc;
	}
	
	@Bean("businessAuthc")
	public AuthenticationFilter businessAuthc() {
		AuthenticationFilter businessAuthc = new AuthenticationFilter();
		businessAuthc.setUserClass(Business.class);
		businessAuthc.setLoginUrl("/business/login");
		businessAuthc.setSuccessUrl("/business/index");
		return businessAuthc;
	}
	
	@Bean("memberAuthc")
	public AuthenticationFilter memberAuthc() {
		AuthenticationFilter memberAuthc = new AuthenticationFilter();
		memberAuthc.setUserClass(Member.class);
		memberAuthc.setLoginUrl("/member/login");
		memberAuthc.setSuccessUrl("/");
		return memberAuthc;
	}
	
	@Bean("logout")
	public LogoutFilter logout() {
		return new LogoutFilter();
	}
	
	@Bean
	public FilterRegistrationBean<AuthenticationFilter> adminRegistration() {
	    FilterRegistrationBean<AuthenticationFilter> adminRegistration = new FilterRegistrationBean<>();
	    adminRegistration.setFilter(adminAuthc());
	    adminRegistration.setEnabled(false);
	    return adminRegistration;
	}
	
	@Bean
	public FilterRegistrationBean<AuthenticationFilter> businessRegistration() {
	    FilterRegistrationBean<AuthenticationFilter> businessRegistration = new FilterRegistrationBean<>();
	    businessRegistration.setFilter(businessAuthc());
	    businessRegistration.setEnabled(false);
	    return businessRegistration;
	}
	
	@Bean
	public FilterRegistrationBean<AuthenticationFilter> memberRegistration() {
	    FilterRegistrationBean<AuthenticationFilter> memberRegistration = new FilterRegistrationBean<>();
	    memberRegistration.setFilter(memberAuthc());
	    memberRegistration.setEnabled(false);
	    return memberRegistration;
	}
	
	@Bean
	public FilterRegistrationBean<LogoutFilter> logoutRegistration() {
	    FilterRegistrationBean<LogoutFilter> logoutRegistration = new FilterRegistrationBean<>();
	    logoutRegistration.setFilter(logout());
	    logoutRegistration.setEnabled(false);
	    return logoutRegistration;
	}
	
    @Bean(name = "securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(authorizingRealm());
        securityManager.setCacheManager(shiroCacheManager());
        
        // 解决使用Shiro后URL中JSESSIONID的问题
        DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager();
        webSessionManager.setSessionIdUrlRewritingEnabled(false);
        securityManager.setSessionManager(webSessionManager);
        return securityManager;
    }
    
    @Bean(name = "authorizingRealm")
    public AuthorizingRealm authorizingRealm() {
    	AuthorizingRealm authorizingRealm = new AuthorizingRealm();
    	authorizingRealm.setAuthorizationCacheName("authorization");
		return authorizingRealm;
    }
    
    @Bean(name = "shiroCacheManager")
    public EhCacheManager shiroCacheManager() {
    	EhCacheManager ehCacheManager = new EhCacheManager();
    	ehCacheManager.setCacheManager(cacheManager);
		return ehCacheManager;
    }
    
    @Bean(name = "methodInvokingFactoryBean")
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
    	MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
    	methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
    	methodInvokingFactoryBean.setArguments(securityManager());
		return methodInvokingFactoryBean;
    }
    
}
