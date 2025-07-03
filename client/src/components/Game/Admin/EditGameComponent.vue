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
const url: Ref<string | undefined> = ref(props.game?.url || '')
const minPlayers: Ref<number | undefined> = ref(props.game?.minPlayers)
const maxPlayers: Ref<number | undefined> = ref(props.game?.maxPlayers)
const playingTimeMinutes: Ref<number | undefined> = ref(props.game?.playingTimeMinutes)

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
    minPlayers: minPlayers.value,
    maxPlayers: maxPlayers.value,
    url: url.value,
    playingTimeMinutes: playingTimeMinutes.value
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
    <div class="item">
      Name
      <InputText name="name" v-model="name" placeholder="Name"></InputText>
    </div>
    <div class="item">
      Description
      <InputText name="description" v-model="description" placeholder="Description"></InputText>
    </div>
    <div class="item">
      URL
      <InputText v-model="url" placeholder="URL"></InputText>
    </div>
    <div class="item">
      Min. players
      <InputText v-model="minPlayers" placeholder="Min. players"></InputText>
    </div>
    <div class="item">
      Max. players
      <InputText v-model="maxPlayers" placeholder="Max. players"></InputText>
    </div>
    <div class="item">
      Playing time (minutes)
      <InputText v-model="playingTimeMinutes" placeholder="Playing time (minutes"></InputText>
    </div>
    <div class="item">
      Image
      <InputText name="imageUrl" v-model="imageUrl" placeholder="imageUrl"></InputText>
    </div>
    <div class="item">
      Thumbnail
      <InputText name="thumbnailUrl" v-model="thumbnailUrl" placeholder="Thumbnail"></InputText>
    </div>

    <Button @click="onClickAddGame">Save</Button>
  </div>
</template>

<style scoped>
.wrapper {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex: 0 0 100%;
  width: 400px;
}
</style>
