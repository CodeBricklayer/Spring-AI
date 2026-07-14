package com.tp.springai.aimedicalpartner.advisor;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * 包名称：com.tp.springai.aimedicalpartner.advisor
 * 类名称：ContextualQueryAugmenterFactory
 * 类描述：创建上下文查询增强器的工厂
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/8 17:36
 */
public class ContextualQueryAugmenterFactory {

    public static ContextualQueryAugmenter createInstance() {
        PromptTemplate promptTemplate = new PromptTemplate("""
                你应该输出下面的内容：
                抱歉，我只能回答医疗相关的问题，别的没办法帮到您哦，
                有问题可以联系管理员。
                """);
        // 上下文查询增强器
        return ContextualQueryAugmenter.builder()
                .allowEmptyContext(false)
                .emptyContextPromptTemplate(promptTemplate)
                .build();
    }

}