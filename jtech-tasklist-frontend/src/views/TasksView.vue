<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useTaskStore } from '@/stores/tasks'
import AppBar from '@/components/AppBar.vue'
import TaskList from '@/components/TaskList.vue'
import TaskForm from '@/components/TaskForm.vue'
import ConfirmDialog from '@/components/ConfirmDialog.vue'
import type { Task } from '@/types'

const taskStore = useTaskStore()

const showForm = ref(false)
const editingTask = ref<Task | null>(null)

const showConfirm = ref(false)
const deletingTask = ref<Task | null>(null)

const snackbar = ref(false)
const snackbarMessage = ref('')
const snackbarColor = ref('success')

function showSnackbar(message: string, color = 'success') {
  snackbarMessage.value = message
  snackbarColor.value = color
  snackbar.value = true
}

function openCreateForm() {
  editingTask.value = null
  showForm.value = true
}

function openEditForm(task: Task) {
  editingTask.value = task
  showForm.value = true
}

function closeForm() {
  showForm.value = false
  editingTask.value = null
}

async function handleSave(data: { title: string; description: string }) {
  try {
    if (editingTask.value) {
      await taskStore.updateTask(editingTask.value.id, data)
      showSnackbar('Tarefa atualizada com sucesso!')
    } else {
      await taskStore.createTask(data)
      showSnackbar('Tarefa criada com sucesso!')
    }
    closeForm()
  } catch {
    showSnackbar('Erro ao salvar tarefa.', 'error')
  }
}

function confirmDelete(task: Task) {
  deletingTask.value = task
  showConfirm.value = true
}

async function handleDelete() {
  if (!deletingTask.value) return
  try {
    await taskStore.deleteTask(deletingTask.value.id)
    showSnackbar('Tarefa excluida com sucesso!')
  } catch {
    showSnackbar('Erro ao excluir tarefa.', 'error')
  } finally {
    showConfirm.value = false
    deletingTask.value = null
  }
}

async function handleToggle(task: Task) {
  try {
    await taskStore.toggleComplete(task)
  } catch {
    showSnackbar('Erro ao atualizar tarefa.', 'error')
  }
}

onMounted(() => {
  taskStore.fetchTasks()
})
</script>

<template>
  <v-layout>
    <AppBar />

    <v-main>
      <v-container class="py-6" style="max-width: 800px">
        <div class="d-flex align-center mb-6">
          <h1 class="text-h4">Minhas Tarefas</h1>
          <v-spacer />
          <v-btn color="primary" prepend-icon="mdi-plus" @click="openCreateForm">
            Nova Tarefa
          </v-btn>
        </div>

        <TaskList
          :tasks="taskStore.tasks"
          :loading="taskStore.loading"
          @toggle="handleToggle"
          @edit="openEditForm"
          @delete="confirmDelete"
        />
      </v-container>
    </v-main>

    <TaskForm
      :visible="showForm"
      :task="editingTask"
      @save="handleSave"
      @cancel="closeForm"
    />

    <ConfirmDialog
      :visible="showConfirm"
      title="Excluir Tarefa"
      :message="`Tem certeza que deseja excluir a tarefa '${deletingTask?.title}'?`"
      @confirm="handleDelete"
      @cancel="showConfirm = false; deletingTask = null"
    />

    <v-snackbar v-model="snackbar" :color="snackbarColor" :timeout="3000" location="bottom right">
      {{ snackbarMessage }}
    </v-snackbar>
  </v-layout>
</template>
