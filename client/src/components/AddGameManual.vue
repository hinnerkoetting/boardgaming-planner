<script setup lang="ts">
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { ref, type Ref } from 'vue'
import { addGame } from '@/services/ApiService'
import type { Game } from '@/model/Game'

const emit = defineEmits(['game-added'])

const name: Ref<String> = ref('')
const description: Ref<String> = ref('')
const imageUrl: Ref<String> = ref('')
const thumbnailUrl: Ref<String> = ref('')

function onClickAddGame() {
  const game: Game = {
    name: name.value,
    description: description.value,
    imageUrl: imageUrl.value,
    thumbnailUrl: thumbnailUrl.value,
    id: undefined
  }
  addGame(game)
  emit('game-added', game)
}
</script>

<template>
  <div>
    <InputText name="name" v-model="name" placeholder="Name"></InputText><br />
    <InputText name="description" v-model="description" placeholder="Description"></InputText><br />
    <InputText name="imageUrl" v-model="imageUrl" placeholder="imageUrl"></InputText><br />
    <InputText name="thumbnailUrl" v-model="thumbnailUrl" placeholder="Thumbnail"></InputText><br />
    <Button @click="onClickAddGame">Add</Button>
  </div>
</template>

<style scoped></style>
