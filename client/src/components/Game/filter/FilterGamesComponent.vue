<template>
  <div>
    <div>
      <div class="numberOfPlayers filterOption">
        <div class="filterHeader">Player count</div>
        <div class="filterContent">
          <InputNumber
            v-model="numberOfPlayers"
            showButtons
            class="numberOfPlayersInput"
            :min="0"
            :max="99"
            @update:model-value="filter"
          />
          <br />
        </div>
        <div class="playerFilterChoice">
          <RadioButton v-model="playerFilterType" inputId="BEST" value="BEST" @change="filter" />
          <label for="BEST" class="playerFilterLabel">Best</label>

          <RadioButton
            v-model="playerFilterType"
            inputId="RECOMMENDED"
            value="RECOMMENDED"
            @change="filter"
          />
          <label for="RECOMMENDED" class="playerFilterLabel">Recommended</label>

          <RadioButton
            v-model="playerFilterType"
            inputId="PLAYABLE"
            value="PLAYABLE"
            @change="filter"
          />
          <label for="PLAYABLE" class="playerFilterLabel">Playable</label>

          <RadioButton v-model="playerFilterType" inputId="OFF" value="OFF" @change="filter" />
          <label for="OFF" class="playerFilterLabel">Any</label>
        </div>
      </div>
    </div>
    <div class="filterOption duration">
      <div class="filterHeader">Duration <br />(minutes)</div>
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
        <Slider
          v-model="duration"
          :step="10"
          range
          :max="600"
          :min="0"
          class="slider"
          @update:model-value="filter"
        />
      </div>
    </div>
    <!-- Global and group tags-->
    <div class="filterOption">
      <div class="filterContent tagFilterOption" >
        <div v-for="tag in nonPlayerTags" :key="tag.id" class="one-filter">
          <Button
            :severity="tag.selected === 'FILTER_WITH' ? 'primary' : 'secondary'"
            @click="onClickFilterWith(tag)"
            class="filterButton"
            >Only {{ tag.description }}</Button
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

    <!-- Player Tags -->
    <div class="filterOption">
      <div class="filterContent tagFilterOption" >
        <div v-for="tag in playerTags" :key="tag.id" class="one-filter">
          <Button
            :severity="tag.selected === 'FILTER_EVERYONE' ? 'primary' : 'secondary'"
            @click="onClickFilterEveryone(tag)"
            class="filterButton"
            >Everyone {{ tag.description }}</Button
          >
          <Button
            :severity="tag.selected === 'FILTER_ANYONE' ? 'primary' : 'secondary'"
            @click="onClickFilterAnyone(tag)"
            class="filterButton"
            >Anyone {{ tag.description }}</Button
          >
          <Button
            :severity="tag.selected === 'FILTER_NOBODY' ? 'primary' : 'secondary'"
            @click="onClickFilterNobody(tag)"
            class="filterButton"
            >Nobody {{ tag.description }}</Button
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
import type { Player } from '@/model/Player/Player'
import { TagModel } from '@/model/TagModel'
import { FilterService, TagSelection, PlayerTagSelection, type PlayerFilterType } from '@/services/FilterService'
import Button from 'primevue/button'
import InputNumber from 'primevue/inputnumber'
import RadioButton from 'primevue/radiobutton'
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
  },
  allPlayers: {
    type: Array as PropType<Player[]>,
    required: true
  }
})

const emit = defineEmits<{
  (e: 'updated-filter', visibleGames: GameGroupGame[]): void
  (e: 'close'): void
}>()

const nonPlayerTags: Ref<TagSelection[]> = ref([])
const playerTags: Ref<PlayerTagSelection[]> = ref([])
const numberOfPlayers: Ref<number | undefined> = ref(props.numberOfPlayersInGroup)
const duration: Ref<number[]> = ref([10, 300])
const playerFilterType: Ref<PlayerFilterType> = ref('PLAYABLE')

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
  } else {
    nonPlayerTags.value = createInitialTagSelection(props.allTags)
    playerTags.value = createInitialPlayerTagSelection(props.allTags)
  }
})

function updateTagsFromSettings(allTags: TagModel[]): TagSelection[] {
  const tagSettings = filterService.loadFilterSettings()?.nonPlayerTags
  if (tagSettings) {
    return allTags.filter(t => t.type !== 'PLAYER').map((tag) => {
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
    return allTags.filter(t => t.type === 'PLAYER').map((tag) => {
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
  return allTags.filter(t => t.type === 'GLOBAL' || t.type === 'GAME_GROUP').map((tag) => new TagSelection(tag.description, tag.id, 'DO_NOT_FILTER'))
}

function createInitialPlayerTagSelection(allTags: TagModel[]): PlayerTagSelection[] {
  return allTags.filter(t => t.type === 'PLAYER').map((tag) => new PlayerTagSelection(tag.description, tag.id, 'DO_NOT_FILTER'))
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

async function onClickFilterEveryone(tag: PlayerTagSelection) {
  if (tag.selected === 'FILTER_EVERYONE') {
    tag.selected = 'DO_NOT_FILTER'
  } else {
    tag.selected = 'FILTER_EVERYONE'
  }
  filter()
}

async function onClickFilterAnyone(tag: PlayerTagSelection) {
  if (tag.selected === 'FILTER_ANYONE') {
    tag.selected = 'DO_NOT_FILTER'
  } else {
    tag.selected = 'FILTER_ANYONE'
  }
  filter()
}

async function onClickFilterNobody(tag: PlayerTagSelection) {
  if (tag.selected === 'FILTER_NOBODY') {
    tag.selected = 'DO_NOT_FILTER'
  } else {
    tag.selected = 'FILTER_NOBODY'
  }
  filter()
}

function filter() {
  const filterSettings = {
    nonPlayerTags: nonPlayerTags.value,
    playerTags: playerTags.value,
    numberOfPlayers: numberOfPlayers.value,
    minPlayingTime: duration.value[0],
    maxPlayingTime: duration.value[1],
    playerFilterType: playerFilterType.value
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
  flex-grow: 1;
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
  flex-wrap: wrap;
  padding-top: 16px;
  margin-bottom: 8px;
}

.filterHeader {
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

.tagFilterOption {
  width: 100%;
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

.playerFilterChoice {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.playerFilterLabel {
  line-height: 1.1;
}

.duration {
  margin-bottom: 16px;
}
</style>

<style lang="css">
.p-inputnumber input {
  width: 100%;
}
</style>
