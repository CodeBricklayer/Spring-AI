package com.tp.springai.aimedicalpartner.tools;

import cn.hutool.core.io.FileUtil;
import com.tp.springai.aimedicalpartner.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 包名称：com.tp.springai.aimedicalpartner.tools
 * 类名称：FileOperationTool
 * 类描述：文件操作工具类（提供文件读写功能）
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/10 10:10
 */
@Component
public class FileOperationTool implements AiTool {

    private final String FILE_DIR = FileConstant.FILE_SAVE_DIR + "/file";

    @Tool(description = "Read content from a file")
    public String readFile(@ToolParam(description = "Name of a file to read.") String fileName) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading file:" + e.getMessage();
        }
    }

    @Tool(description = "Write content from a file")
    public String writeFile(@ToolParam(description = "Name of a file to write.") String fileName,
                            @ToolParam(description = "Content to write to the file.") String content) {
        String filePath = FILE_DIR + "/" + fileName;
        FileUtil.mkParentDirs(filePath);
        try {
            FileUtil.writeUtf8String(content, filePath);
            return "Successfully wrote to file:" + filePath;
        } catch (Exception e) {
            return "Error writing file:" + e.getMessage();
        }
    }
}