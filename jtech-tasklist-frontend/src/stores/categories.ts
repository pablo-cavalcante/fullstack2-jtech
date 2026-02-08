import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { categoryService } from '@/services/categoryService'
import type { Category, CategoryRequest } from '@/types'

export const useCategoryStore = defineStore('categories', () => {
  const categories = ref<Category[]>([])
  const selectedCategoryId = ref<string | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  const selectedCategory = computed(() => 
    categories.value.find((c) => c.id === selectedCategoryId.value) ?? null
  )

  const categoryCount = computed(() => categories.value.length)

  async function fetchCategories() {
    loading.value = true
    error.value = null
    try {
      categories.value = await categoryService.getCategories()
      
      // Se não há categoria selecionada e há categorias disponíveis, seleciona a primeira
      if (!selectedCategoryId.value && categories.value.length > 0) {
        selectedCategoryId.value = categories.value[0].id
      }
      
      // Se a categoria selecionada foi deletada, seleciona a primeira disponível
      if (selectedCategoryId.value && !categories.value.find(c => c.id === selectedCategoryId.value)) {
        selectedCategoryId.value = categories.value.length > 0 ? categories.value[0].id : null
      }
    } catch (e: unknown) {
      error.value = e instanceof Error ? e.message : 'Failed to load categories'
      console.error('Error fetching categories:', e)
    } finally {
      loading.value = false
    }
  }

  async function createCategory(data: CategoryRequest) {
    error.value = null
    try {
      const category = await categoryService.createCategory(data)
      categories.value.unshift(category)
      
      // Seleciona automaticamente a nova categoria
      selectedCategoryId.value = category.id
      
      return category
    } catch (e: unknown) {
      error.value = e instanceof Error ? e.message : 'Failed to create category'
      console.error('Error creating category:', e)
      throw e
    }
  }

  async function updateCategory(id: string, data: CategoryRequest) {
    error.value = null
    try {
      const updated = await categoryService.updateCategory(id, data)
      const index = categories.value.findIndex((c) => c.id === id)
      if (index !== -1) {
        categories.value[index] = updated
      }
      return updated
    } catch (e: unknown) {
      error.value = e instanceof Error ? e.message : 'Failed to update category'
      console.error('Error updating category:', e)
      throw e
    }
  }

  async function deleteCategory(id: string) {
    error.value = null
    try {
      await categoryService.deleteCategory(id)
      
      // Se está deletando a categoria selecionada, seleciona outra
      if (selectedCategoryId.value === id) {
        const remainingCategories = categories.value.filter((c) => c.id !== id)
        selectedCategoryId.value = remainingCategories.length > 0 ? remainingCategories[0].id : null
      }
      
      categories.value = categories.value.filter((c) => c.id !== id)
    } catch (e: unknown) {
      error.value = e instanceof Error ? e.message : 'Failed to delete category'
      console.error('Error deleting category:', e)
      throw e
    }
  }

  function selectCategory(id: string | null) {
    selectedCategoryId.value = id
  }

  function getCategoryById(id: string): Category | undefined {
    return categories.value.find((c) => c.id === id)
  }

  function $reset() {
    categories.value = []
    selectedCategoryId.value = null
    loading.value = false
    error.value = null
  }

  return {
    categories,
    selectedCategoryId,
    selectedCategory,
    loading,
    error,
    categoryCount,
    fetchCategories,
    createCategory,
    updateCategory,
    deleteCategory,
    selectCategory,
    getCategoryById,
    $reset,
  }
}, {
  persist: {
    key: 'categories-store',
    storage: localStorage,
    paths: ['categories', 'selectedCategoryId']
  }
})
