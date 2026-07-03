export interface ChatStreamOptions {
  memoryId: number
  message: string
  onChunk: (text: string) => void
  onDone: () => void
  onError: (error: Error) => void
  signal?: AbortSignal
}

function parseSseEvent(event: string): string | null {
  const lines = event.split('\n')
  const dataLines: string[] = []

  for (const line of lines) {
    if (line.startsWith('data:')) {
      dataLines.push(line.slice(5).trimStart())
    }
  }

  return dataLines.length > 0 ? dataLines.join('\n') : null
}

export async function chatStream(options: ChatStreamOptions): Promise<void> {
  const baseUrl = import.meta.env.VITE_API_BASE || '/api'
  const url = new URL(`${baseUrl}/ai/chatStream`, window.location.origin)
  url.searchParams.set('memoryId', String(options.memoryId))
  url.searchParams.set('message', options.message)

  try {
    const response = await fetch(url.toString(), { signal: options.signal })

    if (!response.ok) {
      const body = await response.text()
      throw new Error(body || `请求失败 (${response.status})`)
    }

    if (!response.body) {
      throw new Error('响应体为空')
    }

    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    let buffer = ''

    while (true) {
      const { done, value } = await reader.read()
      if (done) break

      buffer += decoder.decode(value, { stream: true })
      const events = buffer.split('\n\n')
      buffer = events.pop() || ''

      for (const event of events) {
        const data = parseSseEvent(event)
        if (data !== null) {
          options.onChunk(data)
        }
      }
    }

    if (buffer.trim()) {
      const data = parseSseEvent(buffer)
      if (data !== null) {
        options.onChunk(data)
      }
    }

    options.onDone()
  } catch (err) {
    if (err instanceof Error && err.name === 'AbortError') {
      options.onDone()
      return
    }
    options.onError(err instanceof Error ? err : new Error(String(err)))
  }
}
