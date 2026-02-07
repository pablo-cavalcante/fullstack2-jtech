import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import TaskItem from '../TaskItem.vue'
import type { Task } from '@/types'

const mockTask: Task = {
  id: '1',
  title: 'Test Task',
  description: 'Test description',
  completed: false,
  createdAt: '2024-01-15T10:00:00Z',
  updatedAt: '2024-01-15T10:00:00Z',
}

function mountComponent(task: Task = mockTask) {
  return mount(TaskItem, {
    props: { task },
  })
}

describe('TaskItem', () => {
  it('should render task title', () => {
    const wrapper = mountComponent()
    expect(wrapper.text()).toContain('Test Task')
  })

  it('should render task description', () => {
    const wrapper = mountComponent()
    expect(wrapper.text()).toContain('Test description')
  })

  it('should render creation date', () => {
    const wrapper = mountComponent()
    expect(wrapper.text()).toContain('Criada em:')
  })

  it('should apply line-through class when completed', () => {
    const completedTask = { ...mockTask, completed: true }
    const wrapper = mountComponent(completedTask)
    const titleEl = wrapper.find('.text-decoration-line-through')
    expect(titleEl.exists()).toBe(true)
  })

  it('should not apply line-through when not completed', () => {
    const wrapper = mountComponent()
    const titleEl = wrapper.find('.text-decoration-line-through')
    expect(titleEl.exists()).toBe(false)
  })

  it('should emit edit event on edit button click', async () => {
    const wrapper = mountComponent()
    const buttons = wrapper.findAllComponents({ name: 'VBtn' })
    const editBtn = buttons.find((b) => b.find('.mdi-pencil').exists())
    await editBtn?.trigger('click')
    expect(wrapper.emitted('edit')?.[0]).toEqual([mockTask])
  })

  it('should emit delete event on delete button click', async () => {
    const wrapper = mountComponent()
    const buttons = wrapper.findAllComponents({ name: 'VBtn' })
    const deleteBtn = buttons.find((b) => b.find('.mdi-delete').exists())
    await deleteBtn?.trigger('click')
    expect(wrapper.emitted('delete')?.[0]).toEqual([mockTask])
  })
})
