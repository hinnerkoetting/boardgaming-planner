<script setup lang="ts">
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { ref, type Ref } from 'vue'
import { GameGroup } from '@/model/GameGroup'
import { addGameGroup } from '@/services/api/GameGroupApiService'
import Message from 'primevue/message'

const emit = defineEmits<{
  (e: 'game-group-added', gameGroup: GameGroup): void
}>()

const name: Ref<string> = ref('')
const errorMessage = ref()

async function onClickAddGameGroup() {
  const gameGroup: GameGroup = {
    name: name.value,
    id: undefined
  }
  const response = await addGameGroup(gameGroup)
  if (response.success) {
    emit('game-group-added', response.success)
  } else {
    errorMessage.value = response.error?.detail || 'Error'
  }
}
</script>

<template>
  <div>
    <Message severity="error" v-if="errorMessage"> {{ errorMessage }} </Message>
    <InputText
      name="name"
      v-model="name"
      placeholder="Name"
      v-on:keyup.enter="onClickAddGameGroup"
    ></InputText
    ><br />
    <Button @click="onClickAddGameGroup" class="createButton">Create</Button>
  </div>
</template>

<style scoped>
.createButton {
  margin-top: 4px;
}
</style>
