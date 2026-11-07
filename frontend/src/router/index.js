import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import PlanosCursosView from '../views/PlanosCursosView.vue'
import AulaView from '../views/AulaView.vue'
import AvaliacaoView from '../views/AvaliacaoView.vue'
import RankingView from '../views/RankingView.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        { path: '/', name: 'login', component: LoginView },
        { path: '/cursos', name: 'cursos', component: PlanosCursosView },
        { path: '/aula/:id', name: 'aula', component: AulaView },
        { path: '/avaliacao/:matriculaId', name: 'avaliacao', component: AvaliacaoView },
        { path: '/ranking', name: 'ranking', component: RankingView }
    ]
})

export default router