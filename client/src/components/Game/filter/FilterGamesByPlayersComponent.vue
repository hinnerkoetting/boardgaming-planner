<template>
  <div class="numberOfPlayers">
    <div class="filterHeader">Player count</div>
    <div class="filterContent">
      <InputNumber v-model="numberOfPlayers" showButtons class="numberOfPlayersInput" :min="1" :max="99"
        @update:model-value="filter" />
      <br />
    </div>
    <div class="playerFilterChoice">
      <RadioButton v-model="playerFilterType" inputId="BEST" value="BEST" @change="filter" />
      <label for="BEST" class="playerFilterLabel">Best</label>

      <RadioButton v-model="playerFilterType" inputId="RECOMMENDED" value="RECOMMENDED" @change="filter" />
      <label for="RECOMMENDED" class="playerFilterLabel">Recommended</label>

      <RadioButton v-model="playerFilterType" inputId="PLAYABLE" value="PLAYABLE" @change="filter" />
      <label for="PLAYABLE" class="playerFilterLabel">Playable</label>

      <RadioButton v-model="playerFilterType" inputId="OFF" value="OFF" @change="filter" />
      <label for="OFF" class="playerFilterLabel">Any</label>
    </div>
  </div>
</template>

<script setup lang="ts">
import { InputNumber, RadioButton } from 'primevue';

const numberOfPlayers = defineModel('numberOfPlayers', {
  type: Number,
  default: 0
})
const playerFilterType = defineModel('playerFilterType')

const emits = defineEmits<{
  (e: 'change'): void
}>()

function filter(): void {
  emits('change')
}
</script>

<style lang="css" scoped>
.numberOfPlayersInput {
  width: 75px;
  margin-right: 4px;
}

.numberOfPlayers {
  display: flex;
  gap: 8px;
  align-items: center;

  justify-content: space-between;
  flex-wrap: wrap;
}

.playerFilterLabel {
  line-height: 1.1;
}

.playerFilterChoice {
  display: flex;
  justify-content: space-between;
  width: 100%;
}
</style>