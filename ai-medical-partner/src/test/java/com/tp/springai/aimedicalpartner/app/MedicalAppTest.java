package com.tp.springai.aimedicalpartner.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class MedicalAppTest {

    @Resource
    private MedicalApp medicalApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        //第一轮
        String message = medicalApp.doChat("你好，我是程序员谭鹏", chatId);
        Assertions.assertNotNull(message);
        //第二轮
        message = medicalApp.doChat("我现在有点咳嗽", chatId);
        Assertions.assertNotNull(message);
        //第三轮
        message = medicalApp.doChat("我刚刚有点什么问题来着？刚跟你说过，帮我回忆一下", chatId);
        Assertions.assertNotNull(message);
    }

    @Test
    void testChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        MedicalApp.MedicalReport medicalReport = medicalApp.doChatWithReport("你好，我是程序员谭鹏,我现在有点咳嗽，但我不知道该怎么做", chatId);
        Assertions.assertNotNull(medicalReport);
    }
}