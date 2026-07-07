package com.tp.springai.aimedicalpartner.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 包名称：com.tp.springai.aimedicalpartner.rag
 * 类名称：MedicalAppVectorStoreConfig
 * 类描述：医疗助手向量数据库配置（初始化基于内存的向量数据库 Bean）
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/7 14:59
 */
@Configuration
public class MedicalAppVectorStoreConfig {

    @Resource
    private MedicalAppDocumentLoader medicalAppDocumentLoader;

    @Resource
    private VectorStore vectorStore;

    private static final int EMBEDDING_BATCH_SIZE = 10;


    @Bean
    VectorStore medicalAppVectorStore() {
        List<Document> documents = medicalAppDocumentLoader.loadJson();
        for (int i = 0; i < documents.size(); i += EMBEDDING_BATCH_SIZE) {


            int end = Math.min(
                    i + EMBEDDING_BATCH_SIZE,
                    documents.size()
            );


            List<Document> batch =
                    documents.subList(i, end);


            vectorStore.add(batch);
        }
        return vectorStore;
    }
}