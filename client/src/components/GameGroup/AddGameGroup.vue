<script setup lang="ts">
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { ref, type Ref } from 'vue'
import type { GameGroup } from '@/model/GameGroup'
import { addGameGroup } from '@/services/api/ApiService'

const emit = defineEmits(['game-group-added'])

const name: Ref<string> = ref('')

async function onClickAddGameGroup() {
  const gameGroup: GameGroup = {
    name: name.value,
    id: undefined
  }
  const savedGameGroup = await addGameGroup(gameGroup)
  emit('game-group-added', savedGameGroup)
}
</script>

<template>
  <div>
    <InputText
      name="name"
      v-model="name"
      placeholder="Name"
      v-on:keyup.enter="onClickAddGameGroup"
    ></InputText
    ><br />
    <Button @click="onClickAddGameGroup">Create</Button>
  </div>
</template>

<style scoped></style>
