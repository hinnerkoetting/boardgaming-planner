<template>
  <div v-if="event">
    <router-link :to="{ name: 'groupGamingEventsOverview', params: { gameGroupId: gameGroupId }}">Back to events</router-link><br/>
    <h1>{{ formatDate(new Date(event.start)) }}</h1>
   

    {{ event.description }}
    <h2>Participants:</h2>
    
      <div v-for="(participant, index) in event.participants" :key="index">
        {{ participant.participant.name }} <span :class="participicationStatusClass(participant.participationStatus)"/>
      </div>

    <h2>Games:</h2>
      <div v-for="(game, index) in event.games" :key="index">
        <GameEventGameCard
          :game="game.game"
          :game-group-id="gameGroupId"
          :gameStatus="game.gameStatus"
          :gameComment="game.comment"
          :withRateButton="true"
          :with-tag-button="true"
          :players="event.participants.map(p => p.participant)"
          />
      </div>
  </div>
</template>

<script lang="ts" setup>
import GameEventGameCard from '@/components/GamingEvents/GameEventGameCard.vue';
import type { GamingEvent, ParticipationStatus } from '@/model/GamingEvent';
import { fetchGamingEvent } from '@/services/api/GamingEventsApiService';
import { onMounted, ref, type Ref } from 'vue';
import { useRoute } from 'vue-router';
import 'primeicons/primeicons.css'


const route = useRoute()

const gameGroupId = Number(route.params.gameGroupId)
const gamingEventId = Number(route.params.gamingEventId)
const event: Ref<GamingEvent | null> = ref(null)

onMounted(async () => {
  const result = await fetchGamingEvent(gamingEventId)
  if (result.success) {
    event.value = result.success;
  } else {
    console.error('Error when loaing event ' + gamingEventId)
  }
});

function formatDate(date: Date) {
  return date.toLocaleDateString(undefined, { day: "numeric", month: "long", weekday: "long" });
}

function participicationStatusClass(participant: ParticipationStatus) {
  switch (participant) {
    case 'CONFIRMED':
      return 'pi pi-check accepted';
    case 'DECLINED':
      return 'pi pi-times declined';
    case 'MAYBE':
      return 'pi pi-question-circle maybe';
    case 'NOT_RESPONDED':
      return 'pi pi-question-circle not-responded';
    default:
      return '';
  }
}
</script>

<style lang="css" scoped>

.confirmed {
  color:green;
}

.declined {
  color:red;
}

.maybe {
  color:orange;
}

.not-responded {
  color:gray;
}

</style>