package com.tp.springai.aimedicalpartner.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.QueryExpander;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 包名称：com.tp.springai.aimedicalpartner.demo
 * 类名称：MultiQueryExpanderDemo
 * 类描述：查询扩展器Demo
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/8 16:15
 */
@Component
@RequiredArgsConstructor
public class MultiQueryExpanderDemo {

    private final QueryExpander myMultiQueryExpander;

    /**
     * 扩展查询语句
     *
     * @param query 查询语句
     * @return 扩展后的查询语句列表
     */
    public List<Query> expand(String query) {
        return myMultiQueryExpander.expand(new Query(query));
    }

}