<template>
  <div>
    <RouterLink to="/">back</RouterLink>
    <h1>Sign up</h1>
    <div class="wrapper">
      <InputText
        name="username"
        placeholder="Name"
        autocomplete="given-name"
        v-on:keyup.enter="onStartRegister"
        v-model="nameModel"
      />
      <InputText
        name="password"
        placeholder="Password"
        autocomplete="new-password"
        type="password"
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
import router from '@/router'

const nameModel = ref('')
const passwordModel = ref('')

async function onStartRegister() {
  await register(nameModel.value, passwordModel.value)
  router.push('/gameGroups')
}
</script>

<style lang="css" scoped>
.wrapper {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
</style>
