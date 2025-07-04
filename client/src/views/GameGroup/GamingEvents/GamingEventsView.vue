<template>
  <div>
    <h1>Group {{ gameGroup?.name }}</h1>
    <router-link :to="{ name: 'gameGroup', params: { gameGroupId: gameGroupId }}">To games</router-link>
    
    <h2>Next events</h2>
    <GamingEventsCollection
      v-if="gamingEvents.length > 0"
      :gaming-events="gamingEvents"
      />

   
  </div>
</template>

<script setup lang="ts">
import GamingEventsCollection from '@/components/GamingEvents/GamingEventsCollection.vue';
import type { GameGroup } from '@/model/GameGroup';
import type { GamingEvent } from '@/model/GamingEvent';
import { loadGameGroup } from '@/services/api/GameGroupApiService';
import { fetchNextGamingEvents } from '@/services/api/GamingEventsApiService';
import { onMounted, ref, type Ref } from 'vue';
import { useRoute } from 'vue-router';


const route = useRoute()

const gameGroup: Ref<GameGroup | null> = ref(null);
const gamingEvents: Ref<GamingEvent[]> = ref([])
const gameGroupId = Number(route.params.gameGroupId)

onMounted(async () => {
  loadGameGroup(gameGroupId).then((result) => {
    gameGroup.value = result
  })
  fetchNextGamingEvents(gameGroupId).then((events) => {
    gamingEvents.value = events
  })
});
</script>