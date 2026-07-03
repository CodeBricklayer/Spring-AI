package com.tp.springai.aiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

/**
 * 包名称：com.tp.springai.aiagent.demo.invoke
 * 类名称：LangChainAiInvoke
 * 类描述：阿里云灵积AI langChain4j调用
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/3 16:45
 */
public class LangChainAiInvoke {

    public static void main(String[] args) {
        ChatLanguageModel qwenChatModel = QwenChatModel.builder()
                .baseUrl("https://ws-sk5flpqqtym5x3nv.cn-beijing.maas.aliyuncs.com/api/v1")
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("qwen-plus")
                .build();
        String result = qwenChatModel.chat("我是一名java开发工程师");
        System.out.println(result);
    }
}