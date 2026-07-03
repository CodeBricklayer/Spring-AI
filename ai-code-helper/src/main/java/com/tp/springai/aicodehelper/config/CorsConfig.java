package com.tp.springai.aicodehelper.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 包名称：com.tp.springai.aicodehelper.config
 * 类名称：CorsConfig
 * 类描述：全局跨域配置
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/3 10:25
 */
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 覆盖所有请求
        registry.addMapping("/**")
                // 允许发送 Cookie
                .allowCredentials(true)
                // 放行哪些域名(必须用 patterns，否则 * 会和 allowCredentials 冲突)
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}