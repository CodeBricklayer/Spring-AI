package com.tp.springai.aicodehelper.controller;

import com.tp.springai.aicodehelper.ai.AiCodeHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 包名称：com.tp.springai.aicodehelper.controller
 * 类名称：AiController
 * 类描述：ai对话
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/3 10:17
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiCodeHelperService codeHelperService;

    /**
     * 流式响应对话
     *
     * @param memoryId 会话id
     * @param message  消息
     * @return 响应结果
     */
    @GetMapping("/chatStream")
    public Flux<ServerSentEvent<String>> chatStream(int memoryId, String message) {
        return codeHelperService.chatStream(memoryId, message).map(chunk ->
                ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }
}