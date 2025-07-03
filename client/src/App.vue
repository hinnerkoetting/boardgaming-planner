<script setup lang="ts">
import { RouterView } from 'vue-router'
import NavigationComponent from './components/other/NavigationComponent.vue'
import Toast from 'primevue/toast'
</script>

<template>
  <header>
    <div class="wrapper">
      <NavigationComponent />
    </div>
  </header>

  <RouterView />
  <Toast />
</template>

<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
  width: 100%;
}

@media (min-width: 1024px) {
  header {
    max-width: 250px;
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }
}
</style>
