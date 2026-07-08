package com.tp.springai.aimedicalpartner.rag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonMetadataGenerator;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 包名称：com.tp.springai.aimedicalpartner.rag
 * 类名称：LoveAppDocumentLoader
 * 类描述：医疗助手文件加载器
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/7 12:23
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MedicalAppDocumentLoader {

    private final ResourcePatternResolver resolver;

    /**
     * 加载多篇 Markdown 文档
     *
     * @return Markdown文档内容
     */
    public List<Document> loadMarkdowns() {
        List<Document> allDocuments = new ArrayList<>();
        try {
            Resource[] resources = resolver.getResources("classpath:docs/*.md");
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                MarkdownDocumentReaderConfig config = null;
                if (fileName != null) {
                    config = MarkdownDocumentReaderConfig.builder().withHorizontalRuleCreateDocument(true).withIncludeCodeBlock(false).withIncludeBlockquote(false).withAdditionalMetadata("filename", fileName).build();
                }
                MarkdownDocumentReader reader = null;
                if (config != null) {
                    reader = new MarkdownDocumentReader(resource, config);
                }
                if (reader != null) {
                    allDocuments.addAll(reader.get());
                }
            }
        } catch (IOException e) {
            log.error("Error loading Markdown Documents", e);
        }
        return allDocuments;
    }

    /**
     * 加载多篇 json 文档
     *
     * @return json内容
     */
    public List<Document> loadJson() {
        List<Document> allDocuments = new ArrayList<>();
        try {
            Resource[] resources = resolver.getResources("classpath:docs/*.json");
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename != null) {
                    JsonMetadataGenerator jsonMetadataGenerator = new JsonMetadataGenerator() {
                        @Override
                        public @NonNull Map<String, Object> generate(@NonNull Map<String, Object> jsonMap) {
                            return Map.of("filename", filename);
                        }
                    };
                    JsonReader reader = new JsonReader(resource, jsonMetadataGenerator);
                    allDocuments.addAll(reader.get());
                }
            }
        } catch (IOException e) {
            log.error("Error loading Json Documents", e);
        }
        return allDocuments;
    }
}