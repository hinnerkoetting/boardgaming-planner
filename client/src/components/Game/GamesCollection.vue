<template>
  <div>
    <DataView :value="games">
      <template #list="slotProps">
        <div class="grid">
          <template v-for="(item, index) in slotProps.items" :key="index">
            <div class="grid-card">
              <GameComponent
                :game="item"
                @game-rating-selected="onClickRate"
                v-on:game="onClickRate"
              />
            </div>
          </template>
        </div>
      </template>
    </DataView>

    <Dialog v-model:visible="ratingWindowVisible" modal :header="selectedGame?.name">
      <RatingComponent
        :game="selectedGame!"
        :game-group-id="gameGroupId"
        @game-rated="onGameRated"
        @game-rating-deleted="onGameRatingDeleted"
      />
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import type { RatedGame } from '@/model/RatedGame'
import type { Rating } from '@/model/Rating'
import Dialog from 'primevue/dialog'
import { ref, type Ref } from 'vue'
import RatingComponent from '../RatingComponent.vue'
import DataView from 'primevue/dataview'
import GameComponent from './GameComponent.vue'

const props = defineProps({
  games: {
    required: true
  },
  gameGroupId: {
    type: Number,
    required: true
  }
})

const games: Ref<RatedGame[]> = ref(props.games as RatedGame[])
const gameGroupId = ref(props.gameGroupId)
const ratingWindowVisible = ref(false)
const selectedGame: Ref<RatedGame | undefined> = ref()

async function onClickRate(game: RatedGame) {
  console.log('bla')
  selectedGame.value = game
  ratingWindowVisible.value = true
}

async function onGameRatingDeleted(rating: Rating) {
  selectedGame.value!.rating = rating
  ratingWindowVisible.value = false
}

function onGameRated(rating: Rating) {
  selectedGame.value!.rating = rating
  ratingWindowVisible.value = false
}
</script>

<style lang="css" scoped>
.grid {
  display: flex;
  justify-content: space-between;  
  flex-wrap: wrap;
  gap: 16px;
}

.grid-card {
  flex: 0 0 250px;
  margin-bottom: auto;
  align-self: stretch;  
}

@media (max-width: 500px) {
  .grid {
    display: block;
  }
  .grid-card {
    flex: 0 0 100%;  
  }
}
</style>
