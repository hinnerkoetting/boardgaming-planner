import './assets/main.css'

import { createApp } from 'vue'
import { createStore, useStore as baseUseStore } from 'vuex'

import App from './App.vue'
import router from './router'

import PrimeVue from 'primevue/config'
import Aura from '@primevue/themes/aura'
import ToastService from 'primevue/toastservice'
import type { State } from 'vue-state'
import type { TagModel } from './model/TagModel'

const store = createStore({
  state() {
    return {
      allTags: []
    }
  },
  mutations: {
    updateTags(state: State, newData: TagModel[]) {
      state.allTags = newData
    }
  }
})

export function useStore() {
  return baseUseStore()
}

const app = createApp(App)

app.use(ToastService)
app.use(router)
app.use(store)
app.use(PrimeVue, {
  theme: {
    preset: Aura,
    options: {
      darkModeSelector: 'none',
      cssLayer: false
    }
  }
})

app.mount('#app')
