package com.tp.springai.oldversionlearnai.entity;

import lombok.Data;

/**
 * 包名称：com.tp.springai.oldversionlearnai.entity
 * 类名称：Result
 * 类描述：响应体
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/21 10:36
 */
@Data
public class Result<T> {
    /**
     * 响应结果码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应结果
     */
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("success");
        result.setData(data);
        return result;
    }
}