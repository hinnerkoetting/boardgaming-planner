<template>
  <div>
    <div>
      <div class="numberOfPlayers filterOption">
        <div class="filterHeader">
          Players
        </div>
        <div class="filterContent ">
          <InputNumber
            v-model="numberOfPlayers"
            showButtons
            class="numberOfPlayersInput"
            :min="0"
            :max="99"
            @update:model-value="filter"
          />
          <Button v-if="numberOfPlayersInGroup" @click="copyNumberOfPlayers"
            >&#x2190; From group ({{ numberOfPlayersInGroup }})</Button
          >
        </div>
      </div>
    </div>
    <div class="duration filterOption">
      <div class="filterHeader">
          Duration <br/>(minutes)
        </div>
        <div class="filterContent durationContent">
          <InputNumber
            v-model="duration[0]"
            showButtons
            class="durationInput"
            :min="0"
            :max="600"
            :step="10"
            @update:model-value="filter"
          />
          to
          <InputNumber
            v-model="duration[1]"
            showButtons
            class="durationInput"
            :min="0"
            :max="600"
            :step="10"
            @update:model-value="filter"
          />          
      </div>
      <div class="newLine">
        <Slider v-model="duration" :step="10" range :max="600" :min="0" class="slider" @update:model-value="filter"/>
      </div>      
    </div>
    <div class="filterOption">
      <div class="filterContent">        
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
    </div>
   
    <div class="buttonWrapper">
      <Button severity="primary" class="confirmButton" @click="onClickConfirm">Confirm</Button>
      <Button severity="danger" class="resetButton" @click="onClickReset">Reset</Button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { GameGroupGame } from '@/model/Game'
import { TagModel } from '@/model/TagModel'
import { FilterService, TagSelection } from '@/services/FilterService'
import Button from 'primevue/button'
import InputNumber from 'primevue/inputnumber'
import Slider from 'primevue/slider'
import { onMounted, ref, watch, type PropType, type Ref } from 'vue'

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

const emit = defineEmits<{
  (e: 'updated-filter', visibleGames: GameGroupGame[]): void
  (e: 'close'): void
}>()

const tags: Ref<TagSelection[]> = ref([])
const numberOfPlayers: Ref<number | undefined> = ref(undefined)
const duration: Ref<number[]> = ref([10, 300])

const filterService = new FilterService()

watch(
  () => props.allTags,
  (allTags: TagModel[]) => {
    tags.value = updateTagsFromSettings(allTags)
  }
)

onMounted(async () => {
  const settings = filterService.loadFilterSettings()
  if (settings) {
    tags.value = updateTagsFromSettings(props.allTags)
    numberOfPlayers.value = settings.numberOfPlayers
    duration.value = [settings.minPlayingTime, settings.maxPlayingTime]
  } else {
    tags.value = createTagSelection(props.allTags)
  }
})

function updateTagsFromSettings(allTags: TagModel[]): TagSelection[] {
  const tagSettings = filterService.loadFilterSettings()?.tags
  if (tagSettings) {
    return allTags.map(tag => {
      const currentSelection = tagSettings.find(tagSetting => tagSetting.id === tag.id)
      if (currentSelection) {
        return new TagSelection(tag.description, tag.id, currentSelection.selected)
      } else {
        return new TagSelection(tag.description, tag.id, 'DO_NOT_FILTER')
      }
    })
  }
  return allTags.map((tag) => new TagSelection(tag.description, tag.id, 'DO_NOT_FILTER'))
}

function createTagSelection(allTags: TagModel[]): TagSelection[] {
  return allTags.map((tag) => new TagSelection(tag.description, tag.id, 'DO_NOT_FILTER'))
}

async function onClickFilterWith(tag: TagSelection) {
  if (tag.selected === 'FILTER_WITH') {
    tag.selected = 'DO_NOT_FILTER'
  } else {
    tag.selected = 'FILTER_WITH'
  }
  filter()
}

async function onClickFilterWithout(tag: TagSelection) {
  if (tag.selected === 'FILTER_WITHOUT') {
    tag.selected = 'DO_NOT_FILTER'
  } else {
    tag.selected = 'FILTER_WITHOUT'
  }
  filter()
}

function filter() {
  const filterSettings = {
    tags: tags.value,
    numberOfPlayers: numberOfPlayers.value,
    minPlayingTime: duration.value[0],
    maxPlayingTime: duration.value[1]
  }
  const filteredGames = filterService.filterGames(props.allGames, filterSettings)
  emit('updated-filter', filteredGames)
}

function copyNumberOfPlayers() {
  numberOfPlayers.value = props.numberOfPlayersInGroup
  filter()
}

function onClickReset() {
  tags.value.forEach((tag) => {
    tag.selected = 'DO_NOT_FILTER'
  })
  duration.value[0] = 10
  duration.value[1] = 300
  numberOfPlayers.value = undefined
  filter()
}

function onClickConfirm() {
  emit('close')
}
</script>

<style lang="css" scoped>

.one-filter { 
  display: flex;
  gap: 4px;
  flex-grow: 1;
  flex-shrink: 1;
  flex-basis: 0;
  margin: 4px 0;
}

.filterButton {
  flex: 1;
  width: 150px;
}

.numberOfPlayersInput {
  width: 75px;
  margin-right: 4px;
}

.numberOfPlayers {
  display: flex;
  gap: 8px;
  align-items: center;
}

.duration {
  margin-top: 16px;
  margin-bottom: 32px;
}

.durationInput {
  width: 75px;
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

.filterOption {
  border: 0;
  border-top: 1px dashed #ccc;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap
}

.filterHeader {
  align-self: start;
  color: var(--p-slate-500);  
  margin-top: 2px;
}

.slider {
  margin-top: 4px;
}

.filterContent {
  margin-top: 8px;
  margin-bottom: 8px;
  align-self: end;
}

.durationContent {
  display: flex;
  gap: 16px;
  justify-items: center;
  align-items: center;
}

.newLine {
  flex-basis: 100%;
  height: 0;
}
</style>

<style lang="css">
.p-inputnumber input {
  width: 100%;
}
</style>
