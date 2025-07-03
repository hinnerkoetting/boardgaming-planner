<template>
  <div class="content">
    <div v-for="tag in tagsSelection" :key="tag.id">
      <Button :severity="tag.selected ? 'primary' : 'secondary'" @click="onClickToggle(tag)">{{
        tag.description
      }}</Button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { AdminGame } from '@/model/Game'
import { TagModel } from '@/model/TagModel'
import { addTagToGame, removeTagFromGame } from '@/services/api/GameApiService'
import Button from 'primevue/button'
import { onMounted, ref, type PropType, type Ref } from 'vue'

class TagSelection {
  constructor(
    readonly description: string,
    readonly id: number,
    public selected: boolean
  ) {}
}

const props = defineProps({
  allTags: {
    type: Array as PropType<TagModel[]>,
    required: true
  },
  gameTags: {
    type: Array as PropType<TagModel[]>,
    required: true
  },
  game: {
    type: AdminGame,
    required: true
  }
})

const tagsSelection: Ref<TagSelection[]> = ref([])

onMounted(async () => {
  tagsSelection.value = props.allTags
    .filter((tag) => tag.type === 'GLOBAL')
    .map((tag) => {
      const selected = !!props.gameTags.find((activeGameTag) => activeGameTag.id == tag.id)
      return new TagSelection(tag.description, tag.id, selected)
    })
})

async function onClickToggle(tag: TagSelection) {
  // TODO this is not published correctly to the parent, it will not see the update immediately
  if (tag.selected) {
    const response = await removeTagFromGame(props.game, tag.id)
    if (response.success) {
      tag.selected = false
    }
  } else {
    const response = await addTagToGame(props.game, tag.id)
    if (response.success) {
      tag.selected = true
    }
  }
}
</script>

<style lang="css" scoped>
.content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
</style>
