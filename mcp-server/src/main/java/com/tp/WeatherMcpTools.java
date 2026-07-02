package com.tp;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

/**
 * 包名称：com.tp
 * 类名称：WeatherMcpTools
 * 类描述：天气MCP工具类
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/2 11:04
 */
@Component
public class WeatherMcpTools {

    @McpTool(
            name = "getWeatherForecast",
            description = "根据城市名称查询模拟天气预报（演示数据，非真实气象来源）",
            generateOutputSchema = true
    )
    public String getWeatherForecast(@McpToolParam(description = "城市名称，例如：北京、上海...", required = true) String city) {
        System.out.println("执行到天气预报MCP-SERVER");
        return city + " 天气晴 空气质量优";
    }
}