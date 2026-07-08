package com.tp.springai.aimedicalpartner.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 包名称：com.tp.springai.aimedicalpartner.rag
 * 类名称：MyKeywordEnricher
 * 类描述：基于AI的文档元信息增强器（为文档补充元信息）
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/8 16:02
 */
@Component
public class MyKeywordEnricher {

    @Resource
    private ChatModel ollamaChatModel;

    /**
     * 补充元信息增强器
     * @param documents 文档
     * @return 补充元信息后的文档
     */
    public List<Document> enrichDocuments(List<Document> documents) {
        KeywordMetadataEnricher enricher = KeywordMetadataEnricher.builder(ollamaChatModel)
                .keywordCount(5)
                .build();

        // Or use custom templates
//        KeywordMetadataEnricher enricher = KeywordMetadataEnricher.builder(ollamaChatModel)
//                .keywordsTemplate(YOUR_CUSTOM_TEMPLATE)
//                .build();

        return enricher.apply(documents);
    }
}