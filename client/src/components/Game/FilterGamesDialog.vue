<template>
  <div class="wrapper">
    <Button @click="onClickShowFilter">Filter</Button>
    <Dialog v-model:visible="showDialog" modal :header="`Filter games (${numberOfVisibleGames})`">
      <FilterGamesComponent
        :allGames="allGames"
        :allTags="allTags"
        :numberOfPlayersInGroup="numberOfPlayersInGroup"
        @updated-filter="onFilterUpdated"
      />
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import type { GameGroupGame } from '@/model/Game'
import type { TagModel } from '@/model/TagModel'
import FilterGamesComponent from './FilterGamesComponent.vue'
import { ref, watch, type PropType } from 'vue'
import Button from 'primevue/button'
import Dialog from 'primevue/dialog'

const props = defineProps({
  allTags: {
    type: Array as PropType<TagModel[]>,
    required: true
  },
  allGames: {
    type: Array as PropType<GameGroupGame[]>,
    required: true
  },
  numberOfPlayersInGroup: {
    type: Number
  }
})

const numberOfVisibleGames = ref()

const emit = defineEmits<{
  (e: 'updated-filter', visibleGames: GameGroupGame[]): void
}>()

watch(
  () => props.allGames,
  (games: GameGroupGame[]) => {
    if (!numberOfVisibleGames.value) {
      numberOfVisibleGames.value = games.length
    }
  }
)

const showDialog = ref(false)

function onClickShowFilter() {
  showDialog.value = true
}

function onFilterUpdated(games: GameGroupGame[]) {
  numberOfVisibleGames.value = games.length
  emit('updated-filter', games)
}
</script>

<style lang="css" scoped>
.wrapper {
  display: inline;
}
</style>
