import { createApp } from 'vue'
import App from './App.vue'

import router from './router/index.js'
import {createPinia} from 'pinia'
const pinia = createPinia()

import piniaPluginPersistedstate from "pinia-plugin-persistedstate"

pinia.use(piniaPluginPersistedstate)

import ElementPlus from 'element-plus' //导入element-plus
import 'element-plus/dist/index.css' //导入element-plus样式

createApp(App).use(router).use(pinia).use(ElementPlus).mount('#app')