package com.tp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 包名称：com.tp.config
 * 类名称：CorsConfig
 * 类描述：跨域配置
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/23 16:02
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/ai/**")
                // 允许所有来源，本地调试用
                .allowedOriginPatterns("*")
                .allowedMethods("GET","POST","OPTIONS")
                .allowCredentials(false)
                .maxAge(3600);
    }
}