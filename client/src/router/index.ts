import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/players',
      name: 'players',
      component: () => import('../views/PlayerAdmin.vue')
    },
    {
      path: '/games',
      name: 'games',
      component: () => import('../views/GamesAdmin.vue')
    },
    {
      path: '/gameGroups',
      name: 'gameGroups',
      component: () => import('../views/GameGroupAdmin.vue')
    },
    {
      path: '/gameGroups/:gameGroupId',
      name: 'playersInGameGroups',
      component: () => import('../views/PlayersInGameGroupAdmin.vue')
    }
  ]
})

export default router
