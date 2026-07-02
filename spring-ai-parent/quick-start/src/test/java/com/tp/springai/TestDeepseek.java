package com.tp.springai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 包名称：com.tp.springai
 * 类名称：TestDeepseek
 * 类描述：测试DeepSeek
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/2 12:58
 */
@SpringBootTest
public class TestDeepseek {

    @Test
    public void testDeepseek(@Autowired DeepSeekChatModel deepSeekChatModel){
        String content = deepSeekChatModel.call("你好，你是谁");
        System.out.println(content);
    }

    @Test
    public void testDeepseekStream(@Autowired DeepSeekChatModel deepSeekChatModel){
        Flux<String> stream = deepSeekChatModel.stream("你好，你是谁");
        stream.toIterable().forEach(System.out::println);
    }

    @Test
    public void testOptions(@Autowired DeepSeekChatModel deepSeekChatModel){
        DeepSeekChatOptions options = DeepSeekChatOptions.builder().model("deepseek-chat")
                .temperature(1.9d)
                // 字数
//                .maxTokens(5)
                // 截断不想输出的内容
                .stop(List.of("，"))
                .build();

        Prompt prompt = new Prompt("请写一句诗描述清晨。", options);

        ChatResponse res = deepSeekChatModel.call(prompt);
        System.out.println(Objects.requireNonNull(res.getResult()).getOutput().getText());
    }

    @Test
    public void testDeepseekReasoning(@Autowired DeepSeekChatModel deepSeekChatModel){
        Prompt prompt = new Prompt("你好你是谁");
        ChatResponse response = deepSeekChatModel.call(prompt);
        DeepSeekAssistantMessage message = (DeepSeekAssistantMessage) Objects.requireNonNull(response.getResult()).getOutput();
        String reasoningContent = message.getReasoningContent();
        System.out.println(reasoningContent);
        System.out.println("------------------");
        System.out.println(message.getText());
    }

    @Test
    public void testDeepseekStreamReasoning(@Autowired DeepSeekChatModel deepSeekChatModel){
        Prompt prompt = new Prompt("你好你是谁");
        Flux<ChatResponse> response = deepSeekChatModel.stream(prompt);
        response.toIterable().forEach(chatResponse -> {
            DeepSeekAssistantMessage message = (DeepSeekAssistantMessage) Objects.requireNonNull(chatResponse.getResult()).getOutput();
            String reasoningContent = message.getReasoningContent();
            System.out.println(reasoningContent);
            System.out.println("------------------");
            System.out.println(message.getText());
        });
    }
}