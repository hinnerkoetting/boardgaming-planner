<script setup lang="ts">
import Login from '@/components/LoginComponent.vue'
import EventBus from '@/services/EventBus'
import { isLoggedIn } from '@/services/LoginService'
import { ref } from 'vue'

const isLoggedInRef = ref(isLoggedIn())

function onLoggedIn() {
  isLoggedInRef.value = true
}

EventBus.addEventListener('login-status', () => {
  isLoggedInRef.value = isLoggedIn()
})
</script>

<template>
  <div>
    <img alt="logo" class="logo" src="@/assets/logo.svg" />
    <Login v-if="!isLoggedInRef" @logged-in="onLoggedIn" />
    <RouterLink v-if="!isLoggedInRef" to="register">Register</RouterLink>
  </div>
</template>

<style lang="css" scoped></style>
