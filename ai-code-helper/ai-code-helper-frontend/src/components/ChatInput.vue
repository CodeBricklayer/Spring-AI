<script setup lang="ts">
import { ref } from 'vue'

const props = defineProps<{
  isStreaming: boolean
}>()

const emit = defineEmits<{
  send: [message: string]
  stop: []
}>()

const input = ref('')

function handleSend() {
  const text = input.value.trim()
  if (!text || props.isStreaming) return
  emit('send', text)
  input.value = ''
}

function handleKeydown(event: KeyboardEvent) {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    handleSend()
  }
}
</script>

<template>
  <div class="chat-input">
    <textarea
      v-model="input"
      class="textarea"
      placeholder="输入你的问题，Enter 发送，Shift+Enter 换行"
      rows="3"
      :disabled="isStreaming"
      @keydown="handleKeydown"
    />
    <div class="actions">
      <button
        v-if="isStreaming"
        class="btn btn-stop"
        type="button"
        @click="emit('stop')"
      >
        停止
      </button>
      <button
        v-else
        class="btn btn-send"
        type="button"
        :disabled="!input.trim()"
        @click="handleSend"
      >
        发送
      </button>
    </div>
  </div>
</template>

<style scoped>
.chat-input {
  padding: 16px 20px 20px;
  border-top: 1px solid #e5e7eb;
  background: #fff;
}

.textarea {
  width: 100%;
  box-sizing: border-box;
  padding: 12px 14px;
  border: 1px solid #d1d5db;
  border-radius: 12px;
  font-size: 15px;
  font-family: inherit;
  line-height: 1.5;
  resize: none;
  outline: none;
  transition: border-color 0.2s;
}

.textarea:focus {
  border-color: #4f46e5;
}

.textarea:disabled {
  background: #f9fafb;
  color: #9ca3af;
}

.actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.btn {
  padding: 8px 20px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: opacity 0.2s;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-send {
  background: #4f46e5;
  color: #fff;
}

.btn-send:hover:not(:disabled) {
  background: #4338ca;
}

.btn-stop {
  background: #ef4444;
  color: #fff;
}

.btn-stop:hover {
  background: #dc2626;
}
</style>
