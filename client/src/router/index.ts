import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import { isLoggedIn } from '@/services/LoginService'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: {
        publicArea: true
      }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/CreateUser.vue'),
      meta: {
        publicArea: true
      }
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

router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.publicArea)) {
    next()
  } else {
    if (!isLoggedIn()) {
      console.log('User is not logged in, redirecting to home')
      next({ name: 'home' })
    } else {
      next()
    }
  }
})

export default router
