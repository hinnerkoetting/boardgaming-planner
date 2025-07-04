<script setup lang="ts">
import LoginComponent from '@/components/LoginComponent.vue'
import EventBus from '@/services/EventBus'
import { isLoggedIn } from '@/services/LoginService'
import Button from 'primevue/button'
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
    <div class="box">
      <img alt="logo" class="logo" src="@/assets/logo-full.png" />      
    </div>      
    
    
    <div class="wrapper2">      
      <LoginComponent v-if="!isLoggedInRef" @logged-in="onLoggedIn" />
      <RouterLink v-if="!isLoggedInRef" to="register" id="register">Register</RouterLink>
      <div>
        <Button severity="primary" v-if="isLoggedInRef">
          <RouterLink to="gameGroups" class="light-text">Show groups</RouterLink></Button
        ><br />
        <Button severity="secondary" class="margintop" v-if="isLoggedInRef">
          <RouterLink to="account">Account settings</RouterLink>
        </Button>
      </div>
    </div>
  </div>
</template>

<style lang="css" scoped>
.logo {
  max-width: 100%;
  margin-bottom: 16px;
  width: 500px;
  border-radius: 10%;
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
  align-items: center;
  justify-content: center;
}

#register {
  align-self: flex-start;
  margin-top: 12px;
}

.full-width {
  width: 100%;
}

.light-text {
  color: var(--text-color);
}

button {
  width: 150px;
}

.margintop {
  margin-top: 8px;
}

.box {
   display: flex;   

}


.box > h1 {
  margin-left: 16px;
}


</style>
