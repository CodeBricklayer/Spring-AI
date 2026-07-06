package com.tp.springai.aiagent.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 包名称：com.tp.springai.aiagent.demo.invoke
 * 类名称：OllamaAiInvoke
 * 类描述：Spring AI 框架调用AI大模型（阿里）
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/3 17:24
 */
//@Component
public class OllamaAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel ollamaChatModel;

    @Override
    public void run(String... args) {
        AssistantMessage assistantMessage = ollamaChatModel.call(new Prompt("你好，我是一名java程序员"))
                .getResult()
                .getOutput();
        System.out.println(assistantMessage.getText());
    }
}