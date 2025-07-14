<template>
  <div>
    <h1>Statistics for {{ game?.name }}</h1>
    <Message v-if="errorMessage" severity="error">{{ errorMessage }}</Message>
    <div class="row">      
      <div>Last played</div>
      <div>{{ (gameStatistics?.lastPlayed && formatDate(new Date(gameStatistics.lastPlayed))) || 'Not played yet' }}</div>
    </div>
    <div class="row">      
      <div>Plays</div>
      <div>{{ gameStatistics?.playedNumberOfTimes || 0 }}</div>
    </div>
    <div class="row">      
      <GamesStatisticsPlayDates v-if="gameStatistics" :gameStatistics="gameStatistics" />
    </div>
  </div>
</template>


<script setup lang="ts">
import GamesStatisticsPlayDates from '@/components/Game/Statistics/GamesStatisticsPlayDates.vue'
import type { Game } from '@/model/Game'
import type { GameStatistics } from '@/model/GameStatistics'
import { loadGame, loadGameStatistics } from '@/services/api/GameApiService'
import { Message } from 'primevue'
import { onMounted, ref, type Ref } from 'vue'
import { useRoute } from 'vue-router'


const game: Ref<Game | undefined> = ref(undefined)
const gameStatistics: Ref<GameStatistics | undefined> = ref(undefined)
const route = useRoute()
const gameId = Number(route.params.gameId)
const errorMessage = ref('')

onMounted(async () => {
  game.value = await loadGame(gameId)
  const gameStatisticsResponse = await loadGameStatistics(gameId)
  if (gameStatisticsResponse.success) {
    gameStatistics.value = gameStatisticsResponse.success
  } else {
    errorMessage.value = gameStatisticsResponse.error?.detail || 'Failed to load game statistics'
  }
  
})

function formatDate(date: Date) {
  return date.toLocaleDateString(undefined, { day: "numeric", month: "long", weekday: "long" });
}

</script>

<style lang="css" scoped>

.content {
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  align-items: stretch;
  justify-content: space-between;
}

.row {
  max-width: min(100%, 600px);
  width: 100%;
  border: 0;
  border-top: 1px dashed #ccc;
  display: flex;
  flex-wrap: wrap;
  margin-top: 16px;
  line-height: 28px;
   justify-content: space-between;
}

</style>