<template>
  <div>
    <DataTable :value="games" tableStyle="min-width: 20rem">
      <Column field="name" header="Name"></Column>
      <Column header="Rating (Average / Mine)">
        <template #body="slotProps">
          <div v-if="slotProps.data.rating?.existsVeto">
            <Tag severity="danger">Vetoed</Tag>
            {{ slotProps.data.rating?.averageRating }} / {{ slotProps.data.rating?.myRating }}
          </div>
          <div v-if="!slotProps.data.rating?.existsVeto">
            {{ slotProps.data.rating?.averageRating }} / {{ slotProps.data.rating?.myRating }}
          </div>
        </template>
      </Column>
      <Column field="thumbnailUrl" header="Thumbnail">
        <template #body="slotProps">
          <Image :src="slotProps.data.thumbnailUrl" />
        </template>
      </Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button severity="secondary" @click="onClickRate(slotProps.data)">Rate</Button>
        </template>
      </Column>
    </DataTable>
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
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import Image from 'primevue/image'
import Tag from 'primevue/tag'

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
