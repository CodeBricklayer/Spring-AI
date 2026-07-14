package com.tp.springai.aimedicalpartner.tools;

import cn.hutool.core.io.FileUtil;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.util.List;

/**
 * 包名称：com.tp.springai.aimedicalpartner.tools
 * 类名称：WebScrapingTool
 * 类描述：网页抓取工具
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/10 17:49
 */
public class WebScrapingTool {

    @Tool(description = "This is a tool for scraping web page information.")
    public String scrapingWeb(@ToolParam(description = "URLs from which web information needs to be scraped.") String url) {
        try {
            Resource resource = new UrlResource(url);
            String fileName = url.substring(url.lastIndexOf("/") - 1);
            JsoupDocumentReaderConfig config = JsoupDocumentReaderConfig.builder()
                    // Extract paragraphs within <article> tags
//                    .selector("article p")
                    // Use ISO-8859-1 encoding
                    .charset("UTF-8")
                    // Include link URLs in metadata
                    .includeLinkUrls(true)
                    // Extract author and date meta tags
                    .metadataTags(List.of("author", "date"))
                    // Add custom metadata
                    .additionalMetadata("source", fileName)
                    .build();

            JsoupDocumentReader reader = new JsoupDocumentReader(resource, config);
            return reader.get().getFirst().getFormattedContent();
        } catch (Exception e) {
            return "Error reading file:" + e.getMessage();
        }
    }
}