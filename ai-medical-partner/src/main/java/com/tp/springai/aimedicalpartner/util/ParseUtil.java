package com.tp.springai.aimedicalpartner.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 包名称：com.tp.springai.aimedicalpartner.util
 * 类名称：ParseUtil
 * 类描述：
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/7 14:25
 */
public class ParseUtil {

    private static final Set<String> CONTENT_FIELDS =
            Set.of(
                    "messages",
                    "instruction",
                    "input",
                    "output",
                    "metadata"
            );

    /**
     * 转换元信息
     *
     * @param node 节点
     * @return 元信息
     */
    public static Map<String, Object> parseMetadata(JsonNode node) {

        Map<String, Object> metadata = new HashMap<>();


        // 1. 优先读取 metadata 节点
        JsonNode metadataNode = node.get("metadata");

        if (metadataNode != null && metadataNode.isObject()) {

            metadataNode.properties()
                    .forEach(entry -> {

                        JsonNode value = entry.getValue();

                        metadata.put(
                                entry.getKey(),
                                value.isValueNode()
                                        ? value.asText()
                                        : value.toString()
                        );
                    });
        }


        // 2. 兼容平铺字段
        node.properties()
                .forEach(entry -> {

                    String key = entry.getKey();


                    // 排除正文相关字段
                    if (CONTENT_FIELDS.contains(key)) {
                        return;
                    }


                    JsonNode value = entry.getValue();


                    metadata.put(
                            key,
                            value.isValueNode()
                                    ? value.asText()
                                    : value.toString()
                    );
                });


        return metadata;
    }
}