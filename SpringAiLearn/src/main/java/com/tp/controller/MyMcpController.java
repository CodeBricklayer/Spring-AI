package com.tp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 包名称：com.tp.controller
 * 类名称：MyMcpController
 * 类描述：mcp-client模拟
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/2 11:20
 */
@RestController
@RequiredArgsConstructor
public class MyMcpController {

    private final ChatClient chatClient;

    private final SyncMcpToolCallbackProvider mcpToolCallbackProvider;

    /**
     * 通过mcp查询天气
     *
     * @return 是否查询成功
     */
    @RequestMapping("/ai/mcp")
    public String aiWithMcp() {
        String q = "查一下上海天气";
        String result = chatClient.prompt()
                .system("用户查询天气时，你必须调用工具查询后再用简短中文回答。")
                .user(q).tools(mcpToolCallbackProvider)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "default"))
                .call()
                .content();
        System.out.println(result);
        return "OK";
    }
}