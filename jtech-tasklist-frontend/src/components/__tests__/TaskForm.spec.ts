import { describe, it, expect, afterEach } from 'vitest'
import { mount } from '@vue/test-utils'
import TaskForm from '../TaskForm.vue'

function mountComponent(props = {}) {
  const div = document.createElement('div')
  document.body.appendChild(div)
  return mount(TaskForm, {
    props: { visible: true, ...props },
    attachTo: div,
  })
}

describe('TaskForm', () => {
  afterEach(() => {
    document.body.innerHTML = ''
  })

  it('should show "Nova Tarefa" title when no task prop', () => {
    mountComponent()
    expect(document.body.textContent).toContain('Nova Tarefa')
  })

  it('should show "Editar Tarefa" title when task prop provided', () => {
    mountComponent({
      task: {
        id: '1',
        title: 'Edit me',
        description: 'Desc',
        completed: false,
        createdAt: '',
        updatedAt: '',
      },
    })
    expect(document.body.textContent).toContain('Editar Tarefa')
  })

  it('should emit cancel when cancel button clicked', async () => {
    const wrapper = mountComponent()
    const cancelBtn = wrapper
      .findAllComponents({ name: 'VBtn' })
      .find((b) => b.text() === 'Cancelar')
    await cancelBtn?.trigger('click')
    expect(wrapper.emitted('cancel')).toBeTruthy()
  })

  it('should render title and description fields', () => {
    const wrapper = mountComponent()
    const fields = wrapper.findAllComponents({ name: 'VTextField' })
    const textareas = wrapper.findAllComponents({ name: 'VTextarea' })
    expect(fields.length).toBeGreaterThanOrEqual(1)
    expect(textareas.length).toBeGreaterThanOrEqual(1)
  })
})
