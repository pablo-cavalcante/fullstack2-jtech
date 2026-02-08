import api from './api'
import type { Category, CategoryRequest } from '@/types'

export const categoryService = {
  async getCategories(): Promise<Category[]> {
    const response = await api.get<Category[]>('/categories')
    return response.data
  },

  async getCategoryById(id: string): Promise<Category> {
    const response = await api.get<Category>(`/categories/${id}`)
    return response.data
  },

  async createCategory(data: CategoryRequest): Promise<Category> {
    const response = await api.post<Category>('/categories', data)
    return response.data
  },

  async updateCategory(id: string, data: CategoryRequest): Promise<Category> {
    const response = await api.put<Category>(`/categories/${id}`, data)
    return response.data
  },

  async deleteCategory(id: string): Promise<void> {
    await api.delete(`/categories/${id}`)
  },
}
