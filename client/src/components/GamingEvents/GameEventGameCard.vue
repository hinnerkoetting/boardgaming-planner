<template>
  <div class="full-height">
    <Card @click="onClickCard">
      <template #title>
        <div class="title">
          {{ game.name }}  <span :class="gameStatusClass(gameStatus)"> </span>         
        </div>
      </template>
      <template #content>
        <div class="content">
          <Image :src="game.thumbnailUrl" />
          {{  gameComment }}
        </div>
        <span class="gamestatus-buttons" v-if="isPartOfGroup">
          <Message v-if="errorMessage" severity="error">{{ errorMessage }}</Message>
          <Button :disabled="gameStatus == 'PLAYED'" 
                  label="Played"                 
                  @click.stop="onPlayed(game.id!)" />
          <Button :disabled="gameStatus == 'REJECTED'" 
                  label="Rejected" 
                  severity="warn"
                  @click.stop="onReject(game.id!)" />
          <Button :disabled="gameStatus == 'SUGGESTED'" 
                  label="Suggested" 
                  severity="secondary"
                  @click.stop="onSuggest(game.id!)" />     
          <Button
            label="Delete" 
            severity="danger"
            @click.stop="onDelete(game.id!)" />     
        </span>
        <span v-else>
          {{ gameStatusName(gameStatus)}}
        </span>
      </template>
    </Card>
    <Dialog
      v-model:visible="showGameDialog"
      modal
      :header="game.name"
      style="width: 100%"
    >
      <ShowGameDetailsComponent :game="game" />
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import Card from 'primevue/card'
import Image from 'primevue/image'
import { ref, type PropType } from 'vue'
import Dialog from 'primevue/dialog'
import type { Player } from '@/model/Player/Player'
import type { Game } from '@/model/Game'
import ShowGameDetailsComponent from '../Game/ShowGameDetailsComponent.vue'
import type { GameStatus } from '@/model/GamingEvent'
import 'primeicons/primeicons.css'
import { removeGameFromEvent, updateGameStatus } from '@/services/api/GamingEventsApiService'
import { Button, Message } from 'primevue'

const props = defineProps({
  game: {
    type: Object as PropType<Game>,
    required: true
  },
  gameStatus: {
    type: String as PropType<GameStatus>,
    required: true
  },
  gameComment: {
    type: String as PropType<string | null>,
    required: false
  },
  withRateButton: {
    type: Boolean,
    default: true
  },
  withTagButton: {
    type: Boolean,
    default: true
  },
  players: {
    type: Array as PropType<Player[]>,
    required: true
  },
  gamingEventId: {
    type: Number,
    required: true
  },
  isPartOfGroup: {
    type: Boolean,
    required: true
  }
})

const emits = defineEmits<{
  (e: 'game-removed', game: Game): void
}>()

const game = ref(props.game)
const gameStatus = ref(props.gameStatus)
const showGameDialog = ref(false)
const errorMessage = ref('')

function onClickCard() {
  showGameDialog.value = true
}

function gameStatusClass(gameStatus: GameStatus) {
  switch (gameStatus) {
    case 'SUGGESTED':
      return 'pi pi-lightbulb suggested';
    case 'REJECTED':
      return 'pi pi-times declined';
    case 'PLAYED':
      return 'pi pi-check maybe';
    default:
      return '';
  }
}

function gameStatusName(gameStatus: GameStatus): string {
  switch (gameStatus) {
    case 'SUGGESTED':
      return 'Suggested';
    case 'REJECTED':
      return 'Rejected';
    case 'PLAYED':
      return 'Played';
    default:
      return '';
  }
}

async function onPlayed(gameId: number) {
  const result = await updateGameStatus(props.gamingEventId, gameId, 'PLAYED')
  if (result.success) {
    gameStatus.value = 'PLAYED';    
  } else {
    errorMessage.value = result.error?.detail ?? "Error updating game status";
  }
}

async function onReject(gameId: number) {
  const result = await updateGameStatus(props.gamingEventId, gameId, 'REJECTED')
  if (result.success) {
    gameStatus.value = 'REJECTED';    
  } else {
    errorMessage.value = result.error?.detail ?? "Error updating game status";
  }
}

async function onSuggest(gameId: number) {
  const result = await updateGameStatus(props.gamingEventId, gameId, 'SUGGESTED')
  if (result.success) {
    gameStatus.value = 'SUGGESTED';    
  } else {
    errorMessage.value = result.error?.detail ?? "Error updating game status";
  }
}

async function onDelete(gameId: number) {
  const result = await removeGameFromEvent(props.gamingEventId, gameId)
  if (!result.success) {    
    errorMessage.value = result.error?.detail ?? "Error updating game status";
  } else {
    emits('game-removed', props.game);
  }
}

</script>

<style lang="css" scoped>
.p-card {
  height: 100%;
}

.full-height {
  height: 100%;
  cursor: pointer;
}

.center-horizontally {
  width: 100%;
  margin: 0 auto;
  text-align: center;
}

.title {
  display: block;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  text-align: center;
  max-width: 260px;
}

.content {
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  align-items: center;
}

.fullWidth {
  width: 100%;
}

.rating {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  text-align: center;
  max-width: 200px;
}

.p-dialog {
  --p-dialog-background: var(--color-background);
}

.suggested {
  color: darkgreen;
}

.declined {
  color: red;
}

.played {
  color: green;
}

.gamestatus-buttons > *{
  margin-right: 4px;
}
</style>
