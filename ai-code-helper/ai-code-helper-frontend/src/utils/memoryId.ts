const STORAGE_KEY = 'ai-chat-memory-id'

export function getMemoryId(): number {
  const stored = localStorage.getItem(STORAGE_KEY)
  if (stored) {
    const id = parseInt(stored, 10)
    if (!isNaN(id)) return id
  }
  return createMemoryId()
}

export function createMemoryId(): number {
  const id = Math.floor(Math.random() * 1_000_000_000) + 1
  localStorage.setItem(STORAGE_KEY, String(id))
  return id
}

export function resetMemoryId(): number {
  return createMemoryId()
}
