package com.intelijake.mall.config;

import com.intelijake.mall.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName: WebConfig
 * Description:
 * <p>
 * Datetime: 2025/6/28 23:03
 * Author: @Likun.Fang
 * Version: 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/page/cart/**","/cart/**","/page/order/**","/order/**");
    }
}
