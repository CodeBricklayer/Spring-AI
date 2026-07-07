package com.tp.springai.aimedicalpartner.rag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tp.springai.aimedicalpartner.util.ParseUtil;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 包名称：com.tp.springai.aimedicalpartner.rag
 * 类名称：MessageKnowledgeParse
 * 类描述：Medical AI 大模型数据集
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/7 14:23
 */
@Component
public class MessageKnowledgeParse implements KnowledgeParser {
    @Override
    public boolean support(JsonNode node) {
        return node.has("messages");
    }

    @Override
    public Document parse(JsonNode node, String fileName) {
        ArrayNode messages =
                (ArrayNode) node.get("messages");


        String content =
                StreamSupport.stream(
                                messages.spliterator(),
                                false
                        )
                        .map(item -> {

                            String role =
                                    item.get("role")
                                            .asText();


                            String text =
                                    item.get("content")
                                            .asText();


                            return switch (role) {

                                case "user" -> "患者问题：\n" + text;

                                case "assistant" -> "医生回答：\n" + text;

                                default -> text;
                            };

                        })
                        .collect(Collectors.joining("\n\n"));


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