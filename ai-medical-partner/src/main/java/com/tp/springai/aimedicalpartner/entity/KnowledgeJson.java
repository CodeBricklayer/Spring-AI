package com.tp.springai.aimedicalpartner.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 包名称：com.tp.springai.aimedicalpartner.entity
 * 类名称：KnowledgeJson
 * 类描述：json数据集
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/7 13:55
 */
@Data
public class KnowledgeJson {

    /**
     * 消息内容
     */
    private List<Message> messages;

    /**
     * 元数据信息
     */
    private Map<String, Object> metadata;


    @Data
    public static class Message {

        /**
         * 角色
         */
        private String role;

        /**
         * 消息
         */
        private String content;
    }
}