<template>
  <div>
    <h1>Group {{ gameGroup?.name }}</h1>
    <router-link :to="{ name: 'gameGroup', params: { gameGroupId: gameGroupId }}">To games</router-link>
    
    <h2>
      <Button @click="onClickPreviousEvents" variant="link">
       <i class="pi pi-chevron-circle-left"/>
      </Button>
      Events in {{  formatMonth(startTime) }}
      <Button @click="onClickNextEvents" variant="link">
        <i class="pi pi-chevron-circle-right"/>
      </Button>
    </h2>
    
    <Button @click="showDialog = true" label="Create event" style="margin-top: 8px;" />
    <GamingEventsCollection
      v-if="gamingEvents.length > 0"
      :gaming-events="gamingEvents"
      :game-group-id="gameGroupId"
      />

      <br/>
      
       <Button @click="onClickPreviousEvents" variant="link">
        Previous month
      </Button>      
      <Button @click="onClickNextEvents" variant="link">
        Next month
      </Button>
      
      
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
import { onMounted, ref, watch, type Ref } from 'vue';
import { useRoute } from 'vue-router';
import 'primeicons/primeicons.css'

const route = useRoute()

const gameGroup: Ref<GameGroup | null> = ref(null);
const gamingEvents: Ref<GamingEvent[]> = ref([])
const gameGroupId = Number(route.params.gameGroupId)
let startTime = new Date();

const showDialog = ref(false);


onMounted(async () => {
  startTime = route.query.startTime ? new Date(parseInt(route.query.startTime as string)) : startOfCurrentMonth();
  loadGameGroup(gameGroupId).then((result) => {
    gameGroup.value = result
  })
  fetchNextGamingEvents(gameGroupId, startTime).then((events) => {    
    gamingEvents.value = filterDuplicateEvents(events.map(e => expand(e)).flat().sort((a, b) => a.start - b.start));
  })
});

watch(() => route.query.startTime, (newValue) => {
  if (newValue) {
    startTime = new Date(parseInt(route.query.startTime as string))
    fetchNextGamingEvents(gameGroupId, startTime).then((events) => {    
      gamingEvents.value = filterDuplicateEvents(events.map(e => expand(e)).flat().sort((a, b) => a.start - b.start));
    })
  }
})

function startOfCurrentMonth(): Date {
  const date = new Date();
  date.setDate(1);
  date.setHours(0, 0, 0, 0);
  return date;
}

function formatMonth(date: Date) {
  return date.toLocaleDateString(undefined, { month: "long" });
}

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

function expand(event: GamingEvent): GamingEvent[] {
  const minInclusiveDate = startTime
  const maxInclusiveDate = increaseOneMonth(startTime)
  if (event.schedule === 'ONCE') {
    if (event.start <= maxInclusiveDate.getTime() && event.start >= minInclusiveDate.getTime()) {
      return [event];
    } else {
      return [];
    }    
  } else {
    let events: GamingEvent[] = [];
    let recurringDate = determineScheduleStartDate(new Date(event.start), event.schedule).getTime()
    while (recurringDate <= maxInclusiveDate.getTime() && recurringDate >= minInclusiveDate.getTime()) {
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

function determineScheduleStartDate(scheduleBaseTime: Date, schedule: 'WEEKLY' | 'MONTHLY'): Date {
  const minInclusiveDate = startTime;
  const maxInclusiveDate = increaseOneMonth(startTime)
  if (scheduleBaseTime >= startTime && scheduleBaseTime <= maxInclusiveDate) {
    return scheduleBaseTime;
  }
  if (minInclusiveDate < scheduleBaseTime) {
    return scheduleBaseTime
  }
  const scheduleInterval = schedule === 'WEEKLY' ? 6.048e+8 : 2.628e+9; // 1 week in ms or 1 month in ms
  const numberOMissingSchedules = Math.floor((minInclusiveDate.getTime() - scheduleBaseTime.getTime()) / scheduleInterval)
  const firstInclusiveDate = scheduleBaseTime.getTime() + (numberOMissingSchedules + 1) * scheduleInterval;
  return new Date(firstInclusiveDate)
}

function increaseOneMonth(date: Date): Date {
  const newDate = new Date(date);  
  newDate.setMonth(date.getMonth() + 1);
  return newDate
}


function decreaseOneMonth(date: Date): Date {
  const newDate = new Date(date);  
  newDate.setMonth(date.getMonth() - 1);
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


function onClickPreviousEvents() {
  router.replace({
    name: 'groupGamingEventsOverview',
    params: {
      gameGroupId: gameGroupId
    },
    query: {
      startTime: decreaseOneMonth(startTime).getTime().toString()
    }
  });
}

function onClickNextEvents() {
  router.replace({
    name: 'groupGamingEventsOverview',
    params: {
      gameGroupId: gameGroupId
    },
    query: {
      startTime: increaseOneMonth(startTime).getTime().toString()
    }
  });
}

</script>