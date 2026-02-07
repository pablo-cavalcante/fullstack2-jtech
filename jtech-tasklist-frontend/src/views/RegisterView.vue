<script setup lang="ts">
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

const name = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const loading = ref(false)
const error = ref('')
const showPassword = ref(false)
const form = ref<{ validate: () => Promise<{ valid: boolean }> } | null>(null)

const nameRules = [(v: string) => !!v?.trim() || 'Nome e obrigatorio']
const emailRules = [
  (v: string) => !!v?.trim() || 'Email e obrigatorio',
  (v: string) => /.+@.+\..+/.test(v) || 'Email invalido',
]
const passwordRules = [
  (v: string) => !!v || 'Senha e obrigatoria',
  (v: string) => v.length >= 6 || 'Senha deve ter no minimo 6 caracteres',
]
const confirmPasswordRules = [
  (v: string) => !!v || 'Confirmacao e obrigatoria',
  (v: string) => v === password.value || 'Senhas nao coincidem',
]

async function handleRegister() {
  const result = await form.value?.validate()
  if (!result?.valid) return

  loading.value = true
  error.value = ''

  try {
    await authStore.register({ name: name.value, email: email.value, password: password.value })
    router.push('/login')
  } catch (err: unknown) {
    const e = err as { response?: { data?: { message?: string } } }
    error.value = e.response?.data?.message || 'Erro ao cadastrar. Tente novamente.'
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
              <h2 class="text-h5">Criar Conta</h2>
              <p class="text-body-2 text-grey mt-1">Preencha os dados para se cadastrar</p>
            </div>
          </v-card-title>

          <v-card-text class="px-6">
            <v-alert v-if="error" type="error" variant="tonal" closable class="mb-4" @click:close="error = ''">
              {{ error }}
            </v-alert>

            <v-form ref="form" @submit.prevent="handleRegister">
              <v-text-field
                v-model="name"
                label="Nome"
                :rules="nameRules"
                prepend-inner-icon="mdi-account-outline"
                variant="outlined"
                class="mb-2"
              />
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
              <v-text-field
                v-model="confirmPassword"
                label="Confirmar Senha"
                :type="showPassword ? 'text' : 'password'"
                :rules="confirmPasswordRules"
                prepend-inner-icon="mdi-lock-check-outline"
                variant="outlined"
                class="mb-2"
              />
              <v-btn
                type="submit"
                color="primary"
                block
                size="large"
                :loading="loading"
                class="mt-2"
              >
                Cadastrar
              </v-btn>
            </v-form>
          </v-card-text>

          <v-card-actions class="justify-center pb-6">
            <span class="text-body-2">Ja tem conta?</span>
            <v-btn variant="text" color="primary" size="small" :to="{ path: '/login' }">
              Entrar
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
