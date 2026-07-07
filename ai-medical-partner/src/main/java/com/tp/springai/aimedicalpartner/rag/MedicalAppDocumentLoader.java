package com.tp.springai.aimedicalpartner.rag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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

    private final List<KnowledgeParser> parsers;

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

                ObjectMapper objectMapper = new ObjectMapper();

                String content =
                        Files.readString(
                                resource.getFile().toPath()
                        ).trim();

                JsonNode root = objectMapper.readTree(resource.getInputStream());

                List<JsonNode> nodes = new ArrayList<>();


                // JSON数组
                if (content.startsWith("[")) {

                    ArrayNode array =
                            (ArrayNode) objectMapper.readTree(content);

                    array.forEach(nodes::add);

                }
                // JSONL
                else {

                    try (BufferedReader reader =
                                 Files.newBufferedReader(
                                         resource.getFile().toPath()
                                 )) {

                        String line;

                        while ((line = reader.readLine()) != null) {

                            if (!line.isBlank()) {

                                nodes.add(
                                        objectMapper.readTree(line)
                                );
                            }
                        }
                    }
                }

                List<Document> result = new ArrayList<>();
                String filename = resource.getFilename();
                for (JsonNode node : nodes) {
                    KnowledgeParser parser = parsers.stream().filter(p -> p.support(node)).findFirst().orElseThrow(() -> new RuntimeException("未知JSON格式"));
                    result.add(parser.parse(node, filename));
                }

                allDocuments.addAll(result);
            }
        } catch (IOException e) {
            log.error("Error loading Json Documents", e);
        }
        return allDocuments;
    }
}