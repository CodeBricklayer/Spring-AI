package com.tp.config;

import com.tp.advisor.MySimpleLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 包名称：com.tp.config
 * 类名称：AiConfig
 * 类描述：ai配置类
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/6/25 16:21
 */
@Configuration
public class AiConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                // 设置消息窗口大小为10
                .maxMessages(10)
                // 内存存储
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
    }

    @Bean
    public ChatClient chatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient
                // 创建ChatClient对象，以及设置模型为model
                .builder(model)
                // 添加一个日志拦截器
//                .defaultAdvisors(new MySimpleLoggerAdvisor())
                // 添加一个内置的日志拦截器
                .defaultAdvisors(new SimpleLoggerAdvisor(),
                        // 添加一个聊天记录拦截器
                        MessageChatMemoryAdvisor.builder(chatMemory).build()

                )
                .build();
    }

    @Bean
    public ChatClient chatClient2(OllamaChatModel model) {
        return ChatClient
                .builder(model)
                .defaultAdvisors(new MySimpleLoggerAdvisor())
                .build();
    }

    @Bean
    public ChatClient chatClient3(OpenAiChatModel model) {

        // System Prompt工程：清晰定义AI的角色、任务、约束和输出格式
        String systemPrompt = """
                你是一个资深的Java技术顾问。
                禁止回答任何非技术类问题，例如天气或娱乐八卦。
                代码示例必须符合Java17+规范。
                回答需要符合以下格式：首先一句话概括问题的核心，然后提供代码示例，最后补充注意事项。
                如果自己不确定，可以说“关于这个问题，我目前没有确切的信息”，禁止编造内容
                """;
        return ChatClient
                // 创建ChatClient对象，以及设置模型为model
                .builder(model)
                .defaultSystem(systemPrompt)
                // 添加一个日志拦截器
//                .defaultAdvisors(new MySimpleLoggerAdvisor())
                // 添加一个内置的日志拦截器
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

}