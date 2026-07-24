package com.tp.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * 包名称：com.tp.controller
 * 类名称：DemoController
 * 类描述：
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/24 14:31
 */
@RestController
@RequestMapping("/ai")
public class DemoController {
    @Resource
    private ChatClient client;

    @GetMapping("/prompt/streamChat")
    public Flux<String> doChatWithPrompt(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return client.prompt()
                .system("你是一个编程助手，只回答编程问题，其它问题回复，我只能回答编程相关的问题")
                .user(question)
                .stream().content()
                .filter(text -> text != null && !text.isBlank())
                // AI输出完成后，追加一条结束信号
                .concatWithValues("[DONE]");
    }

    @GetMapping("/prompt/template/streamChat")
    public Flux<String> doChatWithPromptTemplate(@RequestParam(name = "companyName") String companyName,
                                                 @RequestParam(name = "offerType") String offerType,
                                                 @RequestParam(name = "candidateName") String candidateName,
                                                 @RequestParam(name = "jobPosition") String jobPosition,
                                                 @RequestParam(name = "entryDate") String entryDate,
                                                 @RequestParam(name = "salaryRange") String salaryRange,
                                                 @RequestParam(name = "welfare") String welfare) {
        //系统提示词
        String systemTemplate = """
                你是{companyName}的资深人力资源专员，精通{offerType}入职Offer的撰写规范。
                请根据用户提供的信息，生成一份符合{companyName}企业规范的{offerType}Offer，要求如下：
                1. 语言正式且温馨，符合{companyName}的官方文书风格；
                2. 包含核心要素：入职岗位、入职日期、薪资范围（税前）、核心福利、欢迎语；
                3. 以html格式输出
                4. 结尾必须带上{companyName}的名称和人力资源部联系方式提示。
                """;
        PromptTemplate systemTemplatePrompt = new PromptTemplate(systemTemplate);
        Map<String, Object> systemVars = Map.of(
                "companyName", companyName,
                "offerType", offerType
        );
        String systemContent = systemTemplatePrompt.render(systemVars);
        SystemMessage systemMessage = new SystemMessage(systemContent);

        //用户提示词
        String userTemplate = """
                请生成一份入职offer，具体信息如下:
                1.候选人姓名:{candidateName}
                2.入职岗位:{jobPosition}
                3.入职日期:{entryDate}
                4.税前薪资范围:{salaryRange}
                5.核心福利:{welfare}
                """;
        PromptTemplate userTemplatePrompt = new PromptTemplate(userTemplate);
        Map<String, Object> userVars = Map.of(
                "candidateName", candidateName,
                "jobPosition", jobPosition,
                "entryDate", entryDate,
                "salaryRange", salaryRange,
                "welfare", welfare
        );
        String userContent = userTemplatePrompt.render(userVars);
        UserMessage userMessage = new UserMessage(userContent);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        return client.prompt(prompt)
                .stream().content()
                .filter(text -> text != null && !text.isBlank())
                // AI输出完成后，追加一条结束信号
                .concatWithValues("[DONE]");
    }

}