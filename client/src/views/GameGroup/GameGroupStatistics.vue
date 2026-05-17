<template>
  <div class="charts">
    <h1>Statistics</h1>

    <div class="date-filters">
      <DatePicker v-model="startDate" placeholder="Start Date" />
      <DatePicker v-model="endDate" placeholder="End Date" />

      <Button @click="clearDates" label="Clear Dates" severity="secondary" />
    </div>

    <PlayedGamesPieChart v-if="gameGroupStatistics" :gameGroupStatistics="gameGroupStatistics" />
    <GamesStatisticsPlayDates v-if="gameGroupStatistics" :gameGroupStatistics="gameGroupStatistics" />

    <h1>General statistics</h1>
    <GamesRatingStatistics :games="games" v-if="games.length > 0" />
    <MyGamesRatingStatistics :games="games" v-if="games.length > 0" />
    <GamesTagsStatisticsPieCollection :games="games" v-if="games.length > 0" />
    <Message v-if="errorMessage" severity="error">{{ errorMessage }}</Message>
  </div>
</template>

<script setup lang="ts">
import GamesRatingStatistics from '@/components/GameGroup/Statistics/GamesRatingStatistics.vue'
import GamesStatisticsPlayDates from '@/components/GameGroup/Statistics/GamesStatisticsPlayDates.vue'
import GamesTagsStatisticsPieCollection from '@/components/GameGroup/Statistics/GamesTagsStatisticsPieCollection.vue'
import MyGamesRatingStatistics from '@/components/GameGroup/Statistics/MyGamesRatingStatistics.vue'
import PlayedGamesPieChart from '@/components/GameGroup/Statistics/PlayedGamesPieChart.vue'
import type { RatedGame } from '@/model/Game'
import type { GameGroupStatistics } from '@/model/GameGroupStatistics'
import { fetchGameGrouptStatistics, fetchGamesInGroup } from '@/services/api/GameGroupApiService'
import { Message } from 'primevue'
import { Button } from 'primevue'
import { DatePicker } from 'primevue'
import { onMounted, ref, watch, type Ref } from 'vue'
import { useRoute } from 'vue-router'

const games = ref<RatedGame[]>([])
const gameGroupStatistics: Ref<GameGroupStatistics | null> = ref(null)
const startDate = ref<Date | undefined>(undefined)
const endDate = ref<Date | undefined>(undefined)
const route = useRoute()
const gameGroupId = route.params.gameGroupId
const errorMessage = ref('')

const loadStatistics = async () => {
  const statisticsResponse = await fetchGameGrouptStatistics(
    Number(gameGroupId),
    startDate.value,
    endDate.value
  )
  if (statisticsResponse.success) {
    gameGroupStatistics.value = statisticsResponse.success
  } else {
    errorMessage.value = statisticsResponse.error?.detail || 'Failed to load game group statistics'
  }
}

const clearDates = async () => {
  startDate.value = undefined
  endDate.value = undefined
  await loadStatistics()
}

watch([startDate, endDate], async () => {
  await loadStatistics()
})

onMounted(async () => {
  games.value = await fetchGamesInGroup(Number(gameGroupId))
  await loadStatistics()
})
</script>

<style lang="css" scoped>
.charts {
  width: 100%;
  /* max-width: 1000px; */
}

.date-filters {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  align-items: center;
  flex-wrap: wrap;
}
</style>
