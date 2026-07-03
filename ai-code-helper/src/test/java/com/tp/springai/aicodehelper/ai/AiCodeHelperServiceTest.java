package com.tp.springai.aicodehelper.ai;

import dev.langchain4j.service.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeHelperServiceTest {

    @Resource
    private AiCodeHelperService aiCodeHelperService;

    @Test
    void chat() {
        System.out.println(aiCodeHelperService.chat("你好，我是一个java程序员"));
    }

    @Test
    void chatWithMemory() {
        System.out.println(aiCodeHelperService.chat("你好，我是一个java程序员"));
        System.out.println(aiCodeHelperService.chat("你好，我是干什么的来着？"));
    }

    @Test
    void chatForReport() {
        String userMessage = "你好，我是程序员谭鹏，学习编程两年半，请帮我制定学习报告";
        System.out.println(aiCodeHelperService.chatForReport(userMessage));
    }

    @Test
    void chatWithRag() {
        Result<String> result = aiCodeHelperService.chatWithRag("常量定义规范有哪些");
        System.out.println(result.sources());
        System.out.println(result.content());
    }

    @Test
    void chatWithTools() {
        System.out.println(aiCodeHelperService.chat("有哪些常用的计算机网络面试题"));
    }

    @Test
    void chatWithMcp() {
        System.out.println(aiCodeHelperService.chat("什么是哔哩哔哩网站"));
    }

    /**
     * 测试敏感词
     */
    @Test
    void chatWithGuardrail() {
        System.out.println(aiCodeHelperService.chat("kill the game"));
    }
}