<template>
  <div class="full-height">
    <Card @click="onClickCard">
      <template #title>
        <div class="title">
          {{ game.name }}
        </div>
      </template>
      <template #content>
        <div class="content">
          <Image :src="game.thumbnailUrl" />
          <div>
            <div v-if="game.rating?.existsVeto" class="center-horizontally">
              <Tag severity="danger">Vetoed</Tag><br />
            </div>
            <div
              v-if="!!game.rating?.averageRating || !!game.rating?.myRating"
              class="center-horizontally rating"
            >
              Rating: &Oslash; {{ game.rating?.averageRating || '?' }} &#183; Me
              {{ game.rating?.myRating || '?' }} &#183;
              {{ game.rating?.numberOfVotes || '0' }} votes
            </div>
            <div
              v-if="!game.rating?.averageRating && !game.rating?.myRating"
              class="center-horizontally">
              Not rated yet
            </div>
            <div class="center-horizontally" style="margin-top: 8px;">
              <Button
                style="float: left"
                v-if="withRateButton"
                severity="secondary"
                @click.stop="$emit('game-rating-selected', game)"                
                >Rate</Button>
                
              <Button
                style="float: right"
                v-if="withTagButton"
                severity="secondary"
                @click.stop="$emit('game-tag-selected', game)"                             
                >Update</Button>
            </div>
          </div>
          <!-- <div class="center-horizontally" style="margin-top: 8px;">
              <PlayerTagsListComponent :player-tags="game.tags.player" :players="players"/>
            </div> -->
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
import { GameGroupGame } from '@/model/Game'
import Button from 'primevue/button'
import Card from 'primevue/card'
import Image from 'primevue/image'
import Tag from 'primevue/tag'
import { ref, watch, type PropType } from 'vue'
import ShowGameDetailsComponent from './ShowGameDetailsComponent.vue'
import Dialog from 'primevue/dialog'
import PlayerTagsListComponent from '../GameGroup/tags/PlayerTagsListComponent.vue'
import type { Player } from '@/model/Player/Player'

const props = defineProps({
  game: {
    type: Object as PropType<GameGroupGame>,
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

defineEmits<{
  (e: 'game-rating-selected', game: GameGroupGame): void
  (e: 'game-tag-selected', game: GameGroupGame): void
}>()

const game = ref(props.game)
const showGameDialog = ref(false)

watch(
  () => props.game,
  (newGameValue: GameGroupGame) => {
    game.value = newGameValue
  }
)


function onClickCard() {
  showGameDialog.value = true
}
</script>

<style lang="css" scoped>
.p-card {
  height: 100%;
}

.full-height {
  height: 100%;
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
</style>
