import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { authService } from '@/services/authService'
import type { User, LoginRequest, RegisterRequest } from '@/types'
import { useTaskStore } from './tasks'
import { useCategoryStore } from './categories'

export const useAuthStore = defineStore('auth', () => {
  const user = ref<User | null>(null)
  const token = ref<string | null>(null)
  const refreshToken = ref<string | null>(null)

  const isAuthenticated = computed(() => !!token.value)
  const currentUser = computed(() => user.value)

  function loadFromStorage() {
    const storedToken = localStorage.getItem('token')
    const storedRefreshToken = localStorage.getItem('refreshToken')
    const storedUser = localStorage.getItem('user')

    if (storedToken) token.value = storedToken
    if (storedRefreshToken) refreshToken.value = storedRefreshToken
    if (storedUser) {
      try {
        user.value = JSON.parse(storedUser)
      } catch {
        user.value = null
      }
    }
  }

  async function login(data: LoginRequest) {
    const response = await authService.login(data)
    token.value = response.token
    refreshToken.value = response.refreshToken

    localStorage.setItem('token', response.token)
    localStorage.setItem('refreshToken', response.refreshToken)

    // Decode user info from JWT payload
    const payload = JSON.parse(atob(response.token.split('.')[1]))
    user.value = {
      id: payload.sub,
      name: payload.email.split('@')[0],
      email: payload.email,
      createdAt: '',
    }
    localStorage.setItem('user', JSON.stringify(user.value))
  }

  async function register(data: RegisterRequest) {
    const response = await authService.register(data)
    return response
  }

  function logout() {
    // Limpa estado da auth
    user.value = null
    token.value = null
    refreshToken.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('user')
    
    // Limpa outras stores
    const taskStore = useTaskStore()
    const categoryStore = useCategoryStore()
    
    taskStore.$reset()
    categoryStore.$reset()
    
    // Limpa storage das stores
    localStorage.removeItem('tasks-store')
    localStorage.removeItem('categories-store')
  }

  async function doRefreshToken() {
    if (!refreshToken.value) return
    try {
      const response = await authService.refresh(refreshToken.value)
      token.value = response.token
      refreshToken.value = response.refreshToken
      localStorage.setItem('token', response.token)
      localStorage.setItem('refreshToken', response.refreshToken)
    } catch {
      logout()
    }
  }

  return {
    user,
    token,
    refreshToken,
    isAuthenticated,
    currentUser,
    loadFromStorage,
    login,
    register,
    logout,
    doRefreshToken,
  }
}, {
  persist: {
    key: 'auth-store',
    storage: localStorage,
    paths: ['user', 'token', 'refreshToken']
  }
})
