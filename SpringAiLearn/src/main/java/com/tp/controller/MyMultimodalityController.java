package com.tp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 包名称：com.tp.controller
 * 类名称：MyMultimodalityController
 * 类描述：多模态支持模拟
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/2 11:36
 */
@RestController
@RequiredArgsConstructor
public class MyMultimodalityController {

    private final ChatClient chatClient;

    /**
     * 多模态模拟
     * @return 是否成功
     */
    @GetMapping("/analyze-image")
    public String analyzeImage()
    {
        //流产式API构建多模态请求
        String result = chatClient.prompt()
                .user(u->
                        u.text("请详细描述这张图片的内容，包含物体、颜色、场景和可能得用途")
                                .media(MediaType.IMAGE_JPEG,new FileSystemResource("D:\\files\\own\\code\\SpringAiLearn\\SpringAiLearn\\upload\\Apple.jpg")))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "default"))
                .call()
                .content();
        System.out.println(result);
        return "OK";
    }
}