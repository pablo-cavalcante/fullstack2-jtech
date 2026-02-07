<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

function handleLogout() {
  authStore.logout()
  router.push('/login')
}
</script>

<template>
  <v-app-bar color="primary" prominent>
    <v-app-bar-title>
      <v-icon class="mr-2">mdi-checkbox-marked-outline</v-icon>
      JTech Tasklist
    </v-app-bar-title>
    <template #append>
      <span class="mr-4 text-body-1">{{ authStore.currentUser?.email }}</span>
      <v-btn icon @click="handleLogout">
        <v-icon>mdi-logout</v-icon>
        <v-tooltip activator="parent" location="bottom">Sair</v-tooltip>
      </v-btn>
    </template>
  </v-app-bar>
</template>
