package com.tp.controller;

import com.tp.util.DocumentParseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 包名称：com.tp.controller
 * 类名称：MyRagController
 * 类描述：向量知识库的模拟
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/1 9:59
 */
@RestController
@RequiredArgsConstructor
public class MyRagController {

    /**
     * 向量存储
     */
    private final VectorStore vectorStore;

    /**
     * 文本分块器
     */
    private final TokenTextSplitter tokenTextSplitter;

    /**
     * 文档解析工具
     */
    private final DocumentParseUtil documentParseUtil;

    /**
     * 会话客户端
     */
    private final ChatClient chatClient;

    /**
     * 添加文档
     *
     * @return 是否成功
     */
    @RequestMapping("/addDocs")
    public String addDocs() {
        addChunkedDocument("D:\\files\\own\\code\\SpringAiLearn\\SpringAiLearn\\upload\\tp.docx", "百度简介.docx");
        addChunkedDocument("D:\\files\\own\\code\\SpringAiLearn\\SpringAiLearn\\upload\\tp.md", "哔哩哔哩简介.md");
        addChunkedDocument("D:\\files\\own\\code\\SpringAiLearn\\SpringAiLearn\\upload\\tp.pdf", "腾讯简介.pdf");
        addChunkedDocument("D:\\files\\own\\code\\SpringAiLearn\\SpringAiLearn\\upload\\tp.txt", "java学习.txt");
        return "OK";
    }

    /**
     * 查询文档
     *
     * @return 是否成功
     */
    @RequestMapping("/queryDocs")
    public String queryDocs() {
        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("介绍下百度")
                        .topK(3).build()
        );
        System.out.println(docs);
        return "OK";
    }

    /**
     * 知识库问答
     *
     * @return 是否成功
     */
    @RequestMapping("/ask_rag")
    public String askRag() {
        QuestionAnswerAdvisor qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        // 搜索结果数量
                        .topK(3)
                        // 相似度阈值0-1 值越大要求越严格 过滤低相关性的结果，提高RAG的回答质量
                        .similarityThreshold(0.7).build()).build();
        String result = chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "default")).advisors(qaAdvisor).user("介绍下百度").call().content();
        System.out.println(result);
        return "OK";
    }

    /**
     * 解析后按token切分再入库，避免单条Document超过嵌入模型输入上限
     *
     * @param filePath 文档路径
     * @param label    标签
     */
    private void addChunkedDocument(String filePath, String label) {
        List<Document> raw = documentParseUtil.parse(filePath);
        List<Document> chunks = tokenTextSplitter.apply(raw);
        vectorStore.add(chunks);
        System.out.println(label + ",向量存储添加成功！（共" + chunks.size() + "块");
    }
}