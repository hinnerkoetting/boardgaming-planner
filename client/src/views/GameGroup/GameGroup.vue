<template>
  <div>
    <h1>Group {{ gameGroup.name }}</h1>

    <h2>Already played games</h2>
    <DataTable :value="games" tableStyle="min-width: 20rem">
      <Column field="name" header="Name"></Column>
      <Column field="rating" header="Rating"></Column>
      <Column field="thumbnailUrl" header="Thumbnail">
        <template #body="slotProps">
          <Image :src="slotProps.data.thumbnailUrl" />
        </template>
      </Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button severity="secondary" @click="onClickRate(slotProps.data)"> Rate (random)</Button>
          <Button severity="danger" @click="oncClickDeleteRating(slotProps.data)">
            Delete rating
          </Button>
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
  deleteInterest,
  fetchGamesInGroup,
  fetchInterests,
  fetchPlayersInGroup,
  loadGameGroup,
  updateInterest
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
import type { Interest } from '@/model/Interest'
import type { RatedGame } from '@/model/RatedGame'
import Button from 'primevue/button'
import { getCurrentUserId } from '@/services/LoginService'

const gameGroup: Ref<GameGroup> = ref(new GameGroup(-1, ''))
const players: Ref<Player[]> = ref([])
const games: Ref<RatedGame[]> = ref([])
const myInterests: Ref<Interest[]> = ref([])

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
  fetchInterests(gameGroupId).then((result) => {
    myInterests.value = result
    games.value.forEach((game) => {
      const interest = myInterests.value.find((interest) => interest.gameId == game.id)
      if (interest) {
        game.rating = interest.rating
      }
    })
  })
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
  const ratedGame = game as RatedGame
  games.value.push(ratedGame)
}

async function onClickRate(game: RatedGame) {
  const randomRating = Math.floor(Math.random() * 5) as number
  await updateInterest({
    gameId: game.id!,
    playerId: getCurrentUserId(),
    gameGroupId: gameGroupId,
    rating: randomRating
  })
  game.rating = randomRating
}

async function oncClickDeleteRating(game: RatedGame) {
  await deleteInterest({
    gameId: game.id!,
    playerId: getCurrentUserId(),
    gameGroupId: gameGroupId,
    rating: undefined
  })
  games.value
    .filter((existingGame) => existingGame.id === game.id)
    .forEach((game) => (game.rating = undefined))
}
</script>

<script lang="ts"></script>
