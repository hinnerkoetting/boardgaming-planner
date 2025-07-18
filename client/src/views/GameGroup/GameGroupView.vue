<template>
  <div>
    <GameGroupComponent :gameGroupId="gameGroupId" />
  </div>
</template>

<script setup lang="ts">
import GameGroupComponent from '@/components/GameGroup/GameGroupComponent.vue';
import { useRoute } from 'vue-router';


const route = useRoute()
const gameGroupId = Number(route.params.gameGroupId)

</script>
