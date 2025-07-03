<template>
  <div>
    <InputText
      name="username"
      placeholder="Name"
      v-on:keyup.enter="onStartLogin"
      v-model="loginModel"
    />
    <InputText
      name="password"
      placeholder="Password"
      v-on:keyup.enter="onStartLogin"
      v-model="passwordModel"
    />
    <Button @click="onStartLogin">Login</Button>
  </div>
</template>

<script setup lang="ts">
import { login } from '@/services/LoginService'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import { ref } from 'vue'

const emits = defineEmits(['logged-in'])
const loginModel = ref('')
const passwordModel = ref('')
function onStartLogin() {
  login(loginModel.value, passwordModel.value)
  emits('logged-in')
}
</script>
