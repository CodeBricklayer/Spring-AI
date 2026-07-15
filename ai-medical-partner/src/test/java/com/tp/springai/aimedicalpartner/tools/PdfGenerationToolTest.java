package com.tp.springai.aimedicalpartner.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfGenerationToolTest {

    @Test
    void generatePdf() {
        PdfGenerationTool pdfGenerationTool = new PdfGenerationTool();
        String fileName = "AI学习.pdf";
        String content = "这是一个关于学习AI编程的一个项目。";
        System.out.println(pdfGenerationTool.generatePdf(fileName, content));
    }
}