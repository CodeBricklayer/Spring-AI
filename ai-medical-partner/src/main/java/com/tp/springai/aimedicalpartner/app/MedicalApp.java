package com.tp.springai.aimedicalpartner.app;

import com.tp.springai.aimedicalpartner.advisor.MyLoggerAdvisor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 包名称：com.tp.springai.aimedicalpartner.app
 * 类名称：MedicalApp
 * 类描述：医疗助手方法
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/6 11:21
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MedicalApp {

    private static final String REPORT_PROMPT_SUFFIX = "每次对话后都要生成医疗结果，标题为{用户名}的医疗报告，内容为问答列表";

    private final ChatClient client;

    private final MedicalAppConfig medicalAppConfig;

    private final RetrievalAugmentationAdvisor vectorStoreDocumentRetriever;

    /**
     * AI 基础对话（支持多轮对话记忆）
     *
     * @param message 用户消息
     * @param chatId  会话ID
     * @return 对话结果
     */
    public String doChat(String message, String chatId) {

        ChatResponse chatResponse = client.prompt()
                .user(message)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatId))
                .call().chatResponse();
        assert chatResponse != null;
        String content = Objects.requireNonNull(chatResponse.getResult()).getOutput().getText();
        log.info("content:{}", content);
        return content;
    }

    public record MedicalReport(String title, List<String> suggestions) {
    }

    /**
     * AI 医疗报告功能（实战结构化输出）
     *
     * @param message 用户消息
     * @param chatId  会话ID
     * @return 医疗报告
     */
    public MedicalReport doChatWithReport(String message, String chatId) {
        MedicalReport medicalReport = client.prompt()
                .system(medicalAppConfig.systemPrompt() + REPORT_PROMPT_SUFFIX)
                .user(message)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatId))
                .call().entity(MedicalReport.class);
        log.info("MedicalReport:{}", medicalReport);
        return medicalReport;
    }

    /**
     * 和 RAG 知识库进行对话
     *
     * @param message 用户消息
     * @param chatId  会话ID
     * @return 问答结果
     */
    public String doChatWithRag(String message, String chatId) {
        return client.prompt()
                .user(message)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatId))
                //开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                // 应用 RAG 知识库问答
                .advisors(vectorStoreDocumentRetriever)
                .call()
                .content();
    }

}
