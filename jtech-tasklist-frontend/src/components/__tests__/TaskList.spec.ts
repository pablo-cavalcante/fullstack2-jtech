import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import TaskList from '../TaskList.vue'
import type { Task } from '@/types'

const tasks: Task[] = [
  {
    id: '1',
    title: 'Pending Task',
    description: null,
    completed: false,
    createdAt: '2024-01-01',
    updatedAt: '2024-01-01',
  },
  {
    id: '2',
    title: 'Completed Task',
    description: null,
    completed: true,
    createdAt: '2024-01-02',
    updatedAt: '2024-01-02',
  },
]

function mountComponent(props = {}) {
  return mount(TaskList, {
    props: { tasks: [], loading: false, ...props },
  })
}

describe('TaskList', () => {
  it('should show empty state when no tasks', () => {
    const wrapper = mountComponent()
    expect(wrapper.text()).toContain('Nenhuma tarefa encontrada')
  })

  it('should show progress bar when loading', () => {
    const wrapper = mountComponent({ loading: true })
    expect(wrapper.findComponent({ name: 'VProgressLinear' }).exists()).toBe(true)
  })

  it('should render tasks', () => {
    const wrapper = mountComponent({ tasks })
    expect(wrapper.text()).toContain('Pending Task')
    expect(wrapper.text()).toContain('Completed Task')
  })

  it('should show correct count chips', () => {
    const wrapper = mountComponent({ tasks })
    expect(wrapper.text()).toContain('Todas (2)')
    expect(wrapper.text()).toContain('Pendentes (1)')
    expect(wrapper.text()).toContain('Concluidas (1)')
  })
})
