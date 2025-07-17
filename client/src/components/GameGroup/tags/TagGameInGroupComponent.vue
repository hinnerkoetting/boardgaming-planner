<template>
  <div>
    <Message v-if="errorMessage" severity="error" class="full-width">{{ errorMessage }}</Message>

    <h3>Group</h3>
    <div v-for="tag in groupTags" :key="tag.id" class="one-filter">
      <Checkbox
        v-model="tag.selected"
        binary
        :inputId="'tag_' + tag.id"
        @value-change="toggleGroupTagSelection(tag)"
      ></Checkbox>
      <label :for="'tag_' + tag.id">{{ tag.description }}</label>
    </div>

    <h3>Me</h3>
    <div v-for="tag in myPlayerTags" :key="tag.id" class="one-filter">
      <Checkbox
        v-model="tag.selected"
        binary
        :inputId="'tag_' + tag.id"
        @value-change="togglePlayerTagSelection(tag)"
      ></Checkbox>
      <label :for="'tag_' + tag.id">{{ tag.description }}</label>
    </div>

    <Button style="margin-top: 16px" @click.stop="$emit('close')" severity="primary">OK</Button>
  </div>
</template>

<script setup lang="ts">
import { PlayerTagInGameGroup, TagInGameGroup, type RatedGame } from '@/model/Game'
import type { TagModel } from '@/model/TagModel'
import {
  addTagToGameInGroup,
  addTagToPlayereInGroup,
  deleteTagFromGameInGroup,
  deleteTagFromPlayerInGroup
} from '@/services/api/GameGroupApiService'
import { getCurrentPlayerId } from '@/services/LoginService'
import { loadTags } from '@/services/StoreApiService'
import { Button, Checkbox, Message } from 'primevue'
import { onMounted, ref, type PropType, type Ref } from 'vue'

const props = defineProps({
  game: {
    type: Object as PropType<RatedGame>,
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
const groupTags: Ref<TagSelection[]> = ref([])
const myPlayerTags: Ref<TagSelection[]> = ref([])

onMounted(async () => {
  allTags.value = await loadTags()

  groupTags.value = allTags.value
    .filter((t) => t.type === 'GAME_GROUP')
    .map((t) => new TagSelection(t.description, t.id, isGameGroupTagSelected(t)))
  myPlayerTags.value = allTags.value
    .filter((t) => t.type === 'PLAYER')
    .map((t) => new TagSelection(t.description, t.id, isPlayerTagSelected(t)))
})

const errorMessage: Ref<string> = ref('')

async function toggleGroupTagSelection(tag: TagSelection) {
  if (tag.selected) {
    onClickAddGroupTag(tag)
    game.value.tags.group.push(new TagInGameGroup(tag.id, tag.description))
  } else {
    onClickRemoveGroupTag(tag)
    const index = game.value.tags.group.findIndex((t) => t.id === tag.id)
    if (index >= 0) {
      game.value.tags.group.splice(index)
    }
  }
}

async function onClickAddGroupTag(tag: TagSelection) {
  await addTagToGameInGroup(props.gameGroupId, props.game.id!, tag.id)
}

async function onClickRemoveGroupTag(tag: TagSelection) {
  await deleteTagFromGameInGroup(props.gameGroupId, props.game.id!, tag.id)
}

function isGameGroupTagSelected(tag: TagModel): boolean {
  return !!game.value.tags.group.find((gameGroupTag) => gameGroupTag.id === tag.id)
}

async function togglePlayerTagSelection(tag: TagSelection) {
  if (tag.selected) {
    onClickAddPlayerTag(tag)
    game.value.tags.player.push(
      new PlayerTagInGameGroup(tag.id, tag.description, getCurrentPlayerId())
    )
  } else {
    onClickRemovePlayerTag(tag)
    const index = game.value.tags.player.findIndex((t) => t.id === tag.id)
    if (index >= 0) {
      game.value.tags.player.splice(index)
    }
  }
}

async function onClickAddPlayerTag(tag: TagSelection) {
  await addTagToPlayereInGroup(props.gameGroupId, props.game.id!, getCurrentPlayerId(), tag.id)
}

async function onClickRemovePlayerTag(tag: TagSelection) {
  await deleteTagFromPlayerInGroup(props.gameGroupId, props.game.id!, getCurrentPlayerId(), tag.id)
}

function isPlayerTagSelected(tag: TagModel): boolean {
  const currentPlayerId = getCurrentPlayerId()
  return game.value.tags.player.some(
    (playerTag) => playerTag.id === tag.id && currentPlayerId === playerTag.playerId
  )
}
</script>

<style lang="css" scoped>
label {
  margin-left: 8px;
}
</style>
