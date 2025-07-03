<template>
  <div class="full-height">
    <Card>
      <template #title>
        {{ game.name }}
      </template>
      <template #content>
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
          <Button severity="secondary" @click="$emit('game', game)" class="center-horizontally"
            >Rate</Button
          >
        </div>
      </template>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { RatedGame } from '@/model/RatedGame'
import Button from 'primevue/button'
import Card from 'primevue/card'
import Image from 'primevue/image'
import Tag from 'primevue/tag'
import { ref } from 'vue'

const props = defineProps({
  game: {
    type: RatedGame,
    required: true
  }
})

defineEmits<{
  (e: 'game', game: RatedGame): void
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
</style>
