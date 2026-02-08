<script setup lang="ts">
import type { Category } from '@/types'

interface Props {
  categories: Category[]
  selectedCategoryId: string | null
  loading?: boolean
}

interface Emits {
  (e: 'select', id: string | null): void
  (e: 'edit', category: Category): void
  (e: 'delete', category: Category): void
  (e: 'create'): void
}

defineProps<Props>()
const emit = defineEmits<Emits>()

function handleSelect(id: string | null) {
  emit('select', id)
}

function handleEdit(category: Category, event: Event) {
  event.stopPropagation()
  emit('edit', category)
}

function handleDelete(category: Category, event: Event) {
  event.stopPropagation()
  emit('delete', category)
}

function handleCreate() {
  emit('create')
}
</script>

<template>
  <v-card>
    <v-card-title class="d-flex align-center">
      <v-icon class="mr-2">mdi-format-list-bulleted</v-icon>
      <span>Minhas Listas</span>
      <v-spacer />
      <v-btn
        icon="mdi-plus"
        size="small"
        variant="text"
        @click="handleCreate"
      />
    </v-card-title>

    <v-divider />

    <v-list v-if="!loading && categories.length > 0" density="compact">
      <!-- Opção "Todas as Tarefas" -->
      <v-list-item
        :active="selectedCategoryId === null"
        @click="handleSelect(null)"
      >
        <template #prepend>
          <v-icon>mdi-view-list</v-icon>
        </template>
        <v-list-item-title>Todas as Tarefas</v-list-item-title>
      </v-list-item>

      <v-divider />

      <!-- Lista de categorias -->
      <v-list-item
        v-for="category in categories"
        :key="category.id"
        :active="selectedCategoryId === category.id"
        @click="handleSelect(category.id)"
      >
        <template #prepend>
          <v-icon>mdi-folder</v-icon>
        </template>

        <v-list-item-title>{{ category.title }}</v-list-item-title>
        <v-list-item-subtitle v-if="category.description">
          {{ category.description }}
        </v-list-item-subtitle>

        <template #append>
          <div class="d-flex">
            <v-btn
              icon="mdi-pencil"
              size="x-small"
              variant="text"
              @click="(e) => handleEdit(category, e)"
            />
            <v-btn
              icon="mdi-delete"
              size="x-small"
              variant="text"
              color="error"
              @click="(e) => handleDelete(category, e)"
            />
          </div>
        </template>
      </v-list-item>
    </v-list>

    <v-card-text v-else-if="loading" class="text-center py-8">
      <v-progress-circular indeterminate color="primary" />
    </v-card-text>

    <v-card-text v-else class="text-center py-8">
      <p class="text-grey mb-4">Nenhuma lista criada ainda.</p>
      <v-btn color="primary" variant="outlined" @click="handleCreate">
        Criar Primeira Lista
      </v-btn>
    </v-card-text>
  </v-card>
</template>
