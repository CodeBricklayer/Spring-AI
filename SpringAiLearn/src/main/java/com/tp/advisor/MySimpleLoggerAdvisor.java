package com.tp.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

/**
 * 包名称：com.tp.advisor
 * 类名称：MySimpleLoggerAdvisor
 * 类描述：实现CallAdvisor和StreamAdvisor接口，同时支持两种模式
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/6/26 9:21
 */
public class MySimpleLoggerAdvisor implements CallAdvisor, StreamAdvisor {
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        System.out.println("发送请求前:" + request);
        ChatClientResponse response = chain.nextCall(request);
        System.out.println("接收到响应：" + response);
        return response;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {
        System.out.println("发送流式请求前:" + request);
        return chain.nextStream(request)
                .doOnNext(response -> System.out.println("收到流式响应片段：" + response));
    }

    /**
     * 获取Advisor的名称
     *
     * @return 名称
     */
    @Override
    public String getName() {
        return "简单日志Advisor";
    }

    /**
     * 这个getOrder()方法用于指定Advisor(通知器)的执行顺序
     * 作用说明:
     * 返回值越小，优先级越高，越早执行
     * 返回0表示高优先级
     * 如果有多个Advisor，Spring AI会按照此值从小到大依次执行
     *
     * @return 执行顺序
     */
    @Override
    public int getOrder() {
        return 0;
    }
}