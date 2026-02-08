export interface User {
  id: string
  name: string
  email: string
  createdAt: string
}

export interface Category {
  id: string
  title: string
  description: string | null
  createdAt: string
  updatedAt: string
}

export interface Task {
  id: string
  title: string
  description: string | null
  completed: boolean
  categoryId: string | null
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

export interface CategoryRequest {
  title: string
  description?: string
}

export interface TaskRequest {
  title: string
  description?: string
  completed?: boolean
  categoryId?: string
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
