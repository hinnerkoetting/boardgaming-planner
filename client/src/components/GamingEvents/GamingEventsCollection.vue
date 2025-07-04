<template>
  <DataView :value="gamingEvents" class="collection" dataKey="id">
    <template #empty> No events exist </template>
    <template #list="slotProps">
      <div v-for="(item, index) in slotProps.items" :key="index">
        
         <Card>
            <template #title>
              <div class="title">
              {{ formatDate(new Date(item.start))  }}
              </div>
            </template>
            <template #content>       
              <div>
                {{ item.participants.map((participation: Participation) => participation.participant.name).join('&#183;') }}
              </div>               
              <div>
                {{ item.games.map((game: EventGame) => game.game.name).join('&#183;') }}
              </div>
            </template>
          </Card>
        
      </div>
    </template>
  </DataView>

</template>


<script setup lang="ts">
import type { EventGame, GamingEvent, Participation } from '@/model/GamingEvent';
import { Card, DataView } from 'primevue';
import type { PropType } from 'vue';


defineProps({
  gamingEvents: {
    type: Array as PropType<GamingEvent[]>,
    required: true
  }  
})

function formatDate(date: Date) {

  return date.toLocaleDateString(undefined, { day: "numeric", month: "long", weekday: "long" });
}

</script>