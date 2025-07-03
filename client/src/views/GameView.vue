<template>
  <div><ShowGameDetailsComponent v-if="game" :game="game" /></div>
</template>

<script setup lang="ts">
import ShowGameDetailsComponent from '@/components/Game/ShowGameDetailsComponent.vue'
import type { Game } from '@/model/Game'
import { loadGame } from '@/services/api/GameApiService'
import { onMounted, ref, type Ref } from 'vue'
import { useRoute } from 'vue-router'

const game: Ref<Game | undefined> = ref(undefined)
const route = useRoute()
const gameId = Number(route.params.gameId)

onMounted(async () => {
  game.value = await loadGame(gameId)
})
</script>
