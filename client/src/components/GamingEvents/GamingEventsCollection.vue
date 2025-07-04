<template>  
  <DataView :value="gamingEvents" class="collection" dataKey="id">
    <template #empty> No events exist </template>
    <template #list="slotProps">
      <div class="grid">
        <div v-for="(item, index) in slotProps.items" :key="index">
          <div class="grid-card">
            <GamingEventCard :event="item" :gameGroupId="gameGroupId" />
          </div>
          
        </div>
      </div>
    </template>
  </DataView>

</template>


<script setup lang="ts">
import type { GamingEvent } from '@/model/GamingEvent';
import { DataView } from 'primevue';
import type { PropType } from 'vue';
import GamingEventCard from './GamingEventCard.vue';


defineProps({
  gamingEvents: {
    type: Array as PropType<GamingEvent[]>,
    required: true
  },
  gameGroupId: {
    type: Number,
    required: true
  }
})

</script>

<style lang="css" scoped>

.grid {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 16px;
}

.grid-card {
  flex: 1 1 0px;
  margin-bottom: auto;
  align-self: stretch;
  margin-top: 4px;
}

@media (max-width: 500px) {
  .grid {
    display: block;
  }
  .grid-card {
    flex: 0 0 100%;
  }
}
</style>