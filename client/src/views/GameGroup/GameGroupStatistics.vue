

<template>
  <div class="charts">
    <h1>Statistics</h1>
    <router-link :to="{ name: 'gameGroup', params: { gameGroupId: gameGroupId }}">To games</router-link>
    <GamesRatingStatistics :games="games" v-if="games.length > 0"/>
    <MyGamesRatingStatistics :games="games" v-if="games.length > 0"/>
    <GamesTagsStatistics :games="games" v-if="games.length > 0"/>
  </div>
</template>

<script setup lang="ts">
import GamesRatingStatistics from '@/components/Statistics/GamesRatingStatistics.vue';
import GamesTagsStatistics from '@/components/Statistics/GamesTagsStatistics.vue';
import MyGamesRatingStatistics from '@/components/Statistics/MyGamesRatingStatistics.vue';
import type { RatedGame } from '@/model/Game';
import { fetchGamesInGroup } from '@/services/api/GameGroupApiService';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const games = ref<RatedGame[]>([]);
const route = useRoute()
const gameGroupId = route.params.gameGroupId

onMounted(async () => {  
  games.value = await fetchGamesInGroup(Number(gameGroupId));
}); 
</script>


<style lang="css" scoped>
.charts {  
  width: 100%;
  max-width: 1000px;
}  
</style>