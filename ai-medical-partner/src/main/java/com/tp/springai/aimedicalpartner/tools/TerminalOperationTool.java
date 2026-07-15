package com.tp.springai.aimedicalpartner.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;

/**
 * 包名称：com.tp.springai.aimedicalpartner.tools
 * 类名称：TerminalOperationTool
 * 类描述：终端操作工具
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/15 9:53
 */
@Component
public class TerminalOperationTool implements AiTool{

    @Tool(description = "Execute a command in the terminal")
    public String executeTerminalCommand(@ToolParam(description = "Command to execute in the terminal") String[] command) {
        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new  ProcessBuilder(command);
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                output.append("Command execution failed with exit code: ").append(exitCode);
            }
        } catch (Exception e) {
            output.append("Error executing command: ").append(e.getMessage());
        }
        return output.toString();
    }
}