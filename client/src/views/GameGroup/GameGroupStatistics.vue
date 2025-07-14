

<template>
  <div class="charts">
    <h1>Statistics</h1>
    <router-link :to="{ name: 'gameGroup', params: { gameGroupId: gameGroupId }}">To games</router-link>
    <GamesRatingStatistics :games="games" v-if="games.length > 0"/>
    <MyGamesRatingStatistics :games="games" v-if="games.length > 0"/>
    <GamesTagsStatistics :games="games" v-if="games.length > 0"/>
    <GamesStatisticsPlayDates v-if="gameGroupStatistics" :gameGroupStatistics="gameGroupStatistics" />

    <Message v-if="errorMessage" severity="error">{{ errorMessage }}</Message>
  </div>
</template>

<script setup lang="ts">
import GamesRatingStatistics from '@/components/GameGroup/Statistics/GamesRatingStatistics.vue';
import GamesStatisticsPlayDates from '@/components/GameGroup/Statistics/GamesStatisticsPlayDates.vue';
import GamesTagsStatistics from '@/components/GameGroup/Statistics/GamesTagsStatistics.vue';
import MyGamesRatingStatistics from '@/components/GameGroup/Statistics/MyGamesRatingStatistics.vue';
import type { RatedGame } from '@/model/Game';
import type { GameGroupStatistics } from '@/model/GameGroupStatistics';
import { fetchGameGrouptStatistics, fetchGamesInGroup } from '@/services/api/GameGroupApiService';
import { onMounted, ref, type Ref } from 'vue';
import { useRoute } from 'vue-router';

const games = ref<RatedGame[]>([]);
const gameGroupStatistics: Ref<GameGroupStatistics | null> = ref(null);
const route = useRoute()
const gameGroupId = route.params.gameGroupId
const errorMessage = ref('');

onMounted(async () => {  
  games.value = await fetchGamesInGroup(Number(gameGroupId));

  const statisticsResponse = await fetchGameGrouptStatistics(Number(gameGroupId))
  if (statisticsResponse.success) {
    gameGroupStatistics.value = statisticsResponse.success;
  } else {
    errorMessage.value = statisticsResponse.error?.detail || 'Failed to load game group statistics';
  }
}); 
</script>


<style lang="css" scoped>
.charts {  
  width: 100%;
  max-width: 1000px;
}  
</style>