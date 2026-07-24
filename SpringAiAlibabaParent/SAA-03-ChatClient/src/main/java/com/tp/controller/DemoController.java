package com.tp.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 包名称：com.tp.controller
 * 类名称：DemoController
 * 类描述：
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/24 14:31
 */
@RestController
@RequestMapping("/ai")
public class DemoController {
    @Resource
    private ChatClient client;

    @GetMapping("/chat/client/chat")
    public String doChat(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return client.prompt().user(question).call().content();
    }

}