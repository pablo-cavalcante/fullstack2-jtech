<script setup lang="ts">
import { ref, computed } from 'vue'
import TaskItem from './TaskItem.vue'
import type { Task } from '@/types'

const props = defineProps<{
  tasks: Task[]
  loading: boolean
}>()

const emit = defineEmits<{
  toggle: [task: Task]
  edit: [task: Task]
  delete: [task: Task]
}>()

const filter = ref<'all' | 'pending' | 'completed'>('all')

const filteredTasks = computed(() => {
  if (filter.value === 'pending') return props.tasks.filter((t) => !t.completed)
  if (filter.value === 'completed') return props.tasks.filter((t) => t.completed)
  return props.tasks
})
</script>

<template>
  <div>
    <v-chip-group v-model="filter" mandatory selected-class="text-primary" class="mb-4">
      <v-chip value="all" variant="outlined"> Todas ({{ tasks.length }}) </v-chip>
      <v-chip value="pending" variant="outlined">
        Pendentes ({{ tasks.filter((t) => !t.completed).length }})
      </v-chip>
      <v-chip value="completed" variant="outlined">
        Concluidas ({{ tasks.filter((t) => t.completed).length }})
      </v-chip>
    </v-chip-group>

    <v-progress-linear v-if="loading" indeterminate color="primary" class="mb-4" />

    <div v-if="!loading && filteredTasks.length === 0" class="text-center py-8">
      <v-icon size="64" color="grey-lighten-1">mdi-clipboard-text-outline</v-icon>
      <p class="text-h6 text-grey mt-4">Nenhuma tarefa encontrada</p>
      <p class="text-body-2 text-grey">Crie uma nova tarefa para comecar!</p>
    </div>

    <TaskItem
      v-for="task in filteredTasks"
      :key="task.id"
      :task="task"
      @toggle="emit('toggle', $event)"
      @edit="emit('edit', $event)"
      @delete="emit('delete', $event)"
    />
  </div>
</template>
