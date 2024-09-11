import { createApp } from 'vue';
import App from './App.vue';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from '@/router';
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import './style.css'
import './assets/icon/iconfont.css'



const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App);
app.use(router)
app.use(pinia)
app.use(ElementPlus).mount('#app');

// 将 Vue 实例暴露到全局
window.vueApp = app;