<template>
  <div>
    <div v-if="isLoggedInRef">
      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/gameGroups">Groups</RouterLink>
        <RouterLink :to="'/gameGroup/' + selectedGameGroup.id" v-if="selectedGameGroup">
          <div to="/gameGroups/1" id="gameGroupSelected" v-if="selectedGameGroup">
            {{ selectedGameGroup.name }}
          </div>
        </RouterLink>
        <a id="logout" @click="onClickLogout">Logout</a>
      </nav>
      <div v-if="doesCurrentPlayerHaveRole(Role.ADMIN)">
        <h1>Admin</h1>
        <nav>
          <RouterLink to="/admin/players">Players</RouterLink>
          <RouterLink to="/admin/games">Games</RouterLink>
          <RouterLink to="/admin/gameGroups">Groups</RouterLink>
        </nav>
      </div>
    </div>
  </div>
</template>

<style lang="css" scoped>
h1 {
  margin-top: 100px;
}

nav {
  width: 100%;
  font-size: 20px;
  text-align: center;
  margin-top: 2rem;

  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }

  nav {
    text-align: left;
    margin-left: -1rem;
    font-size: 1.5rem;

    padding: 1rem 0;
    margin-top: 1rem;
  }
}

#gameGroupSelected {
  color: gray;
  margin-left: 2rem;
}

#logout {
  cursor: pointer;
  margin-top: 20px;
}
</style>

<script setup lang="ts">
import { doesCurrentPlayerHaveRole, isLoggedIn, logout } from '@/services/LoginService'
import router from '@/router'
import { Role } from '@/model/api/JwtPayload'
import { ref, type Ref } from 'vue'
import EventBus, { EventBusEvent } from '@/services/EventBus'
import type { GameGroup } from '@/model/GameGroup'

const isLoggedInRef = ref(isLoggedIn())
const selectedGameGroup: Ref<GameGroup | undefined> = ref(undefined)

EventBus.addEventListener('login-status', () => {
  isLoggedInRef.value = isLoggedIn()
})

EventBus.addEventListener('gaming-group-opened', (event: Event) => {
  const eventBusEvent = event as EventBusEvent
  selectedGameGroup.value = eventBusEvent.data
})

function onClickLogout() {
  logout()
  router.push('home')
}
</script>
