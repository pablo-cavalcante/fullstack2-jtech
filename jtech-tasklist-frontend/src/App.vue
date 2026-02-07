<script setup lang="ts">
import { onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

onMounted(() => {
  authStore.loadFromStorage()
})
</script>

<template>
  <v-app>
    <router-view />
  </v-app>
</template>
