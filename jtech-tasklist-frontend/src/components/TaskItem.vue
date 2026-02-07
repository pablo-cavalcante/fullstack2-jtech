<script setup lang="ts">
import type { Task } from '@/types'

defineProps<{
  task: Task
}>()

const emit = defineEmits<{
  toggle: [task: Task]
  edit: [task: Task]
  delete: [task: Task]
}>()
</script>

<template>
  <v-card class="mb-3" variant="outlined">
    <v-card-text class="d-flex align-center pa-4">
      <v-checkbox
        :model-value="task.completed"
        hide-details
        color="success"
        class="mr-4 flex-grow-0"
        @update:model-value="emit('toggle', task)"
      />
      <div class="flex-grow-1">
        <div
          class="text-h6"
          :class="{ 'text-decoration-line-through text-grey': task.completed }"
        >
          {{ task.title }}
        </div>
        <div v-if="task.description" class="text-body-2 text-grey-darken-1 mt-1">
          {{ task.description }}
        </div>
        <div class="text-caption text-grey mt-1">
          Criada em: {{ new Date(task.createdAt).toLocaleString('pt-BR') }}
        </div>
      </div>
      <div class="d-flex">
        <v-btn icon variant="text" size="small" @click="emit('edit', task)">
          <v-icon>mdi-pencil</v-icon>
          <v-tooltip activator="parent" location="top">Editar</v-tooltip>
        </v-btn>
        <v-btn icon variant="text" size="small" color="error" @click="emit('delete', task)">
          <v-icon>mdi-delete</v-icon>
          <v-tooltip activator="parent" location="top">Excluir</v-tooltip>
        </v-btn>
      </div>
    </v-card-text>
  </v-card>
</template>
