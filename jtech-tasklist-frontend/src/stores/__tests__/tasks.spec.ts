import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useTaskStore } from '../tasks'
import type { Task } from '@/types'

vi.mock('@/services/taskService', () => ({
  taskService: {
    getTasks: vi.fn(),
    createTask: vi.fn(),
    updateTask: vi.fn(),
    deleteTask: vi.fn(),
  },
}))

import { taskService } from '@/services/taskService'

const mockTask: Task = {
  id: '1',
  title: 'Test Task',
  description: 'Description',
  completed: false,
  createdAt: '2024-01-01',
  updatedAt: '2024-01-01',
}

const mockTaskCompleted: Task = {
  ...mockTask,
  id: '2',
  title: 'Completed Task',
  completed: true,
}

describe('Task Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('should start with empty state', () => {
    const store = useTaskStore()
    expect(store.tasks).toEqual([])
    expect(store.loading).toBe(false)
    expect(store.error).toBeNull()
    expect(store.taskCount).toBe(0)
  })

  it('should fetch tasks', async () => {
    vi.mocked(taskService.getTasks).mockResolvedValue([mockTask, mockTaskCompleted])

    const store = useTaskStore()
    await store.fetchTasks()

    expect(store.tasks).toHaveLength(2)
    expect(store.loading).toBe(false)
    expect(store.taskCount).toBe(2)
    expect(store.completedTasks).toHaveLength(1)
    expect(store.pendingTasks).toHaveLength(1)
  })

  it('should set loading during fetch', async () => {
    let resolvePromise: (value: Task[]) => void
    vi.mocked(taskService.getTasks).mockReturnValue(
      new Promise((resolve) => {
        resolvePromise = resolve
      }),
    )

    const store = useTaskStore()
    const fetchPromise = store.fetchTasks()

    expect(store.loading).toBe(true)

    resolvePromise!([])
    await fetchPromise

    expect(store.loading).toBe(false)
  })

  it('should handle fetch error', async () => {
    vi.mocked(taskService.getTasks).mockRejectedValue(new Error('Network error'))

    const store = useTaskStore()
    await store.fetchTasks()

    expect(store.error).toBe('Network error')
    expect(store.loading).toBe(false)
  })

  it('should create task and prepend to list', async () => {
    vi.mocked(taskService.createTask).mockResolvedValue(mockTask)

    const store = useTaskStore()
    await store.createTask({ title: 'Test Task', description: 'Description' })

    expect(store.tasks).toHaveLength(1)
    expect(store.tasks[0].title).toBe('Test Task')
  })

  it('should update task in list', async () => {
    const updated = { ...mockTask, title: 'Updated' }
    vi.mocked(taskService.updateTask).mockResolvedValue(updated)

    const store = useTaskStore()
    store.tasks = [mockTask]

    await store.updateTask('1', { title: 'Updated' })

    expect(store.tasks[0].title).toBe('Updated')
  })

  it('should delete task from list', async () => {
    vi.mocked(taskService.deleteTask).mockResolvedValue()

    const store = useTaskStore()
    store.tasks = [mockTask, mockTaskCompleted]

    await store.deleteTask('1')

    expect(store.tasks).toHaveLength(1)
    expect(store.tasks[0].id).toBe('2')
  })

  it('should toggle complete status', async () => {
    const toggled = { ...mockTask, completed: true }
    vi.mocked(taskService.updateTask).mockResolvedValue(toggled)

    const store = useTaskStore()
    store.tasks = [mockTask]

    await store.toggleComplete(mockTask)

    expect(taskService.updateTask).toHaveBeenCalledWith('1', {
      title: 'Test Task',
      description: 'Description',
      completed: true,
    })
  })

  it('should throw on create error', async () => {
    vi.mocked(taskService.createTask).mockRejectedValue(new Error('Create failed'))

    const store = useTaskStore()

    await expect(store.createTask({ title: 'Fail' })).rejects.toThrow('Create failed')
    expect(store.error).toBe('Create failed')
  })
})
