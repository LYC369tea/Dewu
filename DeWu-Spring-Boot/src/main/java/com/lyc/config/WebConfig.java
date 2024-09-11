package com.lyc.config;


import com.lyc.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录接口和注册接口不拦截
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/0", "/user/1", "/data/**");
    }


    @Bean
    public CorsFilter corsFilter() {
        // 创建一个基于 URL 的 CorsConfigurationSource 实例
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 创建一个 CorsConfiguration 实例
        CorsConfiguration config = new CorsConfiguration();
        // 允许的域，可以设置为你的前端地址
        config.addAllowedOrigin("*");
        // 允许的请求头部，此处设置为允许所有头部
        config.addAllowedHeader("*");
        // 允许的请求方法，此处设置为允许所有方法
        config.addAllowedMethod("*");
        // 将 CorsConfiguration 实例注册到 UrlBasedCorsConfigurationSource 中，使用通配符允许所有路径跨域访问
        source.registerCorsConfiguration("/**", config);
        // 返回 CorsFilter 实例
        return new CorsFilter(source);
    }
}
