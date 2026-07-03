# Spring AI & LangChain4j 学习笔记

基于 Spring AI 2.x + LangChain4j + MCP + OpenAI/Qwen 的学习与实践。

# 一、为什么学习 Spring AI

随着大模型的发展，越来越多的 Java 项目开始集成 AI 能力，例如：

- 智能客服
- AI 办公助手
- AI 编程助手
- 企业知识库（RAG）
- 数据分析
- 工作流 Agent
- MCP Server

对于 Java 开发而言，目前主流的 AI 开发框架主要有：

| 框架                 | 推荐指数 | 说明                |
| -------------------- | -------- | ------------------- |
| Spring AI            | ⭐⭐⭐⭐⭐    | Spring 官方 AI 框架 |
| LangChain4j          | ⭐⭐⭐⭐⭐    | Java 最成熟 AI 框架 |
| Alibaba Spring AI    | ⭐⭐⭐⭐     | Spring AI Alibaba   |
| Semantic Kernel Java | ⭐⭐⭐      | 微软方案            |

目前 Spring AI 已成为 Spring 官方推荐的 AI 开发框架，而 LangChain4j 在 Agent、Memory、RAG 等方面功能更加丰富，因此两者都值得学习。



# 二、学习路线

建议按照下面顺序学习。

```
大模型基础
      │
      ▼
ChatModel
      │
      ▼
Prompt
      │
      ▼
ChatClient
      │
      ▼
Chat Memory
      │
      ▼
Function Calling
      │
      ▼
Tools
      │
      ▼
MCP
      │
      ▼
RAG
      │
      ▼
Agent
      │
      ▼
AI项目实战
```

# 三、Spring AI 学习内容

目前计划学习如下内容。

| 章节             | 内容       | 状态 |
| ---------------- | ---------- | ---- |
| Spring AI介绍    | 框架介绍   | ✅    |
| ChatModel        | 模型调用   | ✅    |
| Prompt           | 提示词     | ✅    |
| ChatClient       | 聊天客户端 | ✅    |
| Memory           | 上下文记忆 | ✅    |
| Embedding        | 向量模型   | ✅    |
| RAG              | 知识库     | ✅    |
| Tool Calling     | 工具调用   | ✅    |
| Function Calling | 函数调用   | ✅    |
| MCP Client       | 客户端     | ✅    |
| MCP Server       | 服务端     | ✅    |
| Agent            | 智能体     | ✅    |

# 四、当前项目介绍

## SpringAiLearn

Spring AI 基础学习项目。

主要用于学习：

- ChatClient
- Prompt
- Memory
- Tool
- Function Calling
- MCP Client

## spring-ai-parent

统一管理：

- Spring Boot
- Spring AI
- LangChain4j
- OpenAI
- Maven 依赖

便于多个模块统一版本。

## mcp-server

MCP 服务端。

主要学习：

- Tool 定义
- Resource
- Prompt
- Streamable HTTP
- SSE

## ai-code-helper

AI 编程助手。

目标实现：

- AI 对话
- AI 代码解释
- AI SQL 优化
- AI 接口生成
- AI 单元测试
- AI Code Review
- MCP Tool
- RAG

# 五、Spring AI 与 LangChain4j 对比

| 功能             | Spring AI | LangChain4j |
| ---------------- | --------- | ----------- |
| 官方支持         | ★★★★★     | ★★★★☆       |
| Spring Boot 集成 | ★★★★★     | ★★★★☆       |
| Memory           | ★★★★☆     | ★★★★★       |
| Agent            | ★★★☆☆     | ★★★★★       |
| RAG              | ★★★★☆     | ★★★★★       |
| Tool Calling     | ★★★★★     | ★★★★★       |
| MCP              | ★★★★★     | ★★★★☆       |
| 学习难度         | 简单      | 中等        |

# 六、项目目标

通过本项目掌握以下能力：

- Spring AI 开发
- LangChain4j 开发
- MCP Server 开发
- MCP Client 开发
- Function Calling
- Tool Calling
- RAG 知识库
- Agent 开发
- AI 企业级项目开发
