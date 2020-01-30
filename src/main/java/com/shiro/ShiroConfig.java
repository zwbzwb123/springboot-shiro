package com.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authz.SslFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.SerializationUtils;
import sun.security.smartcardio.SunPCSC;

import javax.servlet.Filter;
import java.util.*;


@Configuration
public class ShiroConfig {


    @Bean
    public ShiroFilterFactoryBean filterFactoryBean(@Autowired DefaultWebSecurityManager securityManager,@Autowired SslFilter sslFilter){
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter>  map = new HashMap<>();
        map.put("ssl",sslFilter);
        filterFactoryBean.setFilters(map);
        //设置securityManager
        filterFactoryBean.setSecurityManager(securityManager);
        //设置拦截
        Map<String,String> filterMap = new LinkedHashMap<>();
        filterMap.put("/show","user");
        filterMap.put("/login","ssl,anon");
        filterMap.put("/*","authc");

        filterFactoryBean.setFilterChainDefinitionMap(filterMap);
        filterFactoryBean.setLoginUrl("/toLogin");
        return filterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Autowired UserRealm realm,@Autowired CacheManager cacheManager
                                                              ,@Autowired DefaultWebSessionManager defaultSessionManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        securityManager.setCacheManager(cacheManager);
        securityManager.setSessionManager(defaultSessionManager);
        return securityManager;
    }

    @Bean
    public UserRealm userRealm(){
        UserRealm realm = new UserRealm();

        realm.setCachingEnabled(true);
        realm.setAuthenticationCachingEnabled(true);
        realm.setAuthorizationCachingEnabled(true);

        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher("md5"); //加密算法
        realm.setCredentialsMatcher(matcher);
        return realm;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Autowired DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;

    }

    @Bean
    public CacheManager cacheManager(){
        EhCacheManager cacheManager = new EhCacheManager();
        return  cacheManager;
    }

    //session管理
    @Bean
    public SimpleCookie simpleCookie(){
        SimpleCookie cookie = new SimpleCookie();
        cookie.setName("SESSIONID");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        return cookie;
    }


    @Bean
    public DefaultWebSessionManager defaultSessionManager(@Autowired SimpleCookie simpleCookie){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdCookie(simpleCookie);
        sessionManager.setGlobalSessionTimeout(20000);

        //session监听
        ArrayList<SessionListener> collection = new ArrayList<>();
        collection.add(new MySessionListener());
        sessionManager.setSessionListeners(collection);

        //session检测
        sessionManager.setSessionValidationSchedulerEnabled(true); //默认就是true
        sessionManager.setSessionValidationInterval(50000); //默认 一小时
        return sessionManager;
    }


    //ssl filter
    @Bean
    public SslFilter sslFilter(){
        SslFilter filter = new SslFilter();
        filter.setPort(8080);
        return filter;
    }


}
