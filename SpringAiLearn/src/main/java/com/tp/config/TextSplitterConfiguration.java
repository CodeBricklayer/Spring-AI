package com.tp.config;

import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 包名称：com.tp.config
 * 类名称：TextSplitterConfiguration
 * 类描述：文本分块配置类
 *
 * @author tanpeng
 * @version V4.0
 * @since 2026/7/1 9:44
 */
@Configuration
public class TextSplitterConfiguration {

    /**
     * 令牌文本切分器
     *
     * @return 切分器
     */
    @Bean
    public TokenTextSplitter tokenTextSplitter() {

        /*
         * 默认参数
         * chunkSize‌：800 token，每块目标大小，但实际可能因标点截断更短。
         * minChunkSizeChars‌：350 字符，只有标点位置超过此阈值才允许截断。
         * minChunkLengthToEmbed‌：5 字符，最终块文本长度需大于此值才会保留。
         * maxNumChunks‌：10000，防止无限切分的硬上限。
         * keepSeparator‌：true，控制是否保留换行符。‌
         * */
        return TokenTextSplitter.builder()
                // 单个文本块的最大token数
                .withChunkSize(50)
                // 最大可生成的块数量。防止对超长文本无限分割，达到此上限后即使还有剩余文本也停止生产新块。
                .withMaxNumChunks(1000)
                .build();
    }
}