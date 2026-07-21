package com.tp.springai.oldversionlearnai.entity;

import lombok.Data;

/**
 * 包名称：com.tp.springai.oldversionlearnai.entity
 * 类名称：ChatRequest
 * 类描述：请求参数
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/21 10:37
 */
@Data
public class ChatRequest {

    /**
     * 请求消息
     */
    private String message;
}