import type { PiniaPluginContext } from 'pinia'

export interface PersistOptions {
  key?: string
  storage?: Storage
  paths?: string[]
}

export function createPersistedState() {
  return ({ options, store }: PiniaPluginContext) => {
    const persistOptions = (options as any).persist as PersistOptions | undefined

    if (!persistOptions) return

    const {
      key = store.$id,
      storage = localStorage,
      paths,
    } = persistOptions

    // Carrega o estado salvo
    const savedState = storage.getItem(key)
    if (savedState) {
      try {
        const parsed = JSON.parse(savedState)
        
        if (paths && Array.isArray(paths)) {
          // Se paths foi especificado, restaura apenas esses caminhos
          paths.forEach(path => {
            if (parsed[path] !== undefined) {
              store.$state[path] = parsed[path]
            }
          })
        } else {
          // Caso contrário, restaura tudo
          store.$patch(parsed)
        }
      } catch (e) {
        console.error(`Failed to parse persisted state for ${key}:`, e)
      }
    }

    // Salva o estado quando houver mudanças
    store.$subscribe(() => {
      try {
        let stateToSave = store.$state

        if (paths && Array.isArray(paths)) {
          // Se paths foi especificado, salva apenas esses caminhos
          stateToSave = paths.reduce((acc, path) => {
            acc[path] = store.$state[path]
            return acc
          }, {} as any)
        }

        storage.setItem(key, JSON.stringify(stateToSave))
      } catch (e) {
        console.error(`Failed to persist state for ${key}:`, e)
      }
    })
  }
}
