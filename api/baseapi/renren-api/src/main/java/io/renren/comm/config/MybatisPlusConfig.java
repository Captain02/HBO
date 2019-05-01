/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.comm.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import io.renren.comm.plugin.PagePlugin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * mybatis-plus配置
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     */

    @Value("${dialect}") private String dialect;
    @Value("${pageSqlId}") private String pageSqlId;

    @Bean
    public PagePlugin pagePlugin() {
        PagePlugin pagePlugin = new PagePlugin();
        Properties properties = new Properties();
        properties.setProperty("dialect", dialect);
        properties.setProperty("pageSqlId", pageSqlId);
        pagePlugin.setProperties(properties);
        return pagePlugin;
    }

//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        return new PaginationInterceptor();
//    }
//
//    @Bean
//    public ISqlInjector sqlInjector() {
//        return new LogicSqlInjector();
//    }

//    public ConfigurationCustomizer configurationCustomizer() {
//        return ConfigurationCustomizer;
//    }
}
