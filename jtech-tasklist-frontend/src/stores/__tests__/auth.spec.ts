import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useAuthStore } from '../auth'

vi.mock('@/services/authService', () => ({
  authService: {
    login: vi.fn(),
    register: vi.fn(),
    refresh: vi.fn(),
  },
}))

import { authService } from '@/services/authService'

const mockToken =
  'eyJhbGciOiJIUzI1NiJ9.' +
  btoa(JSON.stringify({ sub: '123', email: 'test@test.com' })) +
  '.signature'

describe('Auth Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
    vi.clearAllMocks()
  })

  it('should start with null state', () => {
    const store = useAuthStore()
    expect(store.user).toBeNull()
    expect(store.token).toBeNull()
    expect(store.refreshToken).toBeNull()
    expect(store.isAuthenticated).toBe(false)
  })

  it('should load from localStorage', () => {
    localStorage.setItem('token', 'my-token')
    localStorage.setItem('refreshToken', 'my-refresh')
    localStorage.setItem('user', JSON.stringify({ id: '1', name: 'Test', email: 'test@test.com', createdAt: '' }))

    const store = useAuthStore()
    store.loadFromStorage()

    expect(store.token).toBe('my-token')
    expect(store.refreshToken).toBe('my-refresh')
    expect(store.user?.email).toBe('test@test.com')
    expect(store.isAuthenticated).toBe(true)
  })

  it('should handle invalid user JSON in localStorage', () => {
    localStorage.setItem('user', 'not-json')

    const store = useAuthStore()
    store.loadFromStorage()

    expect(store.user).toBeNull()
  })

  it('should login and set state', async () => {
    vi.mocked(authService.login).mockResolvedValue({
      token: mockToken,
      refreshToken: 'refresh-123',
      type: 'Bearer',
    })

    const store = useAuthStore()
    await store.login({ email: 'test@test.com', password: '123456' })

    expect(store.token).toBe(mockToken)
    expect(store.refreshToken).toBe('refresh-123')
    expect(store.user?.email).toBe('test@test.com')
    expect(store.isAuthenticated).toBe(true)
    expect(localStorage.getItem('token')).toBe(mockToken)
  })

  it('should register via authService', async () => {
    const mockResponse = { id: '1', name: 'Test', email: 'test@test.com', createdAt: '' }
    vi.mocked(authService.register).mockResolvedValue(mockResponse)

    const store = useAuthStore()
    const result = await store.register({ name: 'Test', email: 'test@test.com', password: '123456' })

    expect(result).toEqual(mockResponse)
    expect(authService.register).toHaveBeenCalledWith({
      name: 'Test',
      email: 'test@test.com',
      password: '123456',
    })
  })

  it('should logout and clear state', () => {
    localStorage.setItem('token', 'tok')
    localStorage.setItem('refreshToken', 'ref')
    localStorage.setItem('user', '{}')

    const store = useAuthStore()
    store.loadFromStorage()
    store.logout()

    expect(store.user).toBeNull()
    expect(store.token).toBeNull()
    expect(store.refreshToken).toBeNull()
    expect(store.isAuthenticated).toBe(false)
    expect(localStorage.getItem('token')).toBeNull()
    expect(localStorage.getItem('refreshToken')).toBeNull()
    expect(localStorage.getItem('user')).toBeNull()
  })

  it('should refresh token', async () => {
    vi.mocked(authService.refresh).mockResolvedValue({
      token: 'new-token',
      refreshToken: 'new-refresh',
      type: 'Bearer',
    })

    const store = useAuthStore()
    store.refreshToken = 'old-refresh'

    await store.doRefreshToken()

    expect(store.token).toBe('new-token')
    expect(store.refreshToken).toBe('new-refresh')
    expect(localStorage.getItem('token')).toBe('new-token')
  })

  it('should logout on refresh failure', async () => {
    vi.mocked(authService.refresh).mockRejectedValue(new Error('expired'))

    const store = useAuthStore()
    store.token = 'old-token'
    store.refreshToken = 'old-refresh'

    await store.doRefreshToken()

    expect(store.token).toBeNull()
    expect(store.refreshToken).toBeNull()
  })
})
