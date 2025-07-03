<script setup lang="ts">
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { ref, type Ref } from 'vue'
import type { GameGroup } from '@/model/GameGroup'
import { addGameGroup } from '@/services/ApiService'

const emit = defineEmits(['game-group-added'])

const name: Ref<String> = ref('')

function onClickAddGameGroup() {
  const gameGroup: GameGroup = {
    name: name.value,
    id: undefined
  }
  addGameGroup(gameGroup)
  emit('game-group-added', gameGroup)
}
</script>

<template>
  <div>
    <InputText name="name" v-model="name" placeholder="Name"></InputText><br />
    <Button @click="onClickAddGameGroup">Add</Button>
  </div>
</template>

<style scoped></style>
