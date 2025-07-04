

<template>
  <div><GamesRatingStatistics :games="games" v-if="games.length > 0"/></div>
</template>

<script setup lang="ts">
import GamesRatingStatistics from '@/components/Statistics/GamesRatingStatistics.vue';
import type { RatedGame } from '@/model/Game';
import { fetchGamesInGroup } from '@/services/api/GameGroupApiService';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const games = ref<RatedGame[]>([]);

onMounted(async () => {
  const route = useRoute()
  games.value = await fetchGamesInGroup(Number(route.params.gameGroupId));
}); 
</script>