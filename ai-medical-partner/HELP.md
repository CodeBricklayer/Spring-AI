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



###  MCP

#### 什么是 MCP？

MCP（Model Context Protocol，模型上下文协议）是‌一种开放标准，目的是增强 AI 与外部系统的交互能力。MCP 为 AI 提供了与外部工具、资源和服务交互的标准化方式，让 AI 能够访问最新数据‌、执行复杂操作，并与现有系统集成。

#### 作用

- 轻松增强 AI 的能力
- 统一标准，降低使用和理解成本
- 打造服务生态，造福广大开发者

#### MCP 架构

##### 1、宏观架构

MCP 的核心是 “客户端 - 服务器” 架构，其中 MCP 客户端主机可以连接到多个服务器。客户端主机是指希望访问 MCP 服务的程序，比如 Claude Desktop、IDE‌、AI 工具或部署在服务器上的项目。

##### 2、SDK 3 层架构

如果我们要在程序中使用 MCP 或开发 MCP 服务，可以引入 MCP 官方的 SDK，比如 [Java SDK](https://modelcontextprotocol.io/sdk/java/mcp-overview)。让我们先通过 MCP 官方文档了解 MCP SDK 的架构，主要分为 3 层：

- 客户端 / 服务器层：McpClient 处理客户端操作，而 McpServer 管理服务器端协议操作。两者都使用 McpSession 进行通信管理。
- 会话层（McpSession）：通过 DefaultMcpSession 实现管理通信模式和状态。
- 传输层（McpTransport）：处理 JSON-RPC 消息序列化和反序列化，支持多种传输实现，比如 Stdio 标准 IO 流传输和 HTTP SSE 远程传输。

##### 3、MCP 客户端

MCP Client 是 MCP 架构中的关键组件，主要负责和 MCP 服务器建立连接并进行通信。它能自动匹配服务器的协议版本、确认可用功能、负责数据传输和 JSON-RPC 交互。此外，它还能发现和使用各种‌工具、管理资源、和提示词系统进行交互。

除了这些核心功能，MCP 客户端还支持一‌些额外特性，比如根管理、采样控制，以及同步或异步操作。为了适应不同场景，它提供‌了多种数据传输方式，包括：

- Stdio 标准输入 / 输出：适用于本地调用
- 基于 Java HttpClient 和 WebFlux 的 SSE 传输：适用于远程调用

##### 4、MCP 服务端

MCP Server 也是整‌个 MCP 架构的关键组件，主要用来为客户端提供各种工‌具、资源和功能支持。

它负责处理客户端的请求，包括解析协议、提供工具‌、管理资源以及处理各种交互信息。同时，它还能记录日志、发送通知，并且支持多个客户端同时连接‌，保证高效的通信和协作。

和客户端一样，它也可以通过多种方式进行数据传输，比如‌ Stdio 标准输入 / 输出、基于 Servlet / WebFlux / WebMVC 的 SSE 传输，满足不同应用场景。

#### MCP 核心概念

1. [Resources 资源](https://modelcontextprotocol.io/docs/concepts/resources#resources)：让服务端向客户端提供各种数据，比如文本、文件、数据库记录、API 响应等，客户端可以决定什么时候使用这些资源。使 AI 能够访问最新信息和外部知识，为模型提供更丰富的上下文。
2. [Prompts 提示词](https://modelcontextprotocol.io/docs/concepts/prompts)：服务端可以定义可复用的提示词模板和工作流，供客户端和用户直接使用。它的作用是标准化常见的 AI 交互模式，比如能作为 UI 元素（如斜杠命令、快捷操作）呈现给用户，从而简化用户与 LLM 的交互过程。
3. [Tools 工具](https://modelcontextprotocol.io/docs/concepts/tools)：MCP 中最实用的特性，服务端可以提供给客户端可调用的函数，使 AI 模型能够执行计算、查询信息或者和外部系统交互，极大扩展了 AI 的能力范围。
4. [Sampling 采样](https://modelcontextprotocol.io/docs/concepts/sampling)：允许服务端通过客户端向大模型发送生成内容的请求（反向请求）。使 MCP 服务能够实现复杂的智能代理行为，同时保持用户对整个过程的控制和数据隐私保护。
5. [Roots 根目录](https://modelcontextprotocol.io/docs/concepts/roots)：MCP 协议的安全机制，定义了服务器可以访问的文件系统位置，限制访问范围，为 MCP 服务提供安全边界，防止恶意文件访问。
6. [Transports 传输](https://modelcontextprotocol.io/docs/concepts/transports)：定义客户端和服务器间的通信方式，包括 Stdio（本地进程间通信）和 SSE（网络实时通信），确保不同环境下的可靠信息交换。

#### MCP 服务大全

- [MCP.so](https://mcp.so/)：较为主流，提供丰富的 MCP 服务目录
- [GitHub Awesome MCP Servers](https://github.com/punkpeye/awesome-mcp-servers)：开源 MCP 服务集合
- [阿里云百炼 MCP 服务市场](https://bailian.console.aliyun.com/?tab=mcp#/mcp-market)
- [Spring AI Alibaba 的 MCP 服务市场](https://java2ai.com/mcp/)
- [Glama.ai MCP 服务](https://glama.ai/mcp/servers)

#### MCP 开发最佳实践

1）慎用 MCP：MCP 不是银弹，其本质就是工具调用，只不过统一了标准、更容易共享而已。如果我们自己开发一些不需要共享的工具，完全没必要使用 MCP，可以节约开发和部署成本。我个人的建议是 **能不用就不用**，先开发工具调用，之后需要提供 MCP 服务时再将工具调用转换成 MCP 服务即可。

2）传输模式选择：Stdio 模式作为客户端子进程运行‌，无需网络传输，因此安全性和性能都更高，更适合小型项目；SSE 模式适合作为独立服务部署，可以被多客户端共享‌调用，更适合模块化的中大型项目团队。

3）明确服务：设计 MCP 服务时，要合理划分工具和资源，并且利用 `@Tool`、`@ToolParam` 注解尽可能清楚地描述工具的作用，便于 AI 理解和选择调用。

4）注意容错：和工具开发一样，要注意‌ MCP 服务的容错性和健壮性，捕获并处理所有可能的异常，并且返回友好的‌错误信息，便于客户端处理。

5）性能优化：MCP 服务端要防止单次执行时间过长，可以采用异步模式来‌处理耗时操作，或者设置超时时间客户端也要合理设置超时时间，防止因为 MCP 调用时间过长而导致 AI 应用‌阻塞

6）跨平台兼容性：开发 MCP 服务时，应该考虑在 Windows、Linux 和 macOS 等不同操作系统上的兼容性。特别是使用 stdio 传输模式时，注意路径分隔符差异、进程启动方式和环境变量设置。比如客户端在 Windows 系统中使用命令时需要额外添加 `.cmd` 后缀。

#### MCP 安全问题

1）**信息不对称问题**，用户一般只能看到工具的基本功能描述，只关注 MCP 服务提供了什么工具、能做哪些事情，但一般不会关注 MCP 服务的源码，以及背后的指令。而 AI 能看到完整的工具描述，包括隐藏在代码中的指令。使得恶意开发者可以在用户不知情的情况下，通过 AI 操控系统的行为。而且 AI 也只是 **通过描述** 来了解工具能做什么，却不知道工具真正做了什么。

2） **上下文混合与隔离不足**，由于所有 MCP 工具的描述都被加载到同一会话上下文中，使得恶意 MCP 工具可以影响其他正常工具的行为。

3）**大模型本身的安全意识不足**。大模型被设计为尽可能精确地执行指令，对恶意指令缺乏有效的识别和抵抗能力。

4）MCP 协议缺乏严格的版本控制和‌更新通知机制，使得远程 MCP 服务可以在用户不知情的情况下更改功能或添加恶意代码，‌客户端无法感知这些变化。

5）对于具有敏感操作能力的 MCP 工具（比如读取文件、执行系统命令），缺乏严格的权限验证和多重授权机制，用户难‌以控制工具的实际行为范围。

#### MCP 安全提升思路

其实目前对于提升 MCP 安‌全性，开发者能做的事情比较有限，比如：

1. 使用沙箱环境：总是在 Docker 等隔离环境中运行第三方 MCP 服务，限制其文件系统和网络访问权限。
2. 仔细检查参数与行为：使用 MCP 工具前，通过源码完整查看所有参数，尤其要注意工具执行过程中的网络请求和文件操作。
3. 优先使用可信来源：仅安装来自官方或知名组织的 MCP 服务，避免使用未知来源的第三方工具。就跟平时开发时选择第三方 SDK 和 API 是一样的，优先选文档详细的、大厂维护的、知名度高的。

我们也可以期待 MCP 官方对协议进行改进，比如：

1. 优化 MCP 服务和工具的定义，明确区分 **功能描述**（给 AI 理解什么时候要调用工具）和 **执行指令**（给 AI 传递的 Prompt 信息）。
2. 完善权限控制：建立 “最小权限” 原则，任何涉及敏感数据的操作都需要用户明确授权。
3. 安全检测机制：检测并禁止工具描述中的恶意指令，比如禁止对其他工具行为的修改、或者对整个 AI 回复的控制。（不过这点估计比较难实现）
4. 规范 MCP 生态：提高 MCP 服务共享的门槛，防止用户将恶意 MCP 服务上传到了服务市场被其他用户使用。服务市场可以对上架的 MCP 服务进行安全审计，自动检测潜在的恶意代码模式。
