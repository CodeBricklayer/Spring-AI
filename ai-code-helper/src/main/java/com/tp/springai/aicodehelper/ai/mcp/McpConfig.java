package com.tp.springai.aicodehelper.ai.mcp;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 包名称：com.tp.springai.aicodehelper.ai.mcp
 * 类名称：McpConfig
 * 类描述：mcp配置类
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/3 9:49
 */
@Configuration
@RequiredArgsConstructor
public class McpConfig {

    @Value("${bigmodel.api-key:}")
    private String apiKey;

    @Bean
    public McpToolProvider mcpToolProvider() {
        // 和 MCP 服务通讯
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("https://open.bigmodel.cn/api/mcp/web_search/sse?Authorization=" + apiKey)
                // 开启日志，查看更多消息
                .logRequests(true)
                .logResponses(true)
                .build();

        // 创建 MCP 客户端
        McpClient mcpClient = new DefaultMcpClient.Builder()
                .key("tpMcpClient")
                .transport(transport)
                .build();

        // 从 MCP 客户端获取工具
        return McpToolProvider.builder()
                .mcpClients(mcpClient)
                .build();
    }
}