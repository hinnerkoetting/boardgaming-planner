<template>
  <div>
    <DataView :value="games" class="gamesGrid" dataKey="id">
      <template #list="slotProps">
        <div class="grid">
          <template v-for="(item, index) in slotProps.items" :key="index">
            <div class="grid-card">
              <GameComponent :game="item" :game-group-id="gameGroupId" @game-rating-selected="onClickRate"
                @game-tag-selected="onClickTag" :withRateButton="withRateButton" :withTagButton="withTagButton"
                :players="players" :isNew="lastVisitedGameGroup < item.addedToGameGroupDate"
                @game-delete-selected="onClickRemoveGame" />
            </div>
          </template>
        </div>
      </template>
    </DataView>

    <Dialog v-model:visible="ratingWindowVisible" modal header="Rating">
      <RatingComponent :game="selectedGame!" :game-group-id="gameGroupId" @game-rated="onGameRated"
        @game-rating-deleted="onGameRatingDeleted" />
    </Dialog>
    <Dialog v-model:visible="tagWindowVisible" modal :header="selectedGame?.name">
      <TagGameInGroupComponent :game="selectedGame!" :game-group-id="gameGroupId" @close="tagWindowVisible = false" />
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import type { Rating } from '@/model/Rating'
import Dialog from 'primevue/dialog'
import { ref, watch, type PropType, type Ref } from 'vue'
import RatingComponent from '../GameGroup/RatingComponent.vue'
import DataView from 'primevue/dataview'
import GameComponent from './GameComponent.vue'
import type { RatedGame } from '@/model/Game'
import TagGameInGroupComponent from '../GameGroup/tags/TagGameInGroupComponent.vue'
import type { GameGroupMember } from '@/model/GameGroupMember'
import { removeGameFromGroup } from '@/services/api/GameGroupApiService'

const props = defineProps({
  games: {
    type: Array as PropType<RatedGame[]>,
    required: true
  },
  gameGroupId: {
    type: Number,
    required: true
  },
  withRateButton: {
    type: Boolean,
    required: true,
    default: true
  },
  withTagButton: {
    type: Boolean,
    required: true,
    default: true
  },
  players: {
    type: Array as PropType<GameGroupMember[]>,
    required: true
  },
  lastVisitedGameGroup: {
    type: Number,
    required: true
  }
})

const emit = defineEmits<{
  (e: 'ratingOpened'): void
  (e: 'tagOpened'): void
  (e: 'gameRated'): void
  (e: 'gameRemoved', gameId: number): void
}>()

const games: Ref<RatedGame[]> = ref(props.games)
const gameGroupId = ref(props.gameGroupId)
const ratingWindowVisible = ref(false)
const tagWindowVisible = ref(false)
const selectedGame: Ref<RatedGame | undefined> = ref()

watch(
  () => props.games,
  (newGames: RatedGame[]) => {
    games.value = newGames
  }
)

async function onClickRate(game: RatedGame) {
  selectedGame.value = game
  ratingWindowVisible.value = true
  emit('ratingOpened')
}

async function onClickTag(game: RatedGame) {
  selectedGame.value = game
  tagWindowVisible.value = true
  emit('tagOpened')
}

async function onGameRatingDeleted(rating: Rating) {
  selectedGame.value!.rating = rating
  ratingWindowVisible.value = false
}

function onGameRated(rating: Rating) {
  selectedGame.value!.rating = rating
  ratingWindowVisible.value = false
  emit('gameRated')
}

async function onClickRemoveGame(game: RatedGame) {
  await removeGameFromGroup(gameGroupId.value, game.id!)
  emit('gameRemoved', game.id!)
}

</script>

<style lang="css" scoped>
.gamesGrid {
  --p-dataview-content-background: var(--color-background);
}

.grid {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 16px;
}

.grid-card {
  flex: 1 1 0px;
  margin-bottom: auto;
  align-self: stretch;
  margin-top: 4px;
  margin-bottom: 0px;
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
