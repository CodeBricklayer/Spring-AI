<script setup lang="ts">
import { nextTick, ref, watch } from 'vue'
import { useChatStream } from '../composables/useChatStream'
import ChatInput from './ChatInput.vue'
import MessageList from './MessageList.vue'

const {
  messages,
  isStreaming,
  error,
  memoryId,
  sendMessage,
  stopStreaming,
  startNewChat,
} = useChatStream()

const messageListRef = ref<InstanceType<typeof MessageList> | null>(null)

async function scrollToBottom() {
  await nextTick()
  messageListRef.value?.scrollToBottom()
}

watch(messages, scrollToBottom, { deep: true })

async function handleSend(content: string) {
  await sendMessage(content)
  await scrollToBottom()
}
</script>

<template>
  <div class="chat-window">
    <header class="header">
      <div class="header-left">
        <h1 class="title">AI 编程助手</h1>
        <span class="session-id">会话 #{{ memoryId }}</span>
      </div>
      <button class="btn-new" type="button" @click="startNewChat">
        新对话
      </button>
    </header>

    <div v-if="error" class="error-banner">
      {{ error }}
    </div>

    <MessageList ref="messageListRef" :messages="messages" />

    <ChatInput
      :is-streaming="isStreaming"
      @send="handleSend"
      @stop="stopStreaming"
    />
  </div>
</template>

<style scoped>
.chat-window {
  display: flex;
  flex-direction: column;
  height: 100vh;
  max-width: 800px;
  margin: 0 auto;
  background: #fff;
  box-shadow: 0 0 24px rgba(0, 0, 0, 0.06);
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-bottom: 1px solid #e5e7eb;
  background: #fff;
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #111827;
}

.session-id {
  font-size: 12px;
  color: #9ca3af;
}

.btn-new {
  padding: 6px 14px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  background: #fff;
  font-size: 13px;
  color: #374151;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-new:hover {
  background: #f3f4f6;
}

.error-banner {
  padding: 10px 20px;
  background: #fef2f2;
  color: #dc2626;
  font-size: 14px;
  border-bottom: 1px solid #fecaca;
}
</style>
