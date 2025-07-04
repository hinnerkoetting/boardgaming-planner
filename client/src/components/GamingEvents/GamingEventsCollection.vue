<template>  
  <DataView :value="internGamingEvents" class="collection" dataKey="id">
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
import { ref, watch, type PropType } from 'vue';
import GamingEventCard from './GamingEventCard.vue';


const props = defineProps({
  gamingEvents: {
    type: Array as PropType<GamingEvent[]>,
    required: true
  },
  gameGroupId: {
    type: Number,
    required: true
  }
})

const internGamingEvents = ref<GamingEvent[]>(props.gamingEvents)

watch(
  () => props.gamingEvents,
  (gamingEvents: GamingEvent[]) => {
    internGamingEvents.value = gamingEvents
  }
)

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