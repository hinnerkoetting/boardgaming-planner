<template>
  <div>
    <h1>All games</h1>

    <GamesTableAdmin @clickDelete="onClickDelete" @game-edited="onGameEdited" :games="games" />
    <AddGame @game-added="onGameAdded" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import type { AdminGame } from '@/model/Game'
import AddGame from '@/components/Game/Admin/AddGame.vue'
import GamesTableAdmin from '@/components/Game/Admin/GamesTableAdmin.vue'
import { deleteGame, fetchGames } from '@/services/api/GameApiService'

const games: Ref<AdminGame[]> = ref([])

onMounted(async () => {
  games.value = await fetchGames()
})

function onClickDelete(game: AdminGame) {
  deleteGame(game.id!)
  games.value = games.value.filter((item) => !(item.id === game.id!))
}

function onGameEdited(game: AdminGame) {
  games.value = games.value.filter((item) => !(item.id === game.id!))
  games.value.push(game)
}

function onGameAdded(game: AdminGame) {
  games.value.push(game)
}
</script>
