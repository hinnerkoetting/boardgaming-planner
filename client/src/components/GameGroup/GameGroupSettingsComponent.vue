<template>
  <div class="content">
    <div>
      Name
      <InputText v-model="name" />
    </div>
    <div>
      Type
      <Select v-model="type"
        :options="[{ name: GameGroupType.PUBLIC, description: 'Public' }, { name: GameGroupType.PRIVATE, description: 'Private' }]"
        optionLabel="description" optionValue="name" />
    </div>
    <div>
      <Checkbox v-model="openForNewPlayers" binary inputId="openForNewPlayers"></Checkbox>
      <label for="openForNewPlayers">Open for new players</label>
    </div>
    <Button @click="onClickEditGameGroup" class="createButton">Save</Button>
    <Message v-if="errorMessage" severity="error" class="full-width">{{ errorMessage }}</Message>
  </div>
</template>

<script setup lang="ts">
import { GameGroupType, type GameGroup } from '@/model/GameGroup';
import { updateGameGroup } from '@/services/api/GameGroupApiService';
import { Button, Checkbox, InputText, Select } from 'primevue';
import { ref, type PropType } from 'vue';


const props = defineProps({
  gameGroup: {
    type: Object as PropType<GameGroup>,
    required: true
  }
})

const emits = defineEmits<{
  (e: 'game-group-updated', gameGroup: GameGroup): void
}>()

const name = ref(props.gameGroup.name)
const type = ref(props.gameGroup.type)
const openForNewPlayers = ref(props.gameGroup.openForNewPlayers)
const errorMessage = ref<string>('')

async function onClickEditGameGroup() {
  const result = await updateGameGroup({
    id: props.gameGroup.id,
    name: name.value,
    type: type.value as GameGroupType,
    openForNewPlayers: openForNewPlayers.value
  })
  if (result.success) {
    emits('game-group-updated', result.success)
  } else {
    errorMessage.value = 'Error updating game group:', result.error?.detail || 'Unknown error';
  }
}
</script>

<style lang="css" scoped>
.content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
</style>
