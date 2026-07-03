import { ref } from 'vue'
import { chatStream } from '../api/aiChat'
import type { Message } from '../types/chat'
import { getMemoryId, resetMemoryId } from '../utils/memoryId'

let messageIdCounter = 0

function nextId() {
  return `msg-${++messageIdCounter}`
}

export function useChatStream() {
  const messages = ref<Message[]>([])
  const isStreaming = ref(false)
  const error = ref<string | null>(null)
  const memoryId = ref(getMemoryId())
  let abortController: AbortController | null = null

  async function sendMessage(content: string) {
    if (!content.trim() || isStreaming.value) return

    error.value = null

    const userMessage: Message = {
      id: nextId(),
      role: 'user',
      content: content.trim(),
    }
    messages.value.push(userMessage)

    const assistantMessage: Message = {
      id: nextId(),
      role: 'assistant',
      content: '',
      streaming: true,
    }
    messages.value.push(assistantMessage)

    isStreaming.value = true
    abortController = new AbortController()

    await chatStream({
      memoryId: memoryId.value,
      message: userMessage.content,
      signal: abortController.signal,
      onChunk: (text) => {
        const last = messages.value[messages.value.length - 1]
        if (last?.role === 'assistant') {
          last.content += text
        }
      },
      onDone: () => {
        const last = messages.value[messages.value.length - 1]
        if (last?.role === 'assistant') {
          last.streaming = false
        }
        isStreaming.value = false
        abortController = null
      },
      onError: (err) => {
        const last = messages.value[messages.value.length - 1]
        if (last?.role === 'assistant') {
          last.streaming = false
          if (!last.content) {
            last.content = err.message
          }
        }
        error.value = err.message
        isStreaming.value = false
        abortController = null
      },
    })
  }

  function stopStreaming() {
    abortController?.abort()
  }

  function startNewChat() {
    if (isStreaming.value) {
      stopStreaming()
    }
    memoryId.value = resetMemoryId()
    messages.value = []
    error.value = null
  }

  return {
    messages,
    isStreaming,
    error,
    memoryId,
    sendMessage,
    stopStreaming,
    startNewChat,
  }
}
