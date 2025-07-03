<script setup lang="ts">
import Login from '@/components/LoginComponent.vue'
import { isLoggedIn } from '@/services/LoginService'
import { watch } from 'vue'
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const isLoggedInRef = ref(isLoggedIn())
function onLoggedIn() {
  isLoggedInRef.value = true
}
</script>

<template>
  <div>
    <img alt="logo" class="logo" src="@/assets/logo.svg" />
    <Login v-if="!isLoggedInRef" @logged-in="onLoggedIn" />
  </div>
</template>

<style lang="css" scoped></style>
