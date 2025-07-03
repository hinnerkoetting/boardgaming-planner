<template>
  <div>
    <h1>Group {{ gameGroup.name }}</h1>

    <h2>Already played games</h2>
    <GamesList v-if="games.length > 0" :games="games" :game-group-id="gameGroupId" />

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
} from '@/services/api/ApiService'
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import { GameGroup } from '@/model/GameGroup'
import { useRoute } from 'vue-router'
import type { Player } from '@/model/Player/Player'
import type { Game } from '@/model/Game'
import AddGameBgg from '@/components/Game/AddGameBgg.vue'
import AddExistingGame from '@/components/Game/AddExistingGame.vue'
import type { RatedGame } from '@/model/RatedGame'
import GamesList from '@/components/Game/GamesList.vue'

const gameGroup: Ref<GameGroup> = ref(new GameGroup(-1, ''))
const players: Ref<Player[]> = ref([])
const games: Ref<RatedGame[]> = ref([])

const route = useRoute()
const gameGroupId = Number(route.params.gameGroupId)

onMounted(async () => {
  loadGameGroup(gameGroupId).then((result) => {
    gameGroup.value = result
  })
  fetchPlayersInGroup(gameGroupId).then((result) => {
    players.value = result
  })
  fetchGamesInGroup(gameGroupId).then((result) => {
    games.value = result as RatedGame[]
  })
})

async function onGameAdded(game: Game) {
  if (!game.id) {
    console.log('Game has no id')
    return
  }
  if (games.value.find((existingGame) => existingGame.id == game.id)) {
    console.log('Game already exists')
    return
  }
  await addGameToGroup(gameGroupId, game.id)
  const ratedGame: RatedGame = {
    ...game,
    rating: {
      myRating: undefined,
      averageRating: 0,
      existsVeto: false
    }
  }
  games.value.push(ratedGame)
}
</script>
