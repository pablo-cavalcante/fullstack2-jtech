import api from './api'
import type { LoginRequest, RegisterRequest, AuthResponse, UserResponse } from '@/types'

export const authService = {
  async register(data: RegisterRequest): Promise<UserResponse> {
    const response = await api.post<UserResponse>('/auth/register', data)
    return response.data
  },

  async login(data: LoginRequest): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/login', data)
    return response.data
  },

  async refresh(refreshToken: string): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/refresh', { refreshToken })
    return response.data
  },
}
