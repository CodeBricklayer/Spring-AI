package com.tp.springai.aimedicalpartner.advisor;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

import java.util.Objects;

/**
 * 包名称：com.tp.springai.aimedicalpartner.advisor
 * 类名称：MyLoggerAdvisor
 * 类描述：自定义日志顾问器
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/6 13:29
 */
@Slf4j
public class MyLoggerAdvisor implements CallAdvisor, StreamAdvisor {
    @Override
    public @NonNull ChatClientResponse adviseCall(@NonNull ChatClientRequest chatClientRequest, @NonNull CallAdvisorChain callAdvisorChain) {
        logRequest(chatClientRequest);

        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

        logResponse(chatClientResponse);

        return chatClientResponse;
    }

    @Override
    public @NonNull Flux<ChatClientResponse> adviseStream(@NonNull ChatClientRequest chatClientRequest, @NonNull StreamAdvisorChain streamAdvisorChain) {
        logRequest(chatClientRequest);

        Flux<ChatClientResponse> chatClientResponses = streamAdvisorChain.nextStream(chatClientRequest);

        return new ChatClientMessageAggregator().aggregateChatClientResponse(chatClientResponses, this::logResponse);
    }

    @Override
    public @NonNull String getName() {
        return this.getClass().getName();
    }

    @Override
    public int getOrder() {
        return 0;
    }


    private void logRequest(ChatClientRequest request) {
        log.info("AI Request: {}", request.prompt().getUserMessage());
    }

    private void logResponse(ChatClientResponse chatClientResponse) {
        assert chatClientResponse.chatResponse() != null;
        log.info("AI Response: {}", Objects.requireNonNull(chatClientResponse.chatResponse().getResult()).getOutput().getText());
    }
}