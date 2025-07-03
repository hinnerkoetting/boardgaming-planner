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
}

@media (max-width: 1023px) {
  header {
    background: #f1f1f1;
  }
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
    width: 100%;
  }
}
</style>
