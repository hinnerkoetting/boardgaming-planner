<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import { isLoggedIn, logout } from './services/LoginService'
import EventBus from './services/EventBus'
import { ref } from 'vue'

const isLoggedInRef = ref(isLoggedIn())

EventBus.addEventListener('login-status', () => {
  isLoggedInRef.value = isLoggedIn()
})

function onClickLogout() {
  logout()
}
</script>

<template>
  <header>
    <div class="wrapper">
      <nav>
        <RouterLink to="/" v-if="isLoggedInRef">Home</RouterLink>
        <a id="logout" @click="onClickLogout" v-if="isLoggedInRef">Logout</a>
      </nav>

      <h1>Admin</h1>
      <nav>
        <RouterLink to="/players" v-if="isLoggedInRef">Players</RouterLink>
        <RouterLink to="/games" v-if="isLoggedInRef">Games</RouterLink>
        <RouterLink to="/gameGroups" v-if="isLoggedInRef">Groups</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
</template>

<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
}

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
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
    width: 100%;
  }

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

#logout {
  cursor: pointer;
  margin-top: 20px;
}
</style>
