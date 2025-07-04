<template>
  <div>
    <Message v-if="errorMessage" severity="error" class="full-width">{{ errorMessage }}</Message>
    
    <div v-for="tag in tags" :key="tag.id" class="one-filter">
      <Checkbox v-model="tag.selected" binary :inputId="'tag_' + tag.id" @value-change="toggleTagSelection(tag)"></Checkbox>
      <label :for="'tag_' + tag.id">{{ tag.description }}</label>
      
    </div>
    <Button 
      style="margin-top: 16px;"
      @click.stop="$emit('close')" 
      severity="primary">OK</Button>
  </div>
</template>

<script setup lang="ts">
import { TagInGameGroup, type GameGroupGame } from '@/model/Game';
import type { TagModel } from '@/model/TagModel';
import { addTagToGameInGroup, deleteTagFromGameInGroup } from '@/services/api/GameGroupApiService';
import { loadTags } from '@/services/StoreApiService';
import { Button, Checkbox, Message } from 'primevue';
import { onMounted, ref, type PropType, type Ref } from 'vue';

const props = defineProps({
  game: {
    type: Object as PropType<GameGroupGame>,
    required: true
  },
  gameGroupId: {
    type: Number,
    required: true
  }
})

defineEmits(['close'])

class TagSelection {
  constructor(
    readonly description: string,
    readonly id: number,
    public selected: boolean
  ) {}
}
const game = ref(props.game)
const allTags: Ref<TagModel[]> = ref([])
const tags: Ref<TagSelection[]> = ref([])

onMounted(async () => {  
  allTags.value = await loadTags()
  
  tags.value = allTags.value.filter(t => t.type === 'GAME_GROUP').map(t => new TagSelection(t.description, t.id, isGameGroupTagSelected(t)))
})

const errorMessage: Ref<string> = ref('')

async function toggleTagSelection(tag: TagSelection) {  
  if (tag.selected) {
    onClickAddTag(tag);
    game.value.tags.group.push(new TagInGameGroup(tag.id, tag.description))
  } else {
    onClickRemoveTag(tag);
    const index = game.value.tags.group.findIndex(t => t.id === tag.id)
    if (index >= 0) {
      game.value.tags.group.splice(index)
    }
  }
  
}

async function onClickAddTag(tag: TagSelection) {
  await addTagToGameInGroup(props.gameGroupId, props.game.id!, tag.id)
}

async function onClickRemoveTag(tag: TagSelection) {
  await deleteTagFromGameInGroup(props.gameGroupId, props.game.id!, tag.id)
}

function isGameGroupTagSelected(tag: TagModel): boolean {
  return !!game.value.tags.group.find(gameGroupTag => gameGroupTag.id === tag.id)
}
</script>

<style lang="css" scoped>

label {
  margin-left: 8px;
}

</style>
