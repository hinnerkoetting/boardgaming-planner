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
    required: true
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
  }
})


const game = ref(props.game)
const showGameDialog = ref(false)


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
</style>
