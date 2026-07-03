package com.tp.springai.aiagent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 包名称：com.tp.springai.aiagent.controller
 * 类名称：HealthController
 * 类描述：健康检查
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/3 14:56
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public String healthCheck() {
        return "OK";
    }
}