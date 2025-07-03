<template>
  <div>
    <div v-for="tag in tags" :key="tag.id" class="one-filter">
      <Button
        :severity="tag.selected === 'FILTER_WITH' ? 'primary' : 'secondary'"
        @click="onClickFilterWith(tag)"
        class="filterButton"
        >{{ tag.description }}</Button
      >
      <Button
        :severity="tag.selected === 'FILTER_WITHOUT' ? 'primary' : 'secondary'"
        @click="onClickFilterWithout(tag)"
        class="filterButton"
        >No {{ tag.description }}</Button
      >
    </div>
  </div>
</template>

<script setup lang="ts">
import type { GameGroupGame } from '@/model/Game'
import { TagModel } from '@/model/TagModel'
import { FilterService, TagSelection } from '@/services/FilterService'
import Button from 'primevue/button'
import { ref, watch, type PropType, type Ref } from 'vue'

const props = defineProps({
  allTags: {
    type: Array as PropType<TagModel[]>,
    required: true
  },
  allGames: {
    type: Array as PropType<GameGroupGame[]>,
    required: true
  }
})

const emit = defineEmits<{
  (e: 'updated-filter', visibleGames: GameGroupGame[]): void
}>()

const tags: Ref<TagSelection[]> = ref(createTagSelection(props.allTags))
const filterService = new FilterService()

watch(
  () => props.allTags,
  (allTags: TagModel[]) => {
    tags.value = createTagSelection(allTags)
  }
)

function createTagSelection(allTags: TagModel[]): TagSelection[] {
  return allTags.map((tag) => new TagSelection(tag.description, tag.id, 'DO_NOT_FILTER'))
}

async function onClickFilterWith(tag: TagSelection) {
  if (tag.selected === 'FILTER_WITH') {
    tag.selected = 'DO_NOT_FILTER'
  } else {
    tag.selected = 'FILTER_WITH'
  }
  emitFilterUpdate()
}

async function onClickFilterWithout(tag: TagSelection) {
  if (tag.selected === 'FILTER_WITHOUT') {
    tag.selected = 'DO_NOT_FILTER'
  } else {
    tag.selected = 'FILTER_WITHOUT'
  }
  emitFilterUpdate()
}

function emitFilterUpdate() {
  const filteredGames = filterService.filterGames(props.allGames, tags.value)
  emit('updated-filter', filteredGames)
}
</script>

<style lang="css" scoped>
.one-filter {
  display: flex;
  gap: 4px;
  flex-grow: 1;
  flex-shrink: 1;
  flex-basis: 0;
  margin: 4px;
}

.filterButton {
  flex: 1;
  width: 150px;
}
</style>
