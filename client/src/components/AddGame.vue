<script setup lang="ts">
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { ref, type Ref } from 'vue'
import { addGame } from '@/services/ApiService'
import AddGameBgg from './AddGameBgg.vue'
import AddGameManual from './AddGameManual.vue'

const emit = defineEmits(['game-added'])

function onGameAdded(params: any) {
  emit('game-added', params)
}
</script>

<template>
  <div>
    <h1 class="green">Add game</h1>

    <h2 class="green">Search via Boardgamegeek</h2>
    <AddGameBgg @game-added="onGameAdded" />

    <h2 class="green">Manual</h2>
    <AddGameManual @game-added="onGameAdded" />
  </div>
</template>

<style scoped></style>
