package com.tp.springai.aimedicalpartner.rag;

import com.fasterxml.jackson.databind.JsonNode;
import com.tp.springai.aimedicalpartner.util.ParseUtil;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 包名称：com.tp.springai.aimedicalpartner.rag
 * 类名称：InstructionKnowledgeParser
 * 类描述：多科室问诊数据集
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/7 14:29
 */
@Component
public class InstructionKnowledgeParser implements KnowledgeParser {
    @Override
    public boolean support(JsonNode node) {

        return node.has("instruction")
                &&
                node.has("output");
    }

    @Override
    public Document parse(JsonNode node, String fileName) {
        String question =
                node.get("instruction")
                        .asText();


        String input =
                node.has("input")
                        ?
                        node.get("input")
                        .asText()
                        :
                        "";


        String answer =
                node.get("output")
                        .asText();


        String content =
                """
                        患者问题：
                        %s
                        
                        补充信息：
                        %s
                        
                        医生回答：
                        %s
                        """
                        .formatted(
                                question,
                                input,
                                answer
                        );


        Map<String, Object> metadata =
                ParseUtil.parseMetadata(node);


        metadata.put(
                "fileName",
                fileName
        );


        return new Document(
                content,
                metadata
        );
    }
}