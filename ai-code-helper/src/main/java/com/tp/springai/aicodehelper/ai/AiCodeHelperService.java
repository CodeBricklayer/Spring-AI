package com.tp.springai.aicodehelper.ai;

import dev.langchain4j.service.SystemMessage;

import java.util.List;

/**
 * 包名称：com.tp.springai.aicodehelper.ai
 * 类名称：AiCodeHelperService
 * 类描述：变成助手接口类
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/2 16:33
 */
//@AiService
public interface AiCodeHelperService {

    /**
     * 简单对话-返回文本
     *
     * @param userMessage 用户消息
     * @return 解答结果
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String userMessage);

    /**
     * 简单对话-返回实体类
     *
     * @param userMessage 用户消息
     * @return 实体类
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    Report chatForReport(String userMessage);

    // 学习报告
    record Report(String name, List<String> suggestionList) {
    }
}