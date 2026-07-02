package com.tp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 包名称：com.tp.controller
 * 类名称：MyEmController
 * 类描述：用于向量模拟
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/6/30 10:28
 */
@RestController
@RequiredArgsConstructor
public class MyEmController {

    private final OpenAiEmbeddingModel embeddingModel;

    private final VectorStore vectorStore;

    /**
     * 向量方法
     *
     * @return 是否成功
     */
    @RequestMapping("/em")
    public String em() {
        float[] textVector1 = embeddingModel.embed("学Java上哔哩哔哩");
        float[] textVector2 = embeddingModel.embed("哔哩哔哩网站教学Java质量真不错");
        float[] textVector3 = embeddingModel.embed("我喜欢吃苹果");
        System.out.println(textVector1.length + ":" + Arrays.toString(textVector1));
        System.out.println(textVector2.length + ":" + Arrays.toString(textVector2));
        System.out.println(textVector3.length + ":" + Arrays.toString(textVector3));
        double dist12 = euclideanDistance(textVector1, textVector2);
        double dist13 = euclideanDistance(textVector1, textVector3);

        System.out.println("textVector1 与 textVector2 欧式距离： " + dist12);
        System.out.println("textVector1 与 textVector3 欧式距离： " + dist13);

        return "OK";
    }

    /**
     * 计算两个向量的欧氏距离
     *
     * @param a 向量1
     * @param b 向量2
     * @return 欧式距离
     */
    private static double euclideanDistance(float[] a, float[] b) {
        if (ObjectUtils.nullSafeEquals(a.length, b.length)) {
            throw new IllegalArgumentException("向量维度不一致");
        }
        double sumSeq = 0;
        for (int i = 0; i < a.length; i++) {
            double d = a[i] - b[i];
            sumSeq += d * d;
        }
        return Math.sqrt(sumSeq);
    }

    /**
     * 添加文档
     *
     * @return 是否成功
     */
    @RequestMapping("/addDoc")
    public String addDoc() {
        List<Document> docs = List.of(
                new Document("学Java上哔哩哔哩"),
                new Document("哔哩哔哩是个学Java的好地方"),
                new Document("我喜欢打篮球")
        );
        vectorStore.add(docs);
        return "OK";
    }

    /**
     * 查询文档
     *
     * @return 是否成功
     */
    @RequestMapping("/queryDoc")
    public String queryDoc() {
        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("去哪里学Java?")
                        .topK(2)
                        .build()
        );
        System.out.println(docs);
        return "OK";
    }
}