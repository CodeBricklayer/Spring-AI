package com.tp.springai.aicodehelper.ai;

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
}