package com.tp.springai.aimedicalpartner.app;

import com.tp.springai.aimedicalpartner.advisor.MyLoggerAdvisor;
import com.tp.springai.aimedicalpartner.chatmemory.FileBasedChatMemory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 包名称：com.tp.springai.aimedicalpartner.app
 * 类名称：MedicalAppConfig
 * 类描述：医疗助手配置
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/6 11:10
 */
@Component
public class MedicalAppConfig {

    private final Resource systemPromptResource;

    private volatile String systemPrompt;

    /**
     * AI 医疗助手知识库问答功能
     */
    @jakarta.annotation.Resource
    private VectorStore vectorStore;

    public MedicalAppConfig(@Value("classpath:prompts/system-message.st") Resource systemPromptResource) {
        this.systemPromptResource = systemPromptResource;
    }

    @Bean
    public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor() {
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.50)
                        .vectorStore(vectorStore)
                        .build())
                .queryAugmenter(ContextualQueryAugmenter.builder()
                        .allowEmptyContext(true)
                        .build())
                .build();
    }

    @Bean
    public QuestionAnswerAdvisor qaAdvisor() {
        return QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        // 搜索结果数量
                        .topK(3)
                        // 相似度阈值0-1 值越大要求越严格 过滤低相关性的结果，提高RAG的回答质量
                        .similarityThreshold(0.7).build()).build();
    }

    @Bean
    public ChatMemory chatMemory() {
        String fileDir = System.getProperty("user.dir") + "/temp/chat-memory";
        return new FileBasedChatMemory(fileDir);
    }

    public String systemPrompt() {
        String cached = this.systemPrompt;
        if (cached != null) {
            return cached;
        }

        synchronized (this) {
            if (this.systemPrompt == null) {
                this.systemPrompt = readSystemPrompt();
            }
            return this.systemPrompt;
        }
    }

    @Bean
    public ChatClient chatClient(OllamaChatModel model, ChatMemory chatMemory) {
        return ChatClient
                // 创建ChatClient对象，以及设置模型为model
                .builder(model)
                .defaultSystem(systemPrompt())
                // 添加一个内置的日志拦截器
                .defaultAdvisors(
                        // 添加一个聊天记录拦截器
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        // 自定义日志 Advisor，可按需开启
                        new MyLoggerAdvisor()
                        // 自定义推理增强 Advisor，可按需开启
//                        , new MyReReadingAdvisor()
                )
                .build();
    }

    private String readSystemPrompt() {
        try (var inputStream = systemPromptResource.getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read prompts/system-message.st", e);
        }
    }

    /**
     * 查询扩展器：在多轮会话场景中，用户输入的提示词‌有时可能不够完整，或者存在歧义。多查询扩展技术可以扩大检索范围，提‌高相关文档的召回率。
     *
     * @param ollamaChatModel 模型
     * @return 查询扩展器
     */
    @Bean
    public MultiQueryExpander myMultiQueryExpander(ChatModel ollamaChatModel) {
        return MultiQueryExpander.builder()
                .chatClientBuilder(ChatClient.builder(ollamaChatModel))
                .numberOfQueries(3)
                .build();
    }

    /**
     * 查询重写器：查询重写可以使查询更加‌精确和专业，但是注意保持查询的语义完整性。
     *
     * @param ollamaChatModel 模型
     * @return 查询重写器
     */
    @Bean
    public QueryTransformer queryTransformer(ChatModel ollamaChatModel) {
        return RewriteQueryTransformer.builder()
                .chatClientBuilder(ChatClient.builder(ollamaChatModel))
                .build();
    }
}
