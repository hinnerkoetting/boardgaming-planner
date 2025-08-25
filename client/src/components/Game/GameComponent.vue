<template>
  <div class="full-height">
    <Card @click="onClickCard">
      <template #title>
        <div class="title-wrapper">
          <div class="title">
            {{ game.name }}
          </div>
          <span v-if="isNew" class="pi pi-star-fill" style="font-size: 1rem; color: gold; margin-left: 4px"
            title="NEW"></span>
        </div>
      </template>
      <template #content>
        <div class="content">
          <Image :src="game.thumbnailUrl" />
          <div>
            <div v-if="game.rating?.existsVeto" class="center-horizontally">
              <Tag severity="danger">Vetoed</Tag><br />
            </div>
            <div v-if="!!game.rating?.averageRating || !!game.rating?.myRating" class="center-horizontally rating">
              &nbsp; &Oslash; rating
              <template v-if="game.rating?.averageRating">
                <span v-for="n in numberOfStars(game.rating?.averageRating)" :key="n" class="pi pi-star-fill"
                  style="color: gold; font-size: 1rem"></span>
                <span v-for="n in missingStars(numberOfStars(game.rating?.averageRating))" :key="n" class="pi pi-star"
                  style="color: black; font-size: 1rem"></span>
                ({{
                  game.rating?.numberOfVotes || '0' }} votes)
              </template>
              <br />

              My rating
              <template v-if="game.rating?.myRating">
                <span v-for="n in numberOfStars(game.rating?.myRating)" :key="n" class="pi pi-star-fill"
                  style="color: gold; font-size: 1rem"></span>
                <span v-for="n in missingStars(numberOfStars(game.rating?.myRating))" :key="n" class="pi pi-star"
                  style="color: black; font-size: 1rem"></span>
              </template>
              <span v-else class="missing-rating">
                Not rated yet
              </span>
              <br />


            </div>
            <div v-if="!game.rating?.averageRating && !game.rating?.myRating" class="center-horizontally">
              Not rated yet
            </div>
            <div class="center-horizontally buttons" style="margin-top: 8px">
              <Button style="float: left" v-if="withRateButton" severity="secondary"
                @click.stop="$emit('game-rating-selected', game)">Rate</Button>

              <!-- Prevent clicks from the splitbutton to be propaged to the card. -->
              <span @click.stop="">
                <SplitButton :model="menuItems(game)" @click="$emit('game-tag-selected', game)" label="Tag"
                  severity="secondary" />
              </span>

            </div>
          </div>
          <!-- <div class="center-horizontally" style="margin-top: 8px;">
              <PlayerTagsListComponent :player-tags="game.tags.player" :players="players"/>
            </div> -->
        </div>
      </template>
    </Card>
    <Dialog v-model:visible="showGameDialog" modal :header="game.name" style="width: 100%">
      <ShowGameDetailsComponent :game="game" />
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import Button from 'primevue/button'
import Card from 'primevue/card'
import Image from 'primevue/image'
import Tag from 'primevue/tag'
import { ref, watch, type PropType } from 'vue'
import ShowGameDetailsComponent from './ShowGameDetailsComponent.vue'
import Dialog from 'primevue/dialog'
import type { RatedGame } from '@/model/Game'
import 'primeicons/primeicons.css'
import { amIGroupAdminOrOwner } from '@/services/GameGroupService'
import type { GameGroupMember } from '@/model/GameGroupMember'
import { getCurrentPlayerId } from '@/services/LoginService'
import { SplitButton } from 'primevue'
import type { MenuItem, MenuItemCommandEvent } from 'primevue/menuitem'

const props = defineProps({
  game: {
    type: Object as PropType<RatedGame>,
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
    type: Array as PropType<GameGroupMember[]>,
    required: true
  },
  isNew: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits<{
  (e: 'game-rating-selected', game: RatedGame): void
  (e: 'game-tag-selected', game: RatedGame): void
  (e: 'game-delete-selected', game: RatedGame): void
}>()

const game = ref(props.game)
const showGameDialog = ref(false)

watch(
  () => props.game,
  (newGameValue: RatedGame) => {
    game.value = newGameValue
  }
)

function onClickCard() {
  showGameDialog.value = true
}

function showDeleteButton(): boolean {
  const myPlayer = getMyPlayer()
  return myPlayer && amIGroupAdminOrOwner(myPlayer) || false
}

function getMyPlayer(): GameGroupMember | undefined {
  return props.players.filter((p) => p.id === getCurrentPlayerId())[0]
}

function menuItems(game: RatedGame): MenuItem[] {
  return [

    {
      label: 'Delete',
      command: () => {
        emit('game-delete-selected', game)
      },
      visible: showDeleteButton(),
    },
  ]
};

function numberOfStars(rating: number): number {
  if (rating === 0) {
    return 0
  }
  if (rating <= 1) {
    return 1
  }
  if (rating <= 4) {
    return 2
  }
  if (rating <= 7) {
    return 3
  }
  if (rating <= 9) {
    return 4
  }
  return 5
}

function missingStars(stars: number): number[] {
  return Array.from(Array(5 - stars).keys()).map((number) => number + stars)
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
  max-width: 220px;
  text-align: left;
}

.p-dialog {
  --p-dialog-background: var(--color-background);
}

.title-wrapper {
  display: flex;
  align-items: center;
}

.buttons {
  display: flex;
  justify-content: space-between;
  gap: 8px;
  width: 100%;
}

.missing-rating {
  color: var(--highlight-text-color);
  font-weight: 800;
}
</style>
