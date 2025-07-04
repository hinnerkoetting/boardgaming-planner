<template>
  <div>
    <h1>Group {{ gameGroup?.name }}</h1>
    <router-link :to="{ name: 'gameGroup', params: { gameGroupId: gameGroupId }}">To games</router-link>
    
    <h2>Next events</h2>
    <Button @click="showDialog = true" label="Create event" />
    <GamingEventsCollection
      v-if="gamingEvents.length > 0"
      :gaming-events="gamingEvents"
      :game-group-id="gameGroupId"
      />

      <Dialog v-model:visible="showDialog" modal header="Create event"><div>
        <CreateGamingEventComponent :game-group-id="gameGroupId" @event-created="onEventCreated"/>
      </div>
    </Dialog>
   
  </div>
</template>

<script setup lang="ts">
import CreateGamingEventComponent from '@/components/GamingEvents/CreateGamingEventComponent.vue';
import GamingEventsCollection from '@/components/GamingEvents/GamingEventsCollection.vue';
import type { GameGroup } from '@/model/GameGroup';
import type { GamingEvent } from '@/model/GamingEvent';
import router from '@/router';
import { loadGameGroup } from '@/services/api/GameGroupApiService';
import { fetchNextGamingEvents } from '@/services/api/GamingEventsApiService';
import { Button, Dialog } from 'primevue';
import { onMounted, ref, type Ref } from 'vue';
import { useRoute } from 'vue-router';


const route = useRoute()

const gameGroup: Ref<GameGroup | null> = ref(null);
const gamingEvents: Ref<GamingEvent[]> = ref([])
const gameGroupId = Number(route.params.gameGroupId)
const showDialog = ref(false);

onMounted(async () => {
  loadGameGroup(gameGroupId).then((result) => {
    gameGroup.value = result
  })
  fetchNextGamingEvents(gameGroupId).then((events) => {    
    gamingEvents.value = filterDuplicateEvents(events.map(e => expand(e, increaseOneMonth(new Date()))).flat().sort((a, b) => a.start - b.start));
  })
});

function filterDuplicateEvents(events: GamingEvent[]): GamingEvent[] {
  const uniqueEvents: GamingEvent[] = []
  
  for (let i = 0; i < events.length; i++) {
    const event = events[i];
    if (i === events.length - 1) {
      uniqueEvents.push(event);      
    } else {
      if (event.start === events[i + 1].start) {
        // Both events have same start time, we will include the non-recurring one. Which will probably not always work, but might be good enough for now.
        if (event.schedule === 'ONCE') {
          uniqueEvents.push(event);
        } else {
          uniqueEvents.push(events[i]);
        }
        i++;
      } else { // non-duplicate
        uniqueEvents.push(event);
      }
    }    
  }
  return uniqueEvents
}

function expand(event: GamingEvent, maxInclusiveDate: Date): GamingEvent[] {
  if (event.schedule === 'ONCE') {
    return [event];
  } else {
    let events: GamingEvent[] = [];
    let recurringDate = event.start
    while (recurringDate <= maxInclusiveDate.getTime()) {
      const newEvent =  { ...event, start: recurringDate };
      
      if (event.schedule === 'WEEKLY') {
        recurringDate = recurringDate + 6.048e+8;
      } else if (event.schedule === 'MONTHLY') {
        recurringDate = increaseOneMonth(new Date(recurringDate)).getTime()        
      } else {
        throw new Error(`Unknown schedule type: ${event.schedule}`);
      }
      events.push(newEvent);
    }
    return events;    
  }
}

function increaseOneMonth(date: Date): Date {
  const newDate = new Date(date);
  newDate.setFullYear(date.getMonth() >= 11 ? date.getFullYear() + 1 : date.getFullYear());
  newDate.setMonth(date.getMonth() >= 11 ? 0 : date.getMonth() + 1);
  return newDate
}

function onEventCreated(gamingEvent: GamingEvent) {
  gamingEvents.value.push(gamingEvent)
  showDialog.value = false;

  router.push({
    name: 'groupGamingEvent',
    params: {
      gamingEventId: gamingEvent.id,
      gameGroupId: gameGroupId
    }
  });
}

</script>