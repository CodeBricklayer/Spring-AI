package com.tp.springai.oldversionlearnai.controller;

import com.tp.springai.oldversionlearnai.entity.ChatRequest;
import com.tp.springai.oldversionlearnai.entity.Result;
import com.tp.springai.oldversionlearnai.service.QwenChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 包名称：com.tp.springai.oldversionlearnai.controller
 * 类名称：ChatController
 * 类描述：会话接口
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/21 10:39
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final QwenChatService qwenChatService;

    /**
     * 单轮对话接口
     */
    @PostMapping("/send")
    public Result<String> chat(@RequestBody ChatRequest request) {
        return Result.success(qwenChatService.singleChat(request.getMessage()));
    }
}