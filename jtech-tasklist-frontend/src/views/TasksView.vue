<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import { useTaskStore } from '@/stores/tasks'
import { useCategoryStore } from '@/stores/categories'
import AppBar from '@/components/AppBar.vue'
import TaskList from '@/components/TaskList.vue'
import TaskForm from '@/components/TaskForm.vue'
import CategoryList from '@/components/CategoryList.vue'
import CategoryForm from '@/components/CategoryForm.vue'
import ConfirmDialog from '@/components/ConfirmDialog.vue'
import type { Task, Category } from '@/types'

const taskStore = useTaskStore()
const categoryStore = useCategoryStore()

// Task state
const showTaskForm = ref(false)
const editingTask = ref<Task | null>(null)
const showTaskConfirm = ref(false)
const deletingTask = ref<Task | null>(null)

// Category state
const showCategoryForm = ref(false)
const editingCategory = ref<Category | null>(null)
const showCategoryConfirm = ref(false)
const deletingCategory = ref<Category | null>(null)

// Snackbar
const snackbar = ref(false)
const snackbarMessage = ref('')
const snackbarColor = ref('success')

// Computed
const currentTasks = computed(() => {
  return taskStore.getTasksByCategory(categoryStore.selectedCategoryId)
})

const currentCategoryTitle = computed(() => {
  if (categoryStore.selectedCategoryId === null) {
    return 'Todas as Tarefas'
  }
  return categoryStore.selectedCategory?.title ?? 'Lista'
})

const tasksInCurrentCategory = computed(() => {
  return currentTasks.value.length
})

// Snackbar helper
function showSnackbar(message: string, color = 'success') {
  snackbarMessage.value = message
  snackbarColor.value = color
  snackbar.value = true
}

// Task handlers
function openCreateTaskForm() {
  editingTask.value = null
  showTaskForm.value = true
}

function openEditTaskForm(task: Task) {
  editingTask.value = task
  showTaskForm.value = true
}

function closeTaskForm() {
  showTaskForm.value = false
  editingTask.value = null
}

async function handleSaveTask(data: { title: string; description: string; categoryId?: string }) {
  try {
    if (editingTask.value) {
      await taskStore.updateTask(editingTask.value.id, data)
      showSnackbar('Tarefa atualizada com sucesso!')
    } else {
      await taskStore.createTask(data)
      showSnackbar('Tarefa criada com sucesso!')
    }
    closeTaskForm()
  } catch {
    showSnackbar('Erro ao salvar tarefa.', 'error')
  }
}

function confirmDeleteTask(task: Task) {
  deletingTask.value = task
  showTaskConfirm.value = true
}

async function handleDeleteTask() {
  if (!deletingTask.value) return
  try {
    await taskStore.deleteTask(deletingTask.value.id)
    showSnackbar('Tarefa excluída com sucesso!')
  } catch {
    showSnackbar('Erro ao excluir tarefa.', 'error')
  } finally {
    showTaskConfirm.value = false
    deletingTask.value = null
  }
}

async function handleToggleTask(task: Task) {
  try {
    await taskStore.toggleComplete(task)
  } catch {
    showSnackbar('Erro ao atualizar tarefa.', 'error')
  }
}

// Category handlers
function openCreateCategoryForm() {
  editingCategory.value = null
  showCategoryForm.value = true
}

function openEditCategoryForm(category: Category) {
  editingCategory.value = category
  showCategoryForm.value = true
}

function closeCategoryForm() {
  showCategoryForm.value = false
  editingCategory.value = null
}

async function handleSaveCategory(data: { title: string; description: string }) {
  try {
    if (editingCategory.value) {
      await categoryStore.updateCategory(editingCategory.value.id, data)
      showSnackbar('Lista atualizada com sucesso!')
    } else {
      await categoryStore.createCategory(data)
      showSnackbar('Lista criada com sucesso!')
    }
    closeCategoryForm()
  } catch {
    showSnackbar('Erro ao salvar lista.', 'error')
  }
}

function confirmDeleteCategory(category: Category) {
  // Verifica se há tarefas na categoria
  const tasksInCategory = taskStore.getTasksByCategoryId(category.id)
  if (tasksInCategory.length > 0) {
    showSnackbar(
      `Não é possível excluir. A lista "${category.title}" possui ${tasksInCategory.length} tarefa(s).`,
      'warning'
    )
    return
  }
  
  deletingCategory.value = category
  showCategoryConfirm.value = true
}

async function handleDeleteCategory() {
  if (!deletingCategory.value) return
  try {
    await categoryStore.deleteCategory(deletingCategory.value.id)
    showSnackbar('Lista excluída com sucesso!')
  } catch {
    showSnackbar('Erro ao excluir lista.', 'error')
  } finally {
    showCategoryConfirm.value = false
    deletingCategory.value = null
  }
}

function handleSelectCategory(id: string | null) {
  categoryStore.selectCategory(id)
}

onMounted(async () => {
  await categoryStore.fetchCategories()
  await taskStore.fetchTasks()
})
</script>

<template>
  <v-layout>
    <AppBar />

    <v-main>
      <v-container fluid class="py-6">
        <v-row>
          <!-- Sidebar com categorias -->
          <v-col cols="12" md="3">
            <CategoryList
              :categories="categoryStore.categories"
              :selected-category-id="categoryStore.selectedCategoryId"
              :loading="categoryStore.loading"
              @select="handleSelectCategory"
              @edit="openEditCategoryForm"
              @delete="confirmDeleteCategory"
              @create="openCreateCategoryForm"
            />
          </v-col>

          <!-- Área principal com tarefas -->
          <v-col cols="12" md="9">
            <v-card>
              <v-card-title class="d-flex align-center">
                <v-icon class="mr-2">mdi-check-circle-outline</v-icon>
                <span>{{ currentCategoryTitle }}</span>
                <v-chip class="ml-2" size="small" variant="outlined">
                  {{ tasksInCurrentCategory }}
                </v-chip>
                <v-spacer />
                <v-btn
                  color="primary"
                  prepend-icon="mdi-plus"
                  @click="openCreateTaskForm"
                >
                  Nova Tarefa
                </v-btn>
              </v-card-title>

              <v-divider />

              <v-card-text class="pa-0">
                <TaskList
                  :tasks="currentTasks"
                  :loading="taskStore.loading"
                  @toggle="handleToggleTask"
                  @edit="openEditTaskForm"
                  @delete="confirmDeleteTask"
                />
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>

    <!-- Dialogs -->
    <TaskForm
      :visible="showTaskForm"
      :task="editingTask"
      :category-id="categoryStore.selectedCategoryId"
      @save="handleSaveTask"
      @cancel="closeTaskForm"
    />

    <CategoryForm
      :visible="showCategoryForm"
      :category="editingCategory"
      @save="handleSaveCategory"
      @cancel="closeCategoryForm"
    />

    <ConfirmDialog
      :visible="showTaskConfirm"
      title="Excluir Tarefa"
      :message="`Tem certeza que deseja excluir a tarefa '${deletingTask?.title}'?`"
      @confirm="handleDeleteTask"
      @cancel="showTaskConfirm = false; deletingTask = null"
    />

    <ConfirmDialog
      :visible="showCategoryConfirm"
      title="Excluir Lista"
      :message="`Tem certeza que deseja excluir a lista '${deletingCategory?.title}'?`"
      @confirm="handleDeleteCategory"
      @cancel="showCategoryConfirm = false; deletingCategory = null"
    />

    <v-snackbar
      v-model="snackbar"
      :color="snackbarColor"
      :timeout="3000"
      location="bottom right"
    >
      {{ snackbarMessage }}
    </v-snackbar>
  </v-layout>
</template>
