package com.tp.controller;

import com.tp.tool.DateTimeTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * 包名称：com.tp.controller
 * 类名称：MyAiChatController
 * 类描述：用于ai对话模拟
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/6/25 16:24
 */
@RestController
@RequiredArgsConstructor
public class MyAiChatController {

    private final ChatClient chatClient;

    private final ChatClient chatClient2;

    private final ChatClient chatClient3;

    private final DateTimeTools dateTimeTools;

    /**
     * 远程api openai调用
     *
     * @param question 请求参数
     * @return 返回问答结果
     */
    @RequestMapping("/ai")
    public String ai(String question) {
        return chatClient
                // 创建Prompt对象，用于构建聊天输入
                .prompt()
                // 设置用户输入
                .user(question)
                //调用模型，返回结果
                .call()
                //获取结果内容
                .content();
    }

    /**
     * 远程api openai调用，可调用本地方法
     *
     * @param question 请求参数
     * @return 返回问答结果
     */
    @RequestMapping("/aiTool")
    public String aiTool(String question) {
        return chatClient
                // 创建Prompt对象，用于构建聊天输入
                .prompt()
                // 设置用户输入
                .user(question)
                //注册工具
                .tools(dateTimeTools)
                //调用模型，返回结果
                .call()
                //获取结果内容
                .content();
    }


    /**
     * 远程api openai调用根据记忆调用返回结果
     *
     * @param question 请求参数
     * @param convId   请求用户id
     * @return 返回问答结果
     */
    @RequestMapping("/aiPlus")
    public String aiPlus(String question, String convId) {
        return chatClient
                // 创建Prompt对象，用于构建聊天输入
                .prompt()
                // 设置用户输入
                .user(question)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, convId))
                //调用模型，返回结果
                .call()
                //获取结果内容
                .content();
    }

    /**
     * 本地ollama问答
     *
     * @param question 请求参数
     * @return 返回问答结果
     */
    @RequestMapping("/ai2")
    public String ai2(String question) {
        return chatClient2
                // 创建Prompt对象，用于构建聊天输入
                .prompt()
                // 设置用户输入
                .user(question)
                //调用模型，返回结果
                .call()
                //获取结果内容
                .content();
    }

    /**
     * 本地ollama流式问答
     *
     * @param question 请求参数
     * @return 流式返回问答结果
     */
    @RequestMapping(value = "/ai3", produces = "text/html;charset=UTF-8")
    public Flux<String> ai3(String question) {
        return chatClient2
                // 创建Prompt对象，用于构建聊天输入
                .prompt()
                // 设置用户输入
                .user(question)
                //流式响应输出
                .stream()
                //获取结果内容
                .content();
    }


    /**
     * 使用模板：一套代码对应所有场景
     *
     * @param topic 请求模板参数
     * @return 流式返回问答结果
     */
    @RequestMapping(value = "/ask", produces = "text/html;charset=UTF-8")
    public Flux<String> ask(String topic) {
        PromptTemplate template = new PromptTemplate("介绍下{topic}");
        Prompt prompt = template.create(Map.of("topic", topic));
        return chatClient2
                // 创建Prompt对象，用于构建聊天输入
                .prompt(prompt)
                //流式响应输出
                .stream()
                //获取结果内容
                .content();
    }


    /**
     * 使用ChatClient的流式API 可以最快速的组装提示词
     *
     * @param topic 请求模板参数
     * @return 流式返回问答结果
     */
    @RequestMapping(value = "/ask2", produces = "text/html;charset=UTF-8")
    public Flux<String> ask2(String topic) {
        return chatClient2
                // 创建Prompt对象，用于构建聊天输入
                .prompt()
                // 设置系统提示词
                .system("你是一个专业的书评助手")
                .user(u -> u.text("请给我推荐三本关于{topic}的书籍").param("topic", topic))
                //流式响应输出
                .stream()
                //获取结果内容
                .content();
    }

    /**
     * 远程api openai调用，设置默认系统提示词
     *
     * @param question 请求参数
     * @return 返回问答结果
     */
    @RequestMapping("/ask3")
    public String ask3(String question) {
        return chatClient3
                // 创建Prompt对象，用于构建聊天输入
                .prompt()
                // 设置用户输入
                .user(question)
                //调用模型，返回结果
                .call()
                //获取结果内容
                .content();
    }

    // 使用Java 16+ 的Record特性，编译器会自动生成构造器、equals/hashCode等方法
    public record TopicBook(
            // 主题
            String topic,
            // 推荐书籍
            List<String> books
    ) {
    }


    /**
     * 输出实体类信息
     *
     * @param topic 主题
     * @return 成功结果
     */
    @RequestMapping("/ask4")
    public String ask4(String topic) {
        TopicBook book = chatClient
                // 创建Prompt对象，用于构建聊天输入
                .prompt()
                // 设置系统提示词
                .system("你是一个专业的书评助手")
                .user(u -> u.text("请给我推荐三本关于{topic}的书籍").param("topic", topic))
                .call()
                //获取结果内容
                .entity(TopicBook.class);
        System.out.println(book);
        return "ok";
    }


    public record BookReview(
            // 评价人
            String reviewerName,
            // 评分
            int rating,
            // 评价内容
            String comment
    ) {
    }

    /**
     * 输出实体类信息
     *
     * @param bookName 书籍名称
     * @return 成功结果
     */
    @RequestMapping("/ask5")
    public String ask5(String bookName) {
        List<BookReview> bookReviews = chatClient
                // 创建Prompt对象，用于构建聊天输入
                .prompt()
                .user(u -> u.text("请给{bookName}书籍三条评价信息").param("bookName", bookName))
                .call()
                //获取结果内容
                .entity(new ParameterizedTypeReference<List<BookReview>>() {
                });
        System.out.println(bookReviews);
        return "ok";
    }
}