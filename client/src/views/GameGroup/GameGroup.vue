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

    <h2 class="green">Add game</h2>
    <AddExistingGame @game-added="onGameAdded" />

    <h2 class="green">Import from Boardgamegeek</h2>
    <AddGameBgg @game-added="onGameAdded" />

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
import {
  addGameToGroup,
  fetchGamesInGroup,
  fetchPlayersInGroup,
  loadGameGroup
} from '@/services/ApiService'
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import { GameGroup } from '@/model/GameGroup'
import { useRoute } from 'vue-router'
import type { Player } from '@/model/Player'
import type { Game } from '@/model/Game'
import Image from 'primevue/image'
import AddGameBgg from '@/components/Game/AddGameBgg.vue'
import AddExistingGame from '@/components/Game/AddExistingGame.vue'

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

async function onGameAdded(game: Game) {
  if (!game.id) {
    console.log('Game has no id')
    return
  }
  if (games.value.find((game) => game.id == game.id)) {
    console.log('Game already exists')
    return
  }
  await addGameToGroup(gameGroupId, game.id)
  games.value.push(game)
}
</script>

<script lang="ts"></script>
