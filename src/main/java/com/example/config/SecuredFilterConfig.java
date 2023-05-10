package com.example.config;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class SecuredFilterConfig {
//    @Autowired
//    private TokenFilter tokenFilter;

//    @Bean
//    public FilterRegistrationBean<Filter> filterRegistrationBean(){
//        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
//        bean.setFilter(tokenFilter);
//        bean.addUrlPatterns("/api/v1/article/private/*");
//        bean.addUrlPatterns("/api/v1/region/private/*");
//        bean.addUrlPatterns("/api/v1/category/private/*");
//        bean.addUrlPatterns("/api/v1/article-type/private/*");
//        bean.addUrlPatterns("/api/v1/profile/private/*");
//        bean.addUrlPatterns("/api/v1/article-like/public/*");
//        bean.addUrlPatterns("/api/v1/comment/*");
//        bean.addUrlPatterns("/api/v1/comment-like/public/*");
//        bean.addUrlPatterns("/api/v1/email-history/private/*");
//        bean.addUrlPatterns("/api/v1/profile/private/*");
//        bean.addUrlPatterns("/api/v1/profile/public/update-photo");
//        bean.addUrlPatterns("/api/v1/saved-article/public/*");
//        return bean;
//    }
//}
