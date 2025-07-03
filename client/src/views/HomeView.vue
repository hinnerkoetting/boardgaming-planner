<script setup lang="ts">
import LoginComponent from '@/components/LoginComponent.vue'
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
  <div class="wrapper full-width">
    <img alt="logo" class="logo" src="@/assets/logo.svg" />
    <div class="wrapper2 full-width">
      <LoginComponent v-if="!isLoggedInRef" @logged-in="onLoggedIn" />
      <RouterLink v-if="!isLoggedInRef" to="register" id="register">Register</RouterLink>
    </div>
  </div>
</template>

<style lang="css" scoped>

.logo {
  max-width: 100%;
}

.wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  flex-wrap: wrap;
}

.wrapper2 {
  display: flex;
  flex-direction: column;
}

#register {
  align-self: flex-start;
  margin-top: 12px;
}

.full-width { 
  width: 100%;
}


</style>
