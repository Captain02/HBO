/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.common.config;

import io.renren.modules.sys.shiro.JWTFilter;
import io.renren.modules.sys.shiro.UserRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro的配置文件
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class ShiroConfig {

    /**
     * 单机环境，session交给shiro管理
     */
//    @Bean
//    @ConditionalOnProperty(prefix = "renren", name = "cluster", havingValue = "false")
//    public DefaultWebSessionManager sessionManager(@Value("${renren.globalSessionTimeout:3600}") long globalSessionTimeout){
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        sessionManager.setSessionValidationSchedulerEnabled(true);
//        sessionManager.setSessionIdUrlRewritingEnabled(false);
//        sessionManager.setSessionValidationInterval(globalSessionTimeout * 1000);
//        sessionManager.setGlobalSessionTimeout(globalSessionTimeout * 1000);
//
//        return sessionManager;
//    }

    /**
     * 集群环境，session交给spring-session管理
     */
//    @Bean
//    @ConditionalOnProperty(prefix = "renren", name = "cluster", havingValue = "true")
//    public ServletContainerSessionManager servletContainerSessionManager() {
//        return new ServletContainerSessionManager();
//    }

//, SessionManager sessionManager
    @Bean("securityManager")
    public DefaultWebSecurityManager getManager(UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
//        securityManager.setSessionManager(sessionManager);
//        securityManager.setRememberMeManager(null);
        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }


    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();

        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JWTFilter());
        shiroFilter.setFilters(filterMap);

        shiroFilter.setSecurityManager(securityManager);
        //shiroFilter.setLoginUrl("/login.html");
        shiroFilter.setUnauthorizedUrl("/login.html");

        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        filterRuleMap.put("/swagger/**", "anon");
        filterRuleMap.put("/v2/api-docs", "anon");
        filterRuleMap.put("/swagger-ui.html", "anon");
        filterRuleMap.put("/webjars/**", "anon");
        filterRuleMap.put("/swagger-resources/**", "anon");

        filterRuleMap.put("/statics/**", "anon");
        filterRuleMap.put("/renren-admin/statics/**", "anon");
        filterRuleMap.put("/login.html", "anon");
        filterRuleMap.put("/sys/login", "anon");
        filterRuleMap.put("/sys/getTitleName", "anon");
        filterRuleMap.put("/favicon.ico", "anon");
        filterRuleMap.put("/captcha.jpg", "anon");
        //filterMap.put("/**", "authc");

        filterRuleMap.put("/**", "jwt");
        shiroFilter.setFilterChainDefinitionMap(filterRuleMap);

        return shiroFilter;
    }

    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
