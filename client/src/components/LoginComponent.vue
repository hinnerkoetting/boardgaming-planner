<template>
  <div class="wrapper">
    <InputText
      name="username"
      placeholder="Name"
      autocomplete="username"
      v-on:keyup.enter="onStartLogin"
      v-model="loginModel"
      class="full-width"
    />
    <InputText
      name="password"
      type="password"
      placeholder="Password"
      autocomplete="password"
      v-on:keyup.enter="onStartLogin"
      v-model="passwordModel"
      class="full-width"
    />

    <Message v-if="errorMessage" severity="error" class="full-width">{{ errorMessage }}</Message>
    <Button @click="onStartLogin" class="full-width">Login</Button>
  </div>
</template>

<script setup lang="ts">
import { isLoggedIn, login } from '@/services/LoginService'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import { ref } from 'vue'
import router from '@/router'
import Message from 'primevue/message'

const emits = defineEmits(['logged-in'])
const loginModel = ref('')
const passwordModel = ref('')
const errorMessage = ref('')

async function onStartLogin() {
  errorMessage.value = ''
  const loginResponse = await login(loginModel.value, passwordModel.value)
  if (isLoggedIn()) {
    emits('logged-in')
    router.push('gameGroups')
  } else {
    errorMessage.value = loginResponse || 'Could not login'
  }
}
</script>

<style scoped lang="css">
.wrapper {
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  gap: 4px;
  width: 250px;
}

.full-width {
  width: 100%;
}
</style>
