<script setup lang="ts">
import { ref, watch } from 'vue'
import type { Category } from '@/types'

interface Props {
  visible: boolean
  category?: Category | null
}

interface Emits {
  (e: 'save', data: { title: string; description: string }): void
  (e: 'cancel'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const title = ref('')
const description = ref('')
const titleRules = [(v: string) => !!v || 'Título é obrigatório']

watch(
  () => props.visible,
  (visible) => {
    if (visible) {
      title.value = props.category?.title ?? ''
      description.value = props.category?.description ?? ''
    }
  },
)

function handleSave() {
  if (title.value.trim()) {
    emit('save', {
      title: title.value.trim(),
      description: description.value.trim(),
    })
  }
}

function handleCancel() {
  emit('cancel')
}
</script>

<template>
  <v-dialog :model-value="visible" max-width="500" persistent @update:model-value="handleCancel">
    <v-card>
      <v-card-title>
        <span class="text-h5">{{ category ? 'Editar Lista' : 'Nova Lista' }}</span>
      </v-card-title>

      <v-card-text>
        <v-form @submit.prevent="handleSave">
          <v-text-field
            v-model="title"
            label="Título*"
            :rules="titleRules"
            variant="outlined"
            class="mb-4"
            autofocus
          />

          <v-textarea
            v-model="description"
            label="Descrição"
            variant="outlined"
            rows="3"
            no-resize
          />
        </v-form>
      </v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn text @click="handleCancel"> Cancelar </v-btn>
        <v-btn color="primary" variant="elevated" :disabled="!title.trim()" @click="handleSave">
          Salvar
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
