# 笔记

### Chat Memory Advisor

MessageChatMemoryAdvisor 将对话历史作为一系列独立的消息添加到提示中，保留原始对话的完整结构，包括每条消息的‌角色标识（用户、助手、系统）

* MessageChatMemoryAdvisor：从记忆中检索历史对话，并将其作为消息集合添加到提示词中
* PromptChatMemoryAdvisor：从记忆中检索历史对话，并将其添加到提示词的系统文本中
* VectorStoreChatMemoryAdvisor：可以用向量数据库来存储检索历史对话

### Chat Memory

Chat Memory 负责历史对话的存储，定义了保存消息、查询消息、清空消息历史的方法。

* InMemoryChatMemory：内存存储
* CassandraChatMemory：在 Cassandra 中带有过期时间的持久化存储
* Neo4jChatMemory：在 Neo4j 中没有过期时间限制的持久化存储
* JdbcChatMemory：在 JDBC 中没有过期时间限制的持久化存储

### StructuredOutputConverter

结构化输出

* AbstractConversionServiceOutputConverter：提供预配置的 GenericConversionService，用于将 LLM 输出转换为所需格式
* AbstractMessageOutputConverter：支持 Spring AI Message 的转换
* BeanOutputConverter：用于将输出转换为 Java Bean 对象（基于 ObjectMapper 实现）
* MapOutputConverter：用于将输出转换为 Map 结构
* ListOutputConverter：用于将输出转换为 List 结构

### 对话记忆持久化

* InMemoryChatMemory：内存存储
* CassandraChatMemory：在 Cassandra 中带有过期时间的持久化存储
* Neo4jChatMemory：在 Neo4j 中没有过期时间限制的持久化存储
* JdbcChatMemory：在 JDBC 中没有过期时间限制的持久化存储
* 自定义实现

### PromptTemplate 模板

PromptTemplate 是 Spring AI 框架中用于构建和管理提示词的核心组件。允许开发者创建带有占位符的文本模板，然后在运行时动态替换这些占位符。

PromptTemplate 在以下场景特别有用：
* 动态个性化交互：根据用户信息、上下文或业务规则定制提示词
* 多语言支持：使用相同的变量但不同的模板文件支持多种语言
* A/B 测试：轻松切换不同版本的提示词进行效果对比
* 提示词版本管理：将提示词外部化，便于版本控制和迭代优化

专用模板类
* SystemPromptTemplate：用于系统消息，设置 AI 的行为和背景
* AssistantPromptTemplate：用于助手消息，用于设置 AI 回复的结构
* FunctionPromptTemplate：目前没用，Spring AI 2.0已删除


### 扩展思路

1）自定义 Advisor，比如权限校验、违禁词校验 Advisor

2）自定义对话记忆，比如持久化对话到 MySQL 或 Redis 存储中

3）编写一套包含变量的 Prompt 模板，并保存为资源文件，从文件加载模板

4）开发一个多模态对话助手，能够让 AI 解释图片（建议使用国内的 AI 大模型）

5）阅读 Spring AI 官方的 ChatMemory 文档，了解如何自主构造 ChatMemory


### RAG
RAG（Retrieval-Augmented Generation，检索增强生成）是一种结合信息检索技术和 AI 内容生成的混合架构，可以解决‌大模型的知识时效性限制和幻觉问题。

| 特性       | 传统大语言模型        | RAG 增强模型            |
| ---------- | --------------------- | ----------------------- |
| 知识时效性 | 受训练数据截止日期限制 | 可接入最新知识库        |
| 领域专业性 | 泛化知识，专业深度有限 | 可接入专业领域知识      |
| 响应准‌确性 | 可能产生 “幻觉”       | 基于检索的事实依据      |
| 可控性     | 依赖原始训练          | 可通过知识库定制输出     |
| 资源消耗   | 较高（需要大模型参‌数） | 模型可更小，结合外部知识 |

#### RAG 工作流程

- 文档收集和切割
- 向量转换和存储
- 文档过滤和检索
- 查询增强和关联

##### 1、文档收集和切割

文档收集：从各种来源（网页、PDF、数据库等）收集原始文档

文档预处理：清洗、标准化文本格式

文档切割：将长文档分割成适当‌大小的片段（俗称 chunks）

- 基于固定大小（如 512 个 token）
- 基于语义边界（如段落、章节）
- 基于递归分割策略（如递归字符 n-gram 切割）

##### 2、向量转换和存储

向量转换：使用 Embedding 模型将文本块转换为高维向量表示，可以捕获到文本‌的语义特征

向量存储：将生成的向量和对应‌文本存入向量数据库，支持高效的相似性搜索

##### 3、文档过滤和检索

查询处理：将用户问题也转换为向量表示

过滤机制：基于元数据、关键词或自定义规则进行过滤

相似度搜索：在向量数据库中查‌找与问题向量最相似的文档块，常用的相似度搜索算法有余弦‌相似度、欧氏距离等

上下文组装：将检索到的多个文档块组装成连贯上下文

##### 4、查询增强和关联

提示词组装：将检索到的相关文档与用户问题组合成增强提示

上下文融合：大模型基于增强提示生成回答

源引用：在回答中添加信息来源引用

后处理：格式化、摘要或其他处理以优化最终输出

#### 召回
召回是信息检索中的第一阶段，目标是从大规模数据集中快速筛选出可能相关的候选项子集。强调速度和广度，而非精确度。

举个例子，我们要从搜索引擎查询 “编程导航 - 程序员一站式编程‌学习交流社区” 时，召回阶段会从数十亿网页中快速筛选出数千个含有 “编程”、“导航”、“程序员” 等相关内容的页面，为后‌续粗略排序和精细排序提供候选集。

#### 精排和 Rank 模型
精排（精确排序）是搜索 / 推荐系统‌的最后阶段，使用计算复杂度更高的算法，考虑更多特征和业务规则，对少量候选‌项进行更复杂、精细的排序。

比如，短视频推荐先通过召回获取数万个可能相关视频‌，再通过粗排缩减至数百条，最后精排阶段会考虑用户最近的互动、视频热度、内容多样性等复杂因素，确定‌最终展示的 10 个视频及顺序。

#### 混合检索策略
混合检索策略结合多种检索方法‌的优势，提高搜索效果。常见组合包括关键词检索、语义检索、知‌识图谱等。


#### 多查询扩展的完整使用流程
* 使用扩展后的查询召回文档：遍历扩展后的查询列表，对每个查询使用 DocumentRetriever 来召回相关文档。
* 整合召回的文档：将每个查询召回的文档进行整合，形成一个包含所有相关信息的文档集合。（也可以使用 文档合并器 去重）
* 使用召回的文档改写 Prompt：将整合后的文档内容添加到原始 Prompt 中，为大语言模型提供更丰富的上下文信息。

#### 工具定义模式

| 特性          | Methods 方式                                | Functions 方式              |
| ------------- | ------------------------------------------- |---------------------------|
| 定义方式       | 使用 @Tool 和 @ToolParam 注解标记类方法     | 使用函数式接口并通过 Spring Bean 定义 |
| 语法复杂度     | 简单，直观                                  | 较复杂，需要定义请求 / 响应对象         |
| 支持的参数类型 | 大多数 Java 类型，包括基本类型、POJO、集合等 | 不支持基本类型、Optional、集合类型     |
| 支持的返回类型 | 几乎所有可序列化类型，包括 void              | 不支持基本类型、Optional、集合类型等    |
| 使用场景       | 适合大多数新项目开发                        | 适合与现有函数式 API 集成           |
| 注册方式       | 支持按需注册和全局注册                       | 通常在配置类中预先定义               |
| 类型转换       | 自动处理                                    | 需要更多手动配置                  |
| 文档支持       | 通过注解提供描述                            | 通过 Bean 描述和 JSON 属性注解     |

1）Methods 模式：通过 `@Tool` 注解定义工具，通过 `tools` 方法绑定工具

```
class WeatherTools {
    @Tool(description = "Get current weather for a location")
    public String getWeather(@ToolParam(description = "The city name") String city) {
        return "Current weather in " + city + ": Sunny, 25°C";
    }
}


ChatClient.create(chatModel)
    .prompt("What's the weather in Beijing?")
    .tools(new WeatherTools())
    .call();
```

2）Functions 模式：通过 `@Bean` 注解定义工具，通过 `functions` 方法绑定工具

```
@Configuration
public class ToolConfig {
    @Bean
    @Description("Get current weather for a location")
    public Function<WeatherRequest, WeatherResponse> weatherFunction() {
        return request -> new WeatherResponse("Weather in " + request.getCity() + ": Sunny, 25°C");
    }
}


ChatClient.create(chatModel)
    .prompt("What's the weather in Beijing?")
    .functions("weatherFunction")
    .call();
```

#### 定义工具

1）注解式：只需使用 `@Tool` 注解标记普通 Java 方法，就可以定义工具了，简单直观。

2）编程式：如果想在运行时动‌态创建工具，可以选择编程式来定义工具，更灵活。

#### 使用工具

1）按需使用：这是最简单的方式，直接在构建 ChatClient 请求时通过 `tools()` 方法附加工具。这种方式适合只在特定对话中使用某些工具的场景。

2）全局使用：如果某些工具需要在所有对话中都可用‌，可以在构建 ChatClient 时注册默认工具。这样，这些工具将对从同一个 ChatClient 发起的所有对话可用。

3）更底层的使用方式：除了给 ChatClient ‌绑定工具外，也可以给更底层的 ChatModel 绑定工具（毕竟工具调用是 AI 大模型支持的能力），‌适合需要更精细控制的场景。

4）动态解析：一般情况下，使用前面 3 种方式即可。对于更复杂的应用，Spring AI 还支持通过 `ToolCallbackResolver` 在运行时动态解析工具。这种方式特别适合工具需要根据上下文动态确定的场景，比如从数据库中根据工具名搜索要调用的工具。在本节的工具进阶知识中会讲到，先了解到有这种方式即可。
