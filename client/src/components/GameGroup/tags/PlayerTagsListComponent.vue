<template>
  <DataView :value="props.playerTags" class="collection" dataKey="id">    
    <template #list="slotProps">
      <div v-for="(item, index) in slotProps.items" :key="index">
       test
      </div>
    </template>
  </DataView>
</template>

<script setup lang="ts">
import type { PropType } from 'vue';

const props = defineProps({
  playerTags: {
    type: Array as PropType<PlayerTagRow[]>,
    required: true
  }
})


</script> 

<script lang="ts">
export class PlayerTagRow {
  constructor(
    readonly description: string,
    readonly id: number,
    readonly selected: boolean
  ) {}
}

export class PlayerTagSelection {
  constructor(
    readonly playerId: number,
    readonly selected: boolean
  ) {}
}
</script>