<template>
  <div>
    <h1>Sign up</h1>
    <div>
      <InputText
        name="username"
        placeholder="Name"
        v-on:keyup.enter="onStartRegister"
        v-model="nameModel"
      />
      <InputText
        name="password"
        placeholder="Password"
        v-on:keyup.enter="onStartRegister"
        v-model="passwordModel"
      />
      <Button @click="onStartRegister">Register</Button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { register } from '@/services/LoginService'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const nameModel = ref('')
const passwordModel = ref('')
const router = useRouter()
async function onStartRegister() {
  await register(nameModel.value, passwordModel.value)
  router.push('/')
}
</script>
