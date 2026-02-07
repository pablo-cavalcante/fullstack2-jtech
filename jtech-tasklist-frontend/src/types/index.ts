export interface User {
  id: string
  name: string
  email: string
  createdAt: string
}

export interface Task {
  id: string
  title: string
  description: string | null
  completed: boolean
  createdAt: string
  updatedAt: string
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  name: string
  email: string
  password: string
}

export interface TaskRequest {
  title: string
  description?: string
  completed?: boolean
}

export interface AuthResponse {
  token: string
  refreshToken: string
  type: string
}

export interface UserResponse {
  id: string
  name: string
  email: string
  createdAt: string
}
