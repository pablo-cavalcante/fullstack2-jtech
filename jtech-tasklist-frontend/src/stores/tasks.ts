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

  async function fetchTasks() {
    loading.value = true
    error.value = null
    try {
      tasks.value = await taskService.getTasks()
    } catch (e: unknown) {
      error.value = e instanceof Error ? e.message : 'Failed to load tasks'
    } finally {
      loading.value = false
    }
  }

  async function createTask(data: TaskRequest) {
    error.value = null
    try {
      const task = await taskService.createTask(data)
      tasks.value.unshift(task)
    } catch (e: unknown) {
      error.value = e instanceof Error ? e.message : 'Failed to create task'
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
    } catch (e: unknown) {
      error.value = e instanceof Error ? e.message : 'Failed to update task'
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
      throw e
    }
  }

  async function toggleComplete(task: Task) {
    await updateTask(task.id, {
      title: task.title,
      description: task.description ?? undefined,
      completed: !task.completed,
    })
  }

  return {
    tasks,
    loading,
    error,
    completedTasks,
    pendingTasks,
    taskCount,
    fetchTasks,
    createTask,
    updateTask,
    deleteTask,
    toggleComplete,
  }
})
