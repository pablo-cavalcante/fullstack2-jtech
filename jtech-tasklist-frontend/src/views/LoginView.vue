<script setup lang="ts">
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

const email = ref('')
const password = ref('')
const loading = ref(false)
const error = ref('')
const showPassword = ref(false)
const form = ref<{ validate: () => Promise<{ valid: boolean }> } | null>(null)

const emailRules = [
  (v: string) => !!v?.trim() || 'Email e obrigatorio',
  (v: string) => /.+@.+\..+/.test(v) || 'Email invalido',
]
const passwordRules = [(v: string) => !!v || 'Senha e obrigatoria']

async function handleLogin() {
  const result = await form.value?.validate()
  if (!result?.valid) return

  loading.value = true
  error.value = ''

  try {
    await authStore.login({ email: email.value, password: password.value })
    router.push('/tasks')
  } catch (err: unknown) {
    const e = err as { response?: { data?: { message?: string } } }
    error.value = e.response?.data?.message || 'Erro ao fazer login. Verifique suas credenciais.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <v-container class="fill-height" fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="4">
        <v-card class="elevation-8">
          <v-card-title class="text-center pa-6">
            <div>
              <v-icon size="48" color="primary" class="mb-2">mdi-checkbox-marked-outline</v-icon>
              <h2 class="text-h5">JTech Tasklist</h2>
              <p class="text-body-2 text-grey mt-1">Faca login para continuar</p>
            </div>
          </v-card-title>

          <v-card-text class="px-6">
            <v-alert v-if="error" type="error" variant="tonal" closable class="mb-4" @click:close="error = ''">
              {{ error }}
            </v-alert>

            <v-form ref="form" @submit.prevent="handleLogin">
              <v-text-field
                v-model="email"
                label="Email"
                type="email"
                :rules="emailRules"
                prepend-inner-icon="mdi-email-outline"
                variant="outlined"
                class="mb-2"
              />
              <v-text-field
                v-model="password"
                label="Senha"
                :type="showPassword ? 'text' : 'password'"
                :rules="passwordRules"
                prepend-inner-icon="mdi-lock-outline"
                :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                variant="outlined"
                class="mb-2"
                @click:append-inner="showPassword = !showPassword"
              />
              <v-btn
                type="submit"
                color="primary"
                block
                size="large"
                :loading="loading"
                class="mt-2"
              >
                Entrar
              </v-btn>
            </v-form>
          </v-card-text>

          <v-card-actions class="justify-center pb-6">
            <span class="text-body-2">Nao tem conta?</span>
            <v-btn variant="text" color="primary" size="small" :to="{ path: '/register' }">
              Cadastre-se
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
