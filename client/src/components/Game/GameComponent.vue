<template>
  <div class="full-height">
    <Card>
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
              {{ game.rating?.averageRating || '?' }} / {{ game.rating?.myRating || '?' }}
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
              @click="$emit('game', game)"
              class="center-horizontally"
              >Rate</Button
            >
          </div>
        </div>
      </template>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { GameGroupGame } from '@/model/Game'
import Button from 'primevue/button'
import Card from 'primevue/card'
import Image from 'primevue/image'
import Tag from 'primevue/tag'
import { ref } from 'vue'

const props = defineProps({
  game: {
    type: GameGroupGame,
    required: true
  },
  withRateButton: {
    type: Boolean,
    default: true
  }
})

defineEmits<{
  (e: 'game', game: GameGroupGame): void
}>()

const game = ref(props.game)
</script>

<style scoped>
.p-card {
  background-color: #fafafa;
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
</style>
