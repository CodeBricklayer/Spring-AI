package com.tp.springai.oldversionlearnai.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 包名称：com.tp.springai.oldversionlearnai.service
 * 接口名称：QwenChatService
 * 接口描述：模型请求方法
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/21 10:19
 */

@Service
public class QwenChatService {

    static {
        Constants.baseHttpApiUrl = "https://ws-sk5flpqqtym5x3nv.cn-beijing.maas.aliyuncs.com/api/v1";
    }

    /**
     * 单轮对话：一问一答，不带记忆
     *
     * @param userMessage 用户提问
     * @return AI回答
     */
    public String singleChat(String userMessage) {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("你是专业的Java开发助手，回答精准简洁，只讲技术干货。")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(userMessage)
                .build();
        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(System.getenv("OPENAI_API_KEY"))
                // 此处以qwen-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        try {
            // 4. 调用大模型，获取结果
            GenerationResult result = gen.call(param);
            // 提取回答内容返回
            return result.getOutput().getChoices().getFirst().getMessage().getContent();
        } catch (Exception e) {
            return "AI调用失败：" + e.getMessage();
        }
    }
}