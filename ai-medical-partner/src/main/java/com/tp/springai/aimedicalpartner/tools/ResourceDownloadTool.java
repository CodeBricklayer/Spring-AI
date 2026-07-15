package com.tp.springai.aimedicalpartner.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.tp.springai.aimedicalpartner.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 包名称：com.tp.springai.aimedicalpartner.tools
 * 类名称：ResourceDownloadTool
 * 类描述：资源下载工具
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/15 10:16
 */
@Component
public class ResourceDownloadTool implements AiTool {

    @Tool(description = "Download a resource from a given URL")
    public String downloadResource(@ToolParam(description = "URL of the resource to download") String url,
                                   @ToolParam(description = "Name of the file to save the download resource") String fileName) {
        String fileDir = FileConstant.FILE_SAVE_DIR + "/download";
        String filePath = fileDir + "/" + fileName;
        try {
            // 创建目录
            FileUtil.mkdir(fileDir);
            // 使用hutool的 downloadFile 方法下载资源
            HttpUtil.downloadFile(url, new File(filePath));
            return "Resource downloaded successfully to:" + filePath;
        } catch (Exception e) {
            return "Error to download resource:" + e.getMessage();
        }
    }
}