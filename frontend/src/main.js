import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // Importa o arquivo de rotas

const app = createApp(App)

app.use(router) // Diz ao Vue para usar o sistema de navegação
app.mount('#app')