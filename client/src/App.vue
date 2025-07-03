<script setup lang="ts">
import { RouterView } from 'vue-router'
import NavigationComponent from './components/other/NavigationComponent.vue'
</script>

<template>
  <header>
    <div class="wrapper">
      <NavigationComponent />
    </div>
  </header>

  <RouterView />
</template>

<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
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
