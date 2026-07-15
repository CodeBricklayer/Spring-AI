package com.tp.springai.aimedicalpartner.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDownloadToolTest {

    @Test
    void downloadResource() {
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        String url = "https://www.baidu.com/img/PCfb_5bf082d29588c07f842ccde3f97243ea.png";
        String name = "logo.png";
        System.out.println(resourceDownloadTool.downloadResource(url, name));
    }
}