import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import LoginView from '../LoginView.vue'

vi.mock('vue-router', () => ({
  useRouter: () => ({
    push: vi.fn(),
  }),
}))

vi.mock('@/services/authService', () => ({
  authService: {
    login: vi.fn(),
    register: vi.fn(),
    refresh: vi.fn(),
  },
}))

function mountComponent() {
  return mount(LoginView, {
    global: {
      plugins: [createPinia()],
      stubs: {
        RouterLink: true,
      },
    },
  })
}

describe('LoginView', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
  })

  it('should render login form', () => {
    const wrapper = mountComponent()
    expect(wrapper.text()).toContain('JTech Tasklist')
    expect(wrapper.text()).toContain('Faca login para continuar')
  })

  it('should render email and password fields', () => {
    const wrapper = mountComponent()
    const textFields = wrapper.findAllComponents({ name: 'VTextField' })
    expect(textFields.length).toBe(2)
  })

  it('should render login button', () => {
    const wrapper = mountComponent()
    expect(wrapper.text()).toContain('Entrar')
  })

  it('should render register link', () => {
    const wrapper = mountComponent()
    expect(wrapper.text()).toContain('Nao tem conta?')
    expect(wrapper.text()).toContain('Cadastre-se')
  })
})
