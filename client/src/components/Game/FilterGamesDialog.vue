<template>
  <div>
    <Button @click="onClickShowFilter">Filter</Button>
    <Dialog v-model:visible="showDialog" modal header="Filter games">
      <FilterGamesComponent
        :allGames="allGames"
        :allTags="allTags"
        @updated-filter="$emit('updated-filter', $event)"
      />
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import type { GameGroupGame } from '@/model/Game'
import type { TagModel } from '@/model/TagModel'
import FilterGamesComponent from './FilterGamesComponent.vue'
import { ref, type PropType } from 'vue'
import Button from 'primevue/button'
import Dialog from 'primevue/dialog'

defineProps({
  allTags: {
    type: Array as PropType<TagModel[]>,
    required: true
  },
  allGames: {
    type: Array as PropType<GameGroupGame[]>,
    required: true
  }
})

defineEmits<{
  (e: 'updated-filter', visibleGames: GameGroupGame[]): void
}>()

const showDialog = ref(false)

function onClickShowFilter() {
  showDialog.value = true
}
</script>
