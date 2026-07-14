package com.tp.springai.aimedicalpartner.advisor;

import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/**
 * 包名称：com.tp.springai.aimedicalpartner.advisor
 * 类名称：CustomAdvisorFactory
 * 类描述：创建自定义的 RAG 检索增强顾问的工厂
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/8 16:52
 */
public class CustomAdvisorFactory {

    /**
     * 创建自定义的 RAG 检索增强顾问
     *
     * @param vectorStore 向量存储
     * @param category    分类
     * @return 自定义的 RAG 检索增强顾问
     */
    public static Advisor createCustomAdvisor(VectorStore vectorStore, String category) {

        // 过滤特定分类的文档
        Filter.Expression expression = new FilterExpressionBuilder()
                .eq("category", category)
                .build();

        VectorStoreDocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                // 相似度阈值
                .similarityThreshold(0.50)
                .vectorStore(vectorStore)
                // 过滤条件
                .filterExpression(expression)
                // 返回文档数量
                .topK(3)
                .build();


        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .queryAugmenter(ContextualQueryAugmenterFactory.createInstance())
                .build();
    }
}