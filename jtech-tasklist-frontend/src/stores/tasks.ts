import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { taskService } from '@/services/taskService'
import type { Task, TaskRequest } from '@/types'

export const useTaskStore = defineStore('tasks', () => {
  const tasks = ref<Task[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  const completedTasks = computed(() => tasks.value.filter((t) => t.completed))
  const pendingTasks = computed(() => tasks.value.filter((t) => !t.completed))
  const taskCount = computed(() => tasks.value.length)

  const getTasksByCategory = computed(() => {
    return (categoryId: string | null) => {
      if (categoryId === null) {
        return tasks.value.filter(t => !t.categoryId)
      }
      return tasks.value.filter(t => t.categoryId === categoryId)
    }
  })

  const getCompletedTasksByCategory = computed(() => {
    return (categoryId: string | null) => {
      return getTasksByCategory.value(categoryId).filter(t => t.completed)
    }
  })

  const getPendingTasksByCategory = computed(() => {
    return (categoryId: string | null) => {
      return getTasksByCategory.value(categoryId).filter(t => !t.completed)
    }
  })

  async function fetchTasks() {
    loading.value = true
    error.value = null
    try {
      tasks.value = await taskService.getTasks()
    } catch (e: unknown) {
      error.value = e instanceof Error ? e.message : 'Failed to load tasks'
      console.error('Error fetching tasks:', e)
    } finally {
      loading.value = false
    }
  }

  async function createTask(data: TaskRequest) {
    error.value = null
    try {
      const task = await taskService.createTask(data)
      tasks.value.unshift(task)
      return task
    } catch (e: unknown) {
      error.value = e instanceof Error ? e.message : 'Failed to create task'
      console.error('Error creating task:', e)
      throw e
    }
  }

  async function updateTask(id: string, data: TaskRequest) {
    error.value = null
    try {
      const updated = await taskService.updateTask(id, data)
      const index = tasks.value.findIndex((t) => t.id === id)
      if (index !== -1) {
        tasks.value[index] = updated
      }
      return updated
    } catch (e: unknown) {
      error.value = e instanceof Error ? e.message : 'Failed to update task'
      console.error('Error updating task:', e)
      throw e
    }
  }

  async function deleteTask(id: string) {
    error.value = null
    try {
      await taskService.deleteTask(id)
      tasks.value = tasks.value.filter((t) => t.id !== id)
    } catch (e: unknown) {
      error.value = e instanceof Error ? e.message : 'Failed to delete task'
      console.error('Error deleting task:', e)
      throw e
    }
  }

  async function toggleComplete(task: Task) {
    await updateTask(task.id, {
      title: task.title,
      description: task.description ?? undefined,
      completed: !task.completed,
      categoryId: task.categoryId ?? undefined,
    })
  }

  function getTasksByCategoryId(categoryId: string | null): Task[] {
    if (categoryId === null) {
      return tasks.value.filter(t => !t.categoryId)
    }
    return tasks.value.filter(t => t.categoryId === categoryId)
  }

  function $reset() {
    tasks.value = []
    loading.value = false
    error.value = null
  }

  return {
    tasks,
    loading,
    error,
    completedTasks,
    pendingTasks,
    taskCount,
    getTasksByCategory,
    getCompletedTasksByCategory,
    getPendingTasksByCategory,
    fetchTasks,
    createTask,
    updateTask,
    deleteTask,
    toggleComplete,
    getTasksByCategoryId,
    $reset,
  }
}, {
  persist: {
    key: 'tasks-store',
    storage: localStorage,
    paths: ['tasks']
  }
})

