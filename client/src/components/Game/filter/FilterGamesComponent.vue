<template>
  <div>
    <div class="filterOption">
      <FilterGamesByPlayersComponent v-model:numberOfPlayers="numberOfPlayers"
        v-model:playerFilterType="playerFilterType" @change="filter" />
    </div>
    <div class="filterOption">
      <FilterGamesByDurationComponent v-model:duration="duration" @change="filter" />
    </div>
    <!-- Global and group tags-->
    <div class="filterOption">
      <FilterGamesByGlobalAndGroupTagsComponent v-model:nonPlayerTags="nonPlayerTags" @change="filter" />
    </div>

    <!-- Player Tags -->
    <div class="filterOption">
      <FilterGamesByPlayerTagsComponent v-model:playerTags="playerTags" @change="filter" />
    </div>

    <div class="newLine"></div>
    <div class="align-right">
      <Checkbox v-model="notRatedYet" binary @change="filter" inputId="notRatedYet"></Checkbox>
      <label for="notRatedYet">Only unrated games</label>
    </div>

    <div class="buttonWrapper">
      <Button severity="primary" class="confirmButton" @click="onClickConfirm">Confirm</Button>
      <Button severity="danger" class="resetButton" @click="onClickReset">Reset</Button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { RatedGame } from '@/model/Game'
import type { Player } from '@/model/Player/Player'
import { TagModel } from '@/model/TagModel'
import {
  FilterService,
  TagSelection,
  PlayerTagSelection,
  type PlayerFilterType
} from '@/services/FilterService'
import { Checkbox } from 'primevue'
import Button from 'primevue/button'
import { onMounted, ref, watch, type PropType, type Ref } from 'vue'
import FilterGamesByPlayersComponent from './FilterGamesByPlayersComponent.vue'
import FilterGamesByDurationComponent from './FilterGamesByDurationComponent.vue'
import FilterGamesByGlobalAndGroupTagsComponent from './FilterGamesByGlobalAndGroupTagsComponent.vue'
import FilterGamesByPlayerTagsComponent from './FilterGamesByPlayerTagsComponent.vue'

const props = defineProps({
  allTags: {
    type: Array as PropType<TagModel[]>,
    required: true
  },
  allGames: {
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

const emit = defineEmits<{
  (e: 'updated-filter', visibleGames: RatedGame[]): void
  (e: 'close'): void
}>()

const nonPlayerTags: Ref<TagSelection[]> = ref([])
const playerTags: Ref<PlayerTagSelection[]> = ref([])
const numberOfPlayers: Ref<number | undefined> = ref(props.numberOfPlayersInGroup)
const duration: Ref<number[]> = ref([10, 300])
const playerFilterType: Ref<PlayerFilterType> = ref('PLAYABLE')
const notRatedYet: Ref<boolean> = ref(false)

const filterService = new FilterService()

watch(
  () => props.allTags,
  (allTags: TagModel[]) => {
    nonPlayerTags.value = updateTagsFromSettings(allTags)
    playerTags.value = updatePlayerTagsFromSettings(allTags)
  }
)

onMounted(async () => {
  const settings = filterService.loadFilterSettings()
  if (settings) {
    nonPlayerTags.value = updateTagsFromSettings(props.allTags)
    playerTags.value = updatePlayerTagsFromSettings(props.allTags)
    numberOfPlayers.value = settings.numberOfPlayers
    duration.value = [settings.minPlayingTime, settings.maxPlayingTime]
    playerFilterType.value = settings.playerFilterType || 'OFF'
    notRatedYet.value = settings.notRatedYet || false
  } else {
    nonPlayerTags.value = createInitialTagSelection(props.allTags)
    playerTags.value = createInitialPlayerTagSelection(props.allTags)
  }
})

function updateTagsFromSettings(allTags: TagModel[]): TagSelection[] {
  const tagSettings = filterService.loadFilterSettings()?.nonPlayerTags
  if (tagSettings) {
    return allTags
      .filter((t) => t.type !== 'PLAYER')
      .map((tag) => {
        const currentSelection = tagSettings.find((tagSetting) => tagSetting.id === tag.id)
        if (currentSelection) {
          return new TagSelection(tag.description, tag.id, currentSelection.selected)
        } else {
          return new TagSelection(tag.description, tag.id, 'DO_NOT_FILTER')
        }
      })
  }
  return allTags.map((tag) => new TagSelection(tag.description, tag.id, 'DO_NOT_FILTER'))
}

function updatePlayerTagsFromSettings(allTags: TagModel[]): PlayerTagSelection[] {
  const tagSettings = filterService.loadFilterSettings()?.playerTags
  if (tagSettings) {
    return allTags
      .filter((t) => t.type === 'PLAYER')
      .map((tag) => {
        const currentSelection = tagSettings.find((tagSetting) => tagSetting.id === tag.id)
        if (currentSelection) {
          return new PlayerTagSelection(tag.description, tag.id, currentSelection.selected)
        } else {
          return new PlayerTagSelection(tag.description, tag.id, 'DO_NOT_FILTER')
        }
      })
  }
  return allTags.map((tag) => new PlayerTagSelection(tag.description, tag.id, 'DO_NOT_FILTER'))
}

function createInitialTagSelection(allTags: TagModel[]): TagSelection[] {
  return allTags
    .filter((t) => t.type === 'GLOBAL' || t.type === 'GAME_GROUP')
    .map((tag) => new TagSelection(tag.description, tag.id, 'DO_NOT_FILTER'))
}

function createInitialPlayerTagSelection(allTags: TagModel[]): PlayerTagSelection[] {
  return allTags
    .filter((t) => t.type === 'PLAYER')
    .map((tag) => new PlayerTagSelection(tag.description, tag.id, 'DO_NOT_FILTER'))
}


function filter() {
  const filterSettings = {
    nonPlayerTags: nonPlayerTags.value,
    playerTags: playerTags.value,
    numberOfPlayers: numberOfPlayers.value,
    minPlayingTime: duration.value[0] as number,
    maxPlayingTime: duration.value[1] as number,
    playerFilterType: playerFilterType.value,
    notRatedYet: notRatedYet.value
  }
  const filteredGames = filterService.filterGames(props.allGames, filterSettings, props.allPlayers)
  emit('updated-filter', filteredGames)
}

function onClickReset() {
  nonPlayerTags.value.forEach((tag) => {
    tag.selected = 'DO_NOT_FILTER'
  })
  playerTags.value.forEach((tag) => {
    tag.selected = 'DO_NOT_FILTER'
  })
  duration.value[0] = 10
  duration.value[1] = 300
  numberOfPlayers.value = props.numberOfPlayersInGroup
  playerFilterType.value = 'PLAYABLE'
  filter()
}

function onClickConfirm() {
  emit('close')
}
</script>

<style lang="css" scoped>
:deep(.one-filter) {
  display: flex;
  gap: 4px;
  flex-grow: 1;
  flex-shrink: 1;
  flex-basis: 0;
  margin: 4px 0;
}

:deep(.filterButton) {
  flex: 1;
  flex-grow: 1;
}

.buttonWrapper {
  width: 100%;
  margin-top: 16px;
}

.resetButton {
  margin-left: auto;
  margin-right: 4px;
  float: right;
}

:deep(.filterOption) {
  border: 0;
  border-top: 1px dashed #ccc;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  padding-top: 16px;
  margin-bottom: 8px;
}

:deep(.filterHeader) {
  color: var(--p-slate-500);
  margin-top: 2px;
}

:deep(.filterContent) {
  margin-top: 8px;
  margin-bottom: 8px;
  align-self: end;
}

.tagFilterOption {
  width: 100%;
}

:deep(.newLine) {
  flex-basis: 100%;
  height: 0;
}

:deep(.p-inputnumber input) {
  width: 100%;
}

label {
  margin-left: 8px;
}

.align-right {
  display: flex;
  flex-direction: row;
  justify-content: end;
}
</style>
