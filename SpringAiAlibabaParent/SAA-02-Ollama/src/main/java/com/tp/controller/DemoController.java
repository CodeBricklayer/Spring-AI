package com.tp.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 包名称：com.tp.controller
 * 类名称：DemoController
 * 类描述：
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/24 9:53
 */
@RestController
@RequestMapping("/ai")
public class DemoController {

    @Resource
    private ChatModel ollamaChatModel;

    @GetMapping("/ollama/streamChat")
    public Flux<String> doChatWithStream(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        Prompt prompt = new Prompt(question);
        return ollamaChatModel.stream(prompt)
                .map(resp -> resp.getResult().getOutput().getText())
                .filter(text -> text != null && !text.isBlank())
                // AI输出完成后，追加一条结束信号
                .concatWithValues("[DONE]");
    }
}