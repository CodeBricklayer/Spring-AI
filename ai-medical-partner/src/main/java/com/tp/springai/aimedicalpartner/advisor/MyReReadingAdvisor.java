package com.tp.springai.aimedicalpartner.advisor;

import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Map;
import java.util.Objects;

/**
 * 包名称：com.tp.springai.aimedicalpartner.advisor
 * 类名称：MyReReadingAdvisor
 * 类描述：自定义重复读顾问器，可提高大语言模型的推理能力
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/6 13:40
 */
public class MyReReadingAdvisor implements BaseAdvisor {

    private static final String DEFAULT_RE2_ADVISE_TEMPLATE = """
			{re2_input_query}
			Read the question again: {re2_input_query}
			""";

    private final String re2AdviseTemplate;

    public MyReReadingAdvisor() {
        this(DEFAULT_RE2_ADVISE_TEMPLATE);
    }

    public MyReReadingAdvisor(String re2AdviseTemplate) {
        this.re2AdviseTemplate = re2AdviseTemplate;
    }

    /**
     * 执行请求前改写ChatClientRequest
     * @param chatClientRequest 会话请求
     * @param advisorChain 顾问
     * @return 改写后的ChatClientRequest
     */
    @Override
    public @NonNull ChatClientRequest before(@NonNull ChatClientRequest chatClientRequest, @NonNull AdvisorChain advisorChain) {
        String augmentedUserText = PromptTemplate.builder()
                .template(this.re2AdviseTemplate)
                .variables(Map.of("re2_input_query", Objects.requireNonNull(chatClientRequest.prompt().getUserMessage().getText())))
                .build()
                .render();

        return chatClientRequest.mutate()
                .prompt(chatClientRequest.prompt().augmentUserMessage(augmentedUserText))
                .build();
    }

    @Override
    public @NonNull ChatClientResponse after(@NonNull ChatClientResponse chatClientResponse, @NonNull AdvisorChain advisorChain) {
        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return 0;
    }

}