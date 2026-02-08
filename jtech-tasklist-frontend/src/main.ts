import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createPersistedState } from './plugins/piniaPersistedState'
import vuetify from './plugins/vuetify'
import App from './App.vue'
import router from './router'

const app = createApp(App)

const pinia = createPinia()
pinia.use(createPersistedState())

app.use(pinia)
app.use(router)
app.use(vuetify)

app.mount('#app')
