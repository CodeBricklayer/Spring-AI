package com.tp.springai.aimedicalpartner.rag;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.ai.document.Document;

/**
 * 包名称：com.tp.springai.aimedicalpartner.rag
 * 接口名称：KnowledgeParser
 * 接口描述：知识库解析
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/7 14:21
 */
public interface KnowledgeParser {


    /**
     * 判断是否支持当前JSON格式
     *
     * @param node json节点
     * @return 是否支持
     */
    boolean support(JsonNode node);


    /**
     * 转换Document
     *
     * @param node     json节点
     * @param fileName 文件名称
     * @return Document列表
     */
    Document parse(
            JsonNode node,
            String fileName
    );
}
