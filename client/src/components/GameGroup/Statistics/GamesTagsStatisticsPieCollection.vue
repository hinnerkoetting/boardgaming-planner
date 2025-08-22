<script setup lang="ts">
import { RatedGame } from '@/model/Game'
import { onMounted, ref, useTemplateRef, type PropType, type Ref } from 'vue'
import { loadTags } from '../../../services/StoreApiService'
import GamesTagsStatisticsPie from './GamesTagsStatisticsPie.vue'

const tags: Ref<TagAggregation[]> = ref([])

const props = defineProps({
  games: {
    type: Array as PropType<RatedGame[]>,
    required: true
  }
})

class TagAggregation {
  constructor(public tagId: number, public name: string, public gamesWithTag: number, public gamesWithoutTag: number) {
  }
}

async function groupGamesByTag(games: RatedGame[]): Promise<TagAggregation[]> {
  const allTags = await loadTags()

  const tagsCounts: Record<string, TagAggregation> = {}
  allTags.forEach(tag => {
    tagsCounts[tag.description] = new TagAggregation(tag.id, tag.description, 0, 0)
  })


  games.forEach(game => {
    Object.values(tagsCounts).forEach((tagCount) => {
      if (game.tags.global.some(tag => tag.id === tagCount.tagId) || game.tags.group.some(tag => tag.id === tagCount.tagId) || game.tags.player.some(tag => tag.id === tagCount.tagId)) {
        tagCount.gamesWithTag += 1
      }

    })

  })
  const gamesCount = games.length
  Object.values(tagsCounts).forEach((tagCount) => {
    tagCount.gamesWithoutTag = gamesCount - tagCount.gamesWithTag
  })
  return Object.values(tagsCounts)
}

onMounted(async () => {
  tags.value = await groupGamesByTag(props.games)
})

</script>

<template>
  <h2>Games by tag</h2>
  <div style="display: flex; flex-wrap: wrap; gap: 16px;">
    <GamesTagsStatisticsPie :tag-aggregation="tag" v-for="tag in tags" />
  </div>

</template>

<style lang="css" scoped>
h2 {
  text-align: center;
  font-weight: 600;
}
</style>
