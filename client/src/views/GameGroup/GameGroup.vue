<template>
  <div>
    <h1>Group {{ gameGroup.name }}</h1>

    <h2>Already played games</h2>
    <DataTable :value="games" tableStyle="min-width: 20rem">
      <Column field="name" header="Name"></Column>
      <Column field="thumbnailUrl" header="Thumbnail">
        <template #body="slotProps">
          <Image :src="slotProps.data.thumbnailUrl" />
        </template>
      </Column>
    </DataTable>

    <h2>Players</h2>
    <DataTable :value="players" tableStyle="min-width: 20rem">
      <Column field="name" header="Name"></Column>
    </DataTable>
  </div>
</template>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>

<script setup lang="ts">
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import { fetchGamesInGroup, fetchPlayersInGroup, loadGameGroup } from '@/services/ApiService'
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import { GameGroup } from '@/model/GameGroup'
import { useRoute } from 'vue-router'
import type { Player } from '@/model/Player'
import type { Game } from '@/model/Game'
import Image from 'primevue/image'

const gameGroup: Ref<GameGroup> = ref(new GameGroup(-1, ''))
const players: Ref<Player[]> = ref([])
const games: Ref<Game[]> = ref([])

const route = useRoute()
const gameGroupId = Number(route.params.gameGroupId)

onMounted(async () => {
  gameGroup.value = await loadGameGroup(gameGroupId)
  players.value = await fetchPlayersInGroup(gameGroupId)
  games.value = await fetchGamesInGroup(gameGroupId)
})
</script>

<script lang="ts"></script>
