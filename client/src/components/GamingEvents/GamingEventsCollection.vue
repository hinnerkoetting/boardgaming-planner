<template>
  <DataView :value="gamingEvents" class="collection" dataKey="id">
    <template #empty> No events exist </template>
    <template #list="slotProps">
      <div v-for="(item, index) in slotProps.items" :key="index">
        
        <div class="row" >
          {{ item.description }}
          {{  formatDate(new Date(item.start)) }}
        </div>
      </div>
    </template>
  </DataView>

</template>


<script setup lang="ts">
import type { GamingEvent } from '@/model/GamingEvent';
import { DataView } from 'primevue';
import type { PropType } from 'vue';


defineProps({
  gamingEvents: {
    type: Array as PropType<GamingEvent[]>,
    required: true
  }  
})

function formatDate(date: Date) {

  return date.toLocaleDateString(undefined, { day: "numeric", month: "long" });
}

</script>