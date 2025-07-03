<template>
  <div>
    <h1>All games</h1>

    <GamesTableAdmin @clickDelete="onClickDelete" @game-edited="onGameEdited" :games="games" />
    <AddGame @game-added="onGameAdded" />

    <Message class="info"
      >Sync all games from BoardGameGeek. Works only once per day and happens in the background, to
      not overload BoardGameGeek.</Message
    >

    <Message v-if="errorMessage" severity="error" class="full-width">{{ errorMessage }}</Message>
    <Button severity="warn" @click="syncAllGames">Sync all games</Button>
  </div>
</template>

<script setup lang="ts">
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import type { AdminGame } from '@/model/Game'
import AddGame from '@/components/Game/Admin/AddGame.vue'
import GamesTableAdmin from '@/components/Game/Admin/GamesTableAdmin.vue'
import { deleteGame, fetchGames } from '@/services/api/GameApiService'
import Message from 'primevue/message'
import Button from 'primevue/button'
import { syncAllGamesFromBgg } from '@/services/api/BggApiService'

const games: Ref<AdminGame[]> = ref([])
const errorMessage = ref('')

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

async function syncAllGames() {
  errorMessage.value = ''
  const response = await syncAllGamesFromBgg()
  if (response.error) {
    errorMessage.value = response.error.detail || 'Error'
  }
}
</script>

<style lang="css" scoped>
.info {
  margin-top: 32px;
}
</style>
