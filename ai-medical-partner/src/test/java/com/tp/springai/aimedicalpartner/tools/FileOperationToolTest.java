package com.tp.springai.aimedicalpartner.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileOperationToolTest {

    @Test
    void readFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "医疗问答.txt";
        String readFile = fileOperationTool.readFile(fileName);
        System.out.println(readFile);
    }

    @Test
    void writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "医疗问答.txt";
        String content = "我不知道你在问什么医疗知识";
        String result = fileOperationTool.writeFile(fileName,content);
        System.out.println(result);
    }
}