package com.tp.springai.aicodehelper.ai;

import com.tp.springai.aicodehelper.ai.guardrail.SafeInputGuardrail;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import reactor.core.publisher.Flux;

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
@InputGuardrails(SafeInputGuardrail.class)
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

    /**
     * 简单对话-依据知识库问答
     *
     * @param userMessage 用户消息
     * @return 返回封装后的结果
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    Result<String> chatWithRag(String userMessage);

    /**
     * 简单对话-流式输出对话
     *
     * @param memoryId    会话id
     * @param userMessage 用户消息
     * @return 流式输出
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    Flux<String> chatStream(@MemoryId int memoryId, @UserMessage String userMessage);
}