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
    gamingEvents.value = events
  })
});

function onEventCreated(gamingEvent: GamingEvent) {
  gamingEvents.value.push(gamingEvent)
  showDialog.value = false;
}

</script>