<script setup lang="ts">
import { ref, watch } from 'vue'
import type { Task } from '@/types'

const props = defineProps<{
  visible: boolean
  task?: Task | null
}>()

const emit = defineEmits<{
  save: [data: { title: string; description: string }]
  cancel: []
}>()

const title = ref('')
const description = ref('')
const form = ref<{ validate: () => Promise<{ valid: boolean }> } | null>(null)

const titleRules = [(v: string) => !!v?.trim() || 'Titulo e obrigatorio']

watch(
  () => props.visible,
  (val) => {
    if (val && props.task) {
      title.value = props.task.title
      description.value = props.task.description ?? ''
    } else if (val) {
      title.value = ''
      description.value = ''
    }
  },
)

async function handleSave() {
  const result = await form.value?.validate()
  if (!result?.valid) return
  emit('save', { title: title.value.trim(), description: description.value.trim() })
}

const dialogTitle = () => (props.task ? 'Editar Tarefa' : 'Nova Tarefa')
</script>

<template>
  <v-dialog :model-value="visible" max-width="500" persistent @update:model-value="emit('cancel')">
    <v-card>
      <v-card-title class="text-h5 pa-4">
        {{ dialogTitle() }}
      </v-card-title>
      <v-card-text>
        <v-form ref="form">
          <v-text-field
            v-model="title"
            label="Titulo"
            :rules="titleRules"
            variant="outlined"
            class="mb-2"
          />
          <v-textarea
            v-model="description"
            label="Descricao (opcional)"
            variant="outlined"
            rows="3"
          />
        </v-form>
      </v-card-text>
      <v-card-actions class="pa-4">
        <v-spacer />
        <v-btn variant="text" @click="emit('cancel')">Cancelar</v-btn>
        <v-btn color="primary" variant="elevated" @click="handleSave">Salvar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
