package com.tp.springai.aimedicalpartner.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebScrapingToolTest {

    @Test
    void scrapingWeb() {
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        System.out.println(webScrapingTool.scrapingWeb("https://www.baidu.com"));
    }
}