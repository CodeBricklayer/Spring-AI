package com.tp.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 包名称：com.tp.config
 * 类名称：LLMConfig
 * 类描述：大模型配置
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/24 14:37
 */
@Configuration
public class LLMConfig {

    @Bean
    public ChatClient chatClient(ChatModel dashscopeChatModel) {
        return ChatClient.builder(dashscopeChatModel).build();
    }
}