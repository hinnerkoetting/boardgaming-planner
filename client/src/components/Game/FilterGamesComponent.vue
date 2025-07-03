<template>
  <div>
    <div>
      <div class="numberOfPlayers">
        Number of players
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
    <div class="duration">
      Duration (minutes)
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

      <Slider v-model="duration" :step="10" range :max="600" :min="0" class="slider" />
    </div>
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
    <div class="resetWrapper">
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
}>()

const tags: Ref<TagSelection[]> = ref(createTagSelection(props.allTags))
const numberOfPlayers: Ref<number | undefined> = ref(undefined)
const duration: Ref<number[]> = ref([10, 300])

const filterService = new FilterService()

watch(
  () => props.allTags,
  (allTags: TagModel[]) => {
    tags.value = createTagSelection(allTags)
  }
)

onMounted(async () => {
  const settings = filterService.loadFilterSettings()
  if (settings) {
    tags.value = settings.tags
    numberOfPlayers.value = settings.numberOfPlayers
    duration.value = [settings.minPlayingTime, settings.maxPlayingTime]
  }
})

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

.numberOfPlayersInput {
  width: 75px;
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
  width: 100px;
}

.slider {
  margin-top: 16px;
}

.resetWrapper {
  width: 100%;
}
.resetButton {
  margin-top: 16px;
  margin-left: auto;
  margin-right: 4px;
  float: right;
}
</style>

<style lang="css">
.p-inputnumber input {
  width: 100%;
}
</style>
