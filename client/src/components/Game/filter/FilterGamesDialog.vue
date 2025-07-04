<template>
  <div class="wrapper">
    <Button @click="onClickShowFilter">{{ filterLabel() }}</Button>
    <Dialog v-model:visible="showDialog" modal :header="`Filter games (${numberOfVisibleGames})`">
      <FilterGamesComponent
        :allGames="allGames"
        :allTags="allTags"
        :numberOfPlayersInGroup="numberOfPlayersInGroup"
        :allPlayers="allPlayers"
        @updated-filter="onFilterUpdated"
        @close="onClickClose"
      />
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import type { RatedGame } from '@/model/Game'
import type { TagModel } from '@/model/TagModel'
import FilterGamesComponent from './FilterGamesComponent.vue'
import { ref, watch, type PropType } from 'vue'
import Button from 'primevue/button'
import Dialog from 'primevue/dialog'
import type { Player } from '@/model/Player/Player'

const props = defineProps({
  allTags: {
    type: Array as PropType<TagModel[]>,
    required: true
  },
  allGames: {
    type: Array as PropType<RatedGame[]>,
    required: true
  },
  visibleGames: {
    type: Array as PropType<RatedGame[]>,
    required: true
  },
  numberOfPlayersInGroup: {
    type: Number
  },
  allPlayers: {
    type: Array as PropType<Player[]>,
    required: true
  }
})

const numberOfVisibleGames = ref()

const emit = defineEmits<{
  (e: 'updated-filter', visibleGames: RatedGame[]): void,
  (e: 'opened'): void
}>()

watch(
  () => props.visibleGames,
  (games: RatedGame[]) => {
    numberOfVisibleGames.value = games.length
  }
)

const showDialog = ref(false)

function onClickShowFilter() {
  showDialog.value = true
  emit('opened')
}

function onFilterUpdated(games: RatedGame[]) {
  emit('updated-filter', games)
}

function onClickClose() {
  showDialog.value = false
}

function filterLabel(): string {
  const numberOfVisibleGames = props.visibleGames.length;
  const numberOfAllGames = props.allGames.length;
  if (numberOfAllGames === numberOfVisibleGames) {
    return 'Filter';
  }
  
  return `Filter ${numberOfVisibleGames} / ${numberOfAllGames}`;
}
</script>

<style lang="css" scoped>
.wrapper {
  display: inline;
}
</style>
