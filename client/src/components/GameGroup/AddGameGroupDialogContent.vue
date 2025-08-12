<script setup lang="ts">
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { ref, type Ref } from 'vue'
import { GameGroup, GameGroupType } from '@/model/GameGroup'
import { addGameGroup } from '@/services/api/GameGroupApiService'
import Message from 'primevue/message'
import { Checkbox, Select } from 'primevue'

const emit = defineEmits<{
  (e: 'game-group-added', gameGroup: GameGroup): void
}>()

const name: Ref<string> = ref('')
const type: Ref<GameGroupType> = ref(GameGroupType.PUBLIC)
const openForNewPlayers: Ref<boolean> = ref(true)
const errorMessage = ref()

async function onClickAddGameGroup() {
  const gameGroup: GameGroup = {
    name: name.value,
    id: undefined,
    type: type.value,
    openForNewPlayers: openForNewPlayers.value
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
  <div class="content">
    <Message severity="error" v-if="errorMessage"> {{ errorMessage }} </Message>
    <InputText name="name" v-model="name" placeholder="Name" v-on:keyup.enter="onClickAddGameGroup" /><br />
    <Select v-model="type"
      :options="[{ name: GameGroupType.PUBLIC, description: 'Public' }, { name: GameGroupType.PRIVATE, description: 'Private' }]"
      optionLabel="description" optionValue="name" />
    <div class="checkbox-label">
      <Checkbox v-model="openForNewPlayers" binary label="Open for new players" inputId="openForNewPlayers" />
      <label for="openForNewPlayers">Open for new players</label>
    </div>
    <Button @click="onClickAddGameGroup" class="createButton">Create</Button>
  </div>
</template>

<style scoped>
.createButton {
  margin-top: 4px;
}

.content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
