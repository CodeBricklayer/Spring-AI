package com.tp.util;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * 包名称：com.tp.util
 * 类名称：DocumentParseUtil
 * 类描述：多格式文档解析工具类
 * 支持：PDF、DOC（DOCX）、TXT/TEXT、MD
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/1 9:37
 */
@Component
public class DocumentParseUtil {

    /**
     * 文档解析
     *
     * @param filePath 文件路径
     * @return 文件内容
     */
    public List<Document> parse(String filePath) {
        File file = new File(filePath);
        String suffix = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
        Resource resource = new FileSystemResource(filePath);
        DocumentReader reader = switch (suffix) {
            case "pdf", "doc", "docx", "txt", "text" -> new TikaDocumentReader(resource);
            case "md", "markdown" -> new MarkdownDocumentReader(file.toURI().toString());
            default -> throw new IllegalArgumentException("不支持的文件格式：" + suffix);
        };
        return reader.get();
    }
}