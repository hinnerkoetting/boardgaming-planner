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
              class="center-horizontally"
            >
              Rating: &Oslash; {{ game.rating?.averageRating || '?' }} &#183; Me
              {{ game.rating?.myRating || '?' }} &#183;
              {{ game.rating?.numberOfVotes || '0' }} votes
            </div>
            <div
              v-if="!game.rating?.averageRating && !game.rating?.myRating"
              class="center-horizontally"
            >
              Not rated yet
            </div>
            <Button
              v-if="withRateButton"
              severity="secondary"
              @click.stop="$emit('game-rating-selected', game)"
              class="center-horizontally"
              >Rate</Button
            >
          </div>
        </div>
      </template>
    </Card>
    <Dialog
      v-model:visible="showGameDialog"
      modal
      :header="game.name"
      class="fullWidth"
      :style="{ width: '100%' }"
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
import { ref, type PropType } from 'vue'
import ShowGameDetailsComponent from './ShowGameDetailsComponent.vue'
import Dialog from 'primevue/dialog'

const props = defineProps({
  game: {
    type: Object as PropType<GameGroupGame>,
    required: true
  },
  withRateButton: {
    type: Boolean,
    default: true
  }
})

defineEmits<{
  (e: 'game-rating-selected', game: GameGroupGame): void
}>()

const game = ref(props.game)
const showGameDialog = ref(false)

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
  max-width: 320px;
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
</style>

<style>
.p-dialog {
  --p-dialog-background: var(--color-background);
}
</style>
