package com.tp.springai.aicodehelper.ai;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 包名称：com.tp.springai.aicodehelper.ai
 * 类名称：AiCodeHelper
 * 类描述：编码助手简单实现类
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/2 16:13
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AiCodeHelper {

    private final ChatModel qwenChatModel;

    private final static String SYSTEM_MESSAGE = """
            你是编程领域的小助手，帮助用户解答编程学习和求职面试相关的问题，并给出建议。重点关注 4 个方向:
            1. 规划清晰的编程学习路线
            2. 提供项目学习建议
            3. 给出程序员求职全流程指南(比如简历优化、投递技巧)
            4. 分享高频面试题和面试技巧
            请用简洁易懂的语言回答，助力用户高效学习与求职。
            """;

    /**
     * 简单对话-文本对话
     *
     * @param message 用户请求消息
     * @return 问答结果
     */
    public String chat(String message) {
        UserMessage userMessage = UserMessage.from(message);
        return chatWithMessage(userMessage);
    }

    /**
     * 简单对话-自定义用户消息
     *
     * @param userMessage 用户请求消息
     * @return 问答结果
     */
    public String chatWithMessage(UserMessage userMessage) {
        // 系统预设
        SystemMessage systemMessage = SystemMessage.from(SYSTEM_MESSAGE);
        ChatResponse chatResponse = qwenChatModel.chat(systemMessage, userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        log.info("AI 输出：{}", aiMessage.toString());
        return aiMessage.text();
    }
}