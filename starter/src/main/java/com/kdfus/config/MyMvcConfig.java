package com.kdfus.config;

import com.kdfus.filter.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Cra2iTeT
 * @version 1.0
 * @date 2022/4/13 15:13
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录验证 拦截
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/user/login",
                        "/user/registry",
                        "/admin/login",
                        "/admin/registry",
                        "/merchant/login",
                        "/merchant/registry"
                );
    }
}