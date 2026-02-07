<script setup lang="ts">
defineProps<{
  visible: boolean
  title: string
  message: string
}>()

const emit = defineEmits<{
  confirm: []
  cancel: []
}>()
</script>

<template>
  <v-dialog :model-value="visible" max-width="400" @update:model-value="emit('cancel')">
    <v-card>
      <v-card-title class="text-h6">{{ title }}</v-card-title>
      <v-card-text>{{ message }}</v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn variant="text" @click="emit('cancel')">Cancelar</v-btn>
        <v-btn color="error" variant="elevated" @click="emit('confirm')">Confirmar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
