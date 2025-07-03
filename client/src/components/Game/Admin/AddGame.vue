<script setup lang="ts">
import AddGameBgg from './AddGameBgg.vue'
import AddGameManual from './Admin/AddGameManual.vue'

const emit = defineEmits(['game-added'])

function onGameAdded(params: any) {
  emit('game-added', params)
}
</script>

<template>
  <div>
    <h1 class="green">Add new game</h1>

    <h2 class="green">Add via Boardgamegeek</h2>
    <AddGameBgg @game-added="onGameAdded" />

    <h2 class="green">Add game manually</h2>
    <AddGameManual @game-added="onGameAdded" />
  </div>
</template>

<style scoped></style>
