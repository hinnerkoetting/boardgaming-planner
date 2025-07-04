<template>
  <div>
    <Message v-if="errorMessage" severity="error" class="full-width">{{ errorMessage }}</Message>
    
    <div v-for="tag in tags" :key="tag.id" class="one-filter">
      <Checkbox v-model="tag.selected" binary :inputId="'tag_' + tag.id"></Checkbox>
      <label :for="'tag_' + tag.id">{{ tag.description }}</label>
      
    </div>
    <Button 
      style="margin-top: 16px;"
      @click.stop="$emit('close')" 
      severity="primary">OK</Button>
  </div>
</template>

<script setup lang="ts">
import type { GameGroupGame, TagInGameGroup } from '@/model/Game';
import type { TagModel } from '@/model/TagModel';
import { fetchTags } from '@/services/api/TagApiService';
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
  allTags.value =  await fetchTags()
  
  tags.value = allTags.value.filter(t => t.type === 'GAME_GROUP').map(t => new TagSelection(t.description, t.id, isGameGroupTagSelected(t)))
})

const errorMessage: Ref<string> = ref('')

function onClickAddTag(tag: TagSelection) {

}

function onClickRemoveTag(tag: TagSelection) {

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