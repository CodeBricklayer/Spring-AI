<script setup lang="ts">
import { ref } from 'vue'
import type { Message } from '../types/chat'

defineProps<{
  messages: Message[]
}>()

const bottomRef = ref<HTMLElement | null>(null)

defineExpose({
  scrollToBottom: () => {
    bottomRef.value?.scrollIntoView({ behavior: 'smooth' })
  },
})
</script>

<template>
  <div class="message-list">
    <div v-if="messages.length === 0" class="empty">
      <p class="empty-title">你好，我是 AI 编程助手</p>
      <p class="empty-hint">可以问我编程学习路线、项目建议、求职面试等问题</p>
    </div>

    <div
      v-for="message in messages"
      :key="message.id"
      class="message-row"
      :class="message.role"
    >
      <div class="bubble">
        <span class="content">{{ message.content }}</span>
        <span v-if="message.streaming" class="cursor" />
      </div>
    </div>

    <div ref="bottomRef" />
  </div>
</template>

<style scoped>
.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 24px 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.empty {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: #6b7280;
  gap: 8px;
}

.empty-title {
  font-size: 18px;
  font-weight: 500;
  color: #374151;
  margin: 0;
}

.empty-hint {
  font-size: 14px;
  margin: 0;
}

.message-row {
  display: flex;
}

.message-row.user {
  justify-content: flex-end;
}

.message-row.assistant {
  justify-content: flex-start;
}

.bubble {
  max-width: 80%;
  padding: 12px 16px;
  border-radius: 16px;
  line-height: 1.6;
  font-size: 15px;
  white-space: pre-wrap;
  word-break: break-word;
}

.user .bubble {
  background: #4f46e5;
  color: #fff;
  border-bottom-right-radius: 4px;
}

.assistant .bubble {
  background: #f3f4f6;
  color: #1f2937;
  border-bottom-left-radius: 4px;
}

.content {
  vertical-align: baseline;
}

.cursor {
  display: inline-block;
  width: 2px;
  height: 1em;
  background: #4f46e5;
  margin-left: 2px;
  vertical-align: text-bottom;
  animation: blink 1s step-end infinite;
}

@keyframes blink {
  50% {
    opacity: 0;
  }
}
</style>
