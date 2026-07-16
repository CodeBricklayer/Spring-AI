package com.tp.springai.imagesearchmcpserver.tools;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 包名称：com.tp.springai.imagesearchmcpserver.tools
 * 类名称：ImageSearchTool
 * 类描述：图片搜索工具
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/16 14:17
 */
@Service
public class ImageSearchTool {

    private static final String API_KEY = "Key";


    private static final String API_URL = "https://api.pexels.com/v1/search";

    @McpTool(description = "search image from web")
    public String searchImage(@McpToolParam(description = "search query keyword") String query){
        try {
            return String.join(",", searchMediumImages(query));
        } catch (Exception e) {
            return "Error search image: " + e.getMessage();
        }
    }

    public List<String> searchMediumImages(String query) {

        Map<String, String> headers = new HashMap<>(2);
        headers.put("Authorization", API_KEY);


        Map<String, Object> params = new HashMap<>(2);
        params.put("query", query);
        params.put("per_page", 10);


        String response = HttpUtil.createGet(API_URL)
                .addHeaders(headers)
                .form(params)
                .execute()
                .body();


        return JSONUtil.parseObj(response)
                .getJSONArray("photos")
                .stream()
                .map(photoObj -> (JSONObject) photoObj)
                .map(photoObj -> photoObj.getJSONObject("src"))
                .map(photo -> photo.getStr("medium"))
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
    }
}