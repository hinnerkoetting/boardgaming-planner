<script setup lang="ts">
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { ref, type PropType, type Ref } from 'vue'
import { Game } from '@/model/Game'
import { addGame, updateGame } from '@/services/api/GameApiService'
import Message from 'primevue/message'
import type { ResponseWrapper } from '@/model/api/Response'

const emit = defineEmits(['game-added'])
const props = defineProps({
  mode: {
    type: String as PropType<'CREATE' | 'EDIT'>,
    required: true
  },
  game: {
    type: Game
  }
})

const name: Ref<string> = ref(props.game?.name || '')
const description: Ref<string> = ref(props.game?.description || '')
const imageUrl: Ref<string> = ref(props.game?.imageUrl || '')
const thumbnailUrl: Ref<string> = ref(props.game?.thumbnailUrl || '')
const errorMessage = ref('')

async function onClickAddGame() {
  const response = await createOrUpdateGame()
  if (response.success) {
    emit('game-added', response.success)
  } else {
    errorMessage.value = response.error?.detail || 'Error'
  }
}

async function createOrUpdateGame(): Promise<ResponseWrapper<Game>> {
  const game: Game = {
    name: name.value,
    description: description.value,
    imageUrl: imageUrl.value,
    thumbnailUrl: thumbnailUrl.value,
    id: props.game?.id || undefined,
    globalTags: props.game?.globalTags || []
  }
  if (props.mode === 'CREATE') {
    return await addGame(game)
  } else {
    const response = await updateGame(game)
    if (response.success) {
      return {
        success: game,
        error: undefined
      }
    } else {
      return {
        success: undefined,
        error: response.error
      }
    }
  }
}
</script>

<template>
  <div class="wrapper">
    <Message severity="error" v-if="errorMessage"> {{ errorMessage }} </Message>
    <InputText name="name" v-model="name" placeholder="Name"></InputText><br />
    <InputText name="description" v-model="description" placeholder="Description"></InputText><br />
    <InputText name="imageUrl" v-model="imageUrl" placeholder="imageUrl"></InputText><br />
    <InputText name="thumbnailUrl" v-model="thumbnailUrl" placeholder="Thumbnail"></InputText><br />
    <Button @click="onClickAddGame">Save</Button>
  </div>
</template>

<style scoped>
.wrapper {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
</style>
