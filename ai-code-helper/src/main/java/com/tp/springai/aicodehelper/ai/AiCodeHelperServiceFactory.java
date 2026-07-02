package com.tp.springai.aicodehelper.ai;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 包名称：com.tp.springai.aicodehelper.ai
 * 类名称：AiCodeHelperServiceFactory
 * 类描述：编码助手接口工厂类
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/2 16:36
 */
@Configuration
@RequiredArgsConstructor
public class AiCodeHelperServiceFactory {

    private final ChatModel qwenChatModel;

    private final ContentRetriever contentRetriever;

    @Bean
    public AiCodeHelperService aiCodeHelperService() {
        // 会话记忆
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        // 构造AI service
        return AiServices.builder(AiCodeHelperService.class)
                .chatModel(qwenChatModel)
                // 会话记忆
                .chatMemory(chatMemory)
                // RAG检索增强生成
                .contentRetriever(contentRetriever)
                .build();
    }
}