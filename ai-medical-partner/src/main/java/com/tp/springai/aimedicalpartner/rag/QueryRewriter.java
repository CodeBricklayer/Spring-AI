package com.tp.springai.aimedicalpartner.rag;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.stereotype.Component;

/**
 * 包名称：com.tp.springai.aimedicalpartner.rag
 * 类名称：QueryRewriter
 * 类描述：查询重写器
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/8 16:28
 */
@Component
@RequiredArgsConstructor
public class QueryRewriter {

    private final QueryTransformer queryTransformer;

    /**
     * 执行查询重写并输出查询重写后的查询
     *
     * @param prompt 查询语句
     * @return 重写后的查询
     */
    public String doQueryRewrite(String prompt) {
        // 执行查询重写并输出查询重写后的查询
        return queryTransformer.transform(new Query(prompt)).text();
    }
}