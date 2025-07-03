<template>
  <DataView :value="gameGroups" class="collection">
    <template #empty> No group exists. Try creating one below. </template>
    <template #list="slotProps">
      <div v-for="(item, index) in slotProps.items" :key="index">
        <div class="row" @click="$emit('onRowClick', item)">
          {{ item.name }}
          <Button @click="$emit('onClickJoinGroup', item)" v-if="withJoinButton"> Join </Button>
        </div>
      </div>
    </template>
  </DataView>
</template>

<script setup lang="ts">
import { GameGroup } from '@/model/GameGroup'
import Button from 'primevue/button'
import DataView from 'primevue/dataview'
import type { PropType } from 'vue'

defineProps({
  gameGroups: {
    type: Array as PropType<GameGroup[]>,
    required: true
  },
  withJoinButton: {
    type: Boolean,
    default: false
  }
})

defineEmits<{
  (e: 'onRowClick', gameGroup: GameGroup): void
  (e: 'onClickJoinGroup', gameGroup: GameGroup): void
}>()
</script>

<style lang="css" scoped>
.row {
  border-color: #e2e8f0;
  border-style: solid;
  border-width: 1px 0 1px 0;
  padding: 8px;
  font-size: 15px;
  line-height: 1.6;
  padding-left: 16px;
  display: flex;
  column-gap: 32px;
  vertical-align: middle;
  align-items: center;
  justify-content: space-between;
}

.row:hover {
  background: #f1f5f9;
}

button {
  margin-left: 20px;
}
</style>
