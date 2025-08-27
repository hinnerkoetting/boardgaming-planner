<template>
  <div class="filterHeader">Duration <br />(minutes)</div>
  <div class="filterContent durationContent">
    <InputNumber v-model="duration[0]" showButtons class="durationInput" :min="0" :max="600" :step="10"
      @update:model-value="filter" />
    to
    <InputNumber v-model="duration[1]" showButtons class="durationInput" :min="0" :max="600" :step="10"
      @update:model-value="filter" />
  </div>
  <div class="newLine">
    <Slider v-model="duration" :step="10" range :max="600" :min="0" class="slider" @update:model-value="filter" />
  </div>
</template>

<script setup lang="ts">
import { InputNumber, Slider } from 'primevue';
import type { PropType } from 'vue';


const duration = defineModel('duration', {
  type: Array as PropType<number[]>,
  required: true
})


const emits = defineEmits<{
  (e: 'change'): void
}>()

function filter(): void {
  emits('change')
}


</script>

<style lang="css" scoped>
.durationInput {
  width: 75px;
}

.durationContent {
  display: flex;
  gap: 16px;
  justify-items: center;
  align-items: center;
}

.duration {
  margin-bottom: 16px;
}

.slider {
  margin: 0px 0;
}
</style>