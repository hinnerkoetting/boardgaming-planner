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
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  grid-gap: 8px;  
  grid-template-rows: repeat(3, 1fr);  
}

.grid-card {
  
  margin-bottom: auto;
  
  margin-top: 4px;
  height: 100%;
}

@media (max-width: 1023px) {
  .grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 500px) {
  .grid {
    grid-template-columns: repeat(1, 1fr);
  }
}
</style>