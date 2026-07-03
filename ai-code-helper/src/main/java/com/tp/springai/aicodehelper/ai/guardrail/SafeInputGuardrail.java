package com.tp.springai.aicodehelper.ai.guardrail;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;

import java.util.Set;

/**
 * 包名称：com.tp.springai.aicodehelper.ai.guardrail
 * 类名称：SafeInputGuardrail
 * 类描述：输入护轨（输入拦截器）
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/3 10:00
 */
public class SafeInputGuardrail implements InputGuardrail {

    /**
     * 敏感词集合
     */
    private static final Set<String> SENSITIVE_WORD = Set.of("kill", "evil");

    /**
     * 检测用户输入是否安全
     *
     * @param userMessage the response from the LLM
     * @return 输出
     */
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        // 获取用户输入并转换为小写以确保大小写不敏感
        String inputText = userMessage.singleText().toLowerCase();
        // 使用正则表达式分割输入文本为单词
        String[] words = inputText.split("\\W+");
        for (String word : words) {
            if (SENSITIVE_WORD.contains(word)) {
                return fatal("Sensitive word detected: " + word);
            }
        }
        return success();
    }
}