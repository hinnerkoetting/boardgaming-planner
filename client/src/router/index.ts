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
    {
      path: '/gameGroup/:gameGroupId/gamingEvents',
      name: 'groupGamingEventsOverview',
      component: () => import('../views/GameGroup/GamingEvents/GamingEventsOverview.vue')
    },
    {
      path: '/gameGroup/:gameGroupId/gamingEvents/:gamingEventId',
      name: 'groupGamingEvent',
      component: () => import('../views/GameGroup/GamingEvents/GamingEventView.vue')
    },
    {
      path: '/account',
      name: 'account',
      component: () => import('../views/AccountView.vue')
    },
    {
      path: '/game/:gameId',
      name: 'game',
      component: () => import('../views/GameView.vue')
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
    },
    {
      path: '/admin/tags',
      name: 'tags',
      component: () => import('../views/Admin/TagView.vue')
    }
  ]
})

router.beforeEach((to, from, next) => {
if (to.matched.some((record) => record.meta.publicArea)) {
    next()
  } else {
    if (!isLoggedIn()) {
      console.log('User is not logged in, redirecting to home')
      sessionStorage.setItem('redirectAfterLogin', to.fullPath);

      next({ name: 'home' })
    } else {
      next()
    }
  }
})

export default router
