package com.tp.springai.aicodehelper.ai.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 包名称：com.tp.springai.aicodehelper.ai.tools
 * 类名称：InterviewQuestionTool
 * 类描述：
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/3 9:25
 */
@Slf4j
public class InterviewQuestionTool {

    /**
     * 从面试鸭网站获取关键词相关的面试题列表
     * @param keyword 搜索关键词（如“redis”、“java多线程”）
     * @return 面试题列表，若失败则返回错误信息
     */
    @Tool(name = "interviewQuestionSearch", value = """
            Retrieves relevant interview question from mianshiya.com based on a keyword.
            Use this tool when the user asks for interview question about specific technologies,
            programing concepts, or job-related topics.The input should be a clear search term.
            """)
    public String searchInterviewQuestion(@P(value = "the keyword to search") String keyword) {
        List<String> questions = new ArrayList<>();
        // 构建搜索URL（编码关键词以支持中文）
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String url = "https://www.mianshiya.com/search/all?searchText=" + encodedKeyword;
        // 发送请求并解析页面
        Document document;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();
        } catch (IOException e) {
            log.error("get web error", e);
            return e.getMessage();
        }
        // 提取面试题
        Elements questionElements = document.select(".ant-table-cell > a");
        questionElements.forEach(element -> questions.add(element.text().trim()));
        return String.join("\n", questions);
    }
}