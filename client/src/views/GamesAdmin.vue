<template>
  <div>
    <h1>All games</h1>

    <GamesTable @delete="onClickDelete" :games="games" />
    <AddGame @game-added="onGameAdded" />
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
import { deleteGame, fetchGames } from '@/services/ApiService'
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import type { Game } from '@/model/Game'
import AddGame from '@/components/AddGame.vue'
import GamesTable from '@/components/GamesTable.vue'

const games: Ref<Game[]> = ref([] as Game[])

onMounted(async () => {
  games.value = await fetchGames()
})

function onClickDelete(id: Number) {
  deleteGame(id)
  games.value = games.value.filter((item) => !(item.id === id))
}

function onGameAdded(game: Game) {
  games.value.push(game)
}
</script>

<script lang="ts"></script>
