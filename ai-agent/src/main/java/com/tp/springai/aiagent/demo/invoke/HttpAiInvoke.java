package com.tp.springai.aiagent.demo.invoke;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.dashscope.utils.Constants;

/**
 * 包名称：com.tp.springai.aiagent.demo.invoke
 * 类名称：HttpAiInvoke
 * 类描述：阿里云灵积AI Http调用
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/3 15:24
 */
public class HttpAiInvoke {

    static {
        Constants.baseHttpApiUrl = "https://ws-sk5flpqqtym5x3nv.cn-beijing.maas.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    }

    public static void main(String[] args) {

        JSONObject body = new JSONObject();

        body.set("model", "qwen-plus");

        JSONArray messages = new JSONArray();

        messages.add(new JSONObject()
                .set("role", "system")
                .set("content", "You are a helpful assistant."));

        messages.add(new JSONObject()
                .set("role", "user")
                .set("content", "你是谁？"));

        body.set("input", new JSONObject().set("messages", messages));

        body.set("parameters", new JSONObject()
                .set("result_format", "message"));

        HttpResponse response = HttpRequest.post(Constants.baseHttpApiUrl)
                .header(Header.AUTHORIZATION, "Bearer " + System.getenv("OPENAI_API_KEY"))
                .header(Header.CONTENT_TYPE, "application/json")
                .body(JSONUtil.toJsonStr(body))
                .execute();

        System.out.println(response.body());
    }
}