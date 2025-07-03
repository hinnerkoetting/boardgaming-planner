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
      path: '/gameGroups',
      name: 'gameGroups',
      component: () => import('../views/GameGroup/GameGroupOverview.vue')
    },
    {
      path: '/gameGroup/:gameGroupId',
      name: 'gameGroup',
      component: () => import('../views/GameGroup/GameGroupView.vue')
    },

    // Admin
    {
      path: '/admin/players',
      name: 'adminPlayers',
      component: () => import('../views/Admin/PlayersListAdmin.vue')
    },
    {
      path: '/admin/games',
      name: 'adminGames',
      component: () => import('../views/Admin/GamesAdmin.vue')
    },
    {
      path: '/admin/gameGroups',
      name: 'adminGameGroups',
      component: () => import('../views/Admin/GameGroupAdmin.vue')
    },
    {
      path: '/admin/gameGroups/:gameGroupId',
      name: 'adminPlayersInGameGroups',
      component: () => import('../views/Admin/PlayersInGameGroupAdmin.vue')
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
