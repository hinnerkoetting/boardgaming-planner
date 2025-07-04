<template>
  <div v-if="event">
    <Button label="Back to events" variant="link" @click="onBackToEvents"/>
    
    <h1>{{ startTime && formatDate(startTime) }}</h1>
    <div v-if="event.schedule == 'WEEKLY'">
      <i>Every week</i> <Button label="Edit single event" severity="secondary" variant="link" @click="editSingleEvent"/>
    </div>
    <div v-if="event.schedule == 'MONTHLY'">
      <i>Every month</i> <Button label="Edit single event" severity="secondary" variant="link" @click="editSingleEvent"/>
    </div>

    {{ event.description }}
    <h2>Participants:</h2>
    
    <div v-for="(participant, index) in event.participants" :key="index" class="participant">
      <span :class="participicationStatusClass(participant.participationStatus)" style="margin-right: 8px;"/>
      {{ participant.participant.name }} 
      <span class="participant-buttons" v-if="isMe(participant.participant.id)">
        <Message v-if="errorMessage" severity="error">{{ errorMessage }}</Message>
        <Button :disabled="participant.participationStatus == 'CONFIRMED'" 
                label="Confirm"                 
                @click="onConfirmParticipation(participant.participant.id)" />
        <Button :disabled="participant.participationStatus == 'DECLINED'" 
                label="Decline" 
                severity="warn"
                @click="onDeclineParticipation(participant.participant.id)" />
        <Button :disabled="participant.participationStatus == 'MAYBE'" 
                label="Maybe" 
                severity="secondary"
                @click="onMaybeParticipation(participant.participant.id)" />     
      </span>
      
    </div>
    <Button label="Invite missing players" severity="secondary" 
              @click="addMissingPlayers" style="margin-top: 16px"/>

    <h2>Games:</h2>
      <h2 class="green">Add game</h2>
      <AddGameToGroupComponent @game-added="onGameAdded" ref="addGameComponent"  style="margin-bottom: 16px;"/>
      <div v-for="(game, index) in event.games" :key="index" style="margin: 8px">
        <GameEventGameCard
          :game="game.game"
          :game-group-id="gameGroupId"
          :gameStatus="game.gameStatus"
          :gameComment="game.comment"
          :withRateButton="true"
          :with-tag-button="true"
          :players="event.participants.map(p => p.participant)"
          :gaming-event-id="gamingEventId"
          @game-removed="onGameRemoved"
          />
      </div>      
    <Button severity="danger" label="Delete event" @click="onDeleteEvent" style="margin-top: 16px"/>
  </div>

  
</template>

<script lang="ts" setup>
import GameEventGameCard from '@/components/GamingEvents/GameEventGameCard.vue';
import type { GamingEvent, ParticipationStatus } from '@/model/GamingEvent';
import { addAllGroupMembersToGamingEvent, addGameToEvent, cloneEvent, deleteEvent, fetchGamingEvent, updateEvent, updateParticipationStatus } from '@/services/api/GamingEventsApiService';
import { onMounted, ref, type Ref } from 'vue';
import { onBeforeRouteUpdate, useRoute, type LocationQuery } from 'vue-router';
import 'primeicons/primeicons.css'
import { Button, Message } from 'primevue';
import { getCurrentPlayerId } from '@/services/LoginService';
import type { Game } from '@/model/Game';
import AddGameToGroupComponent from '@/components/Game/AddGameToGroupComponent.vue';
import router from '@/router';


const route = useRoute()

let gameGroupId: number;
let gamingEventId: number;
let startTime: Date | null = null;
const errorMessage = ref('')
const event: Ref<GamingEvent | null> = ref(null)

onBeforeRouteUpdate(async (to, from, next) => {
  gameGroupId = Number(to.params.gameGroupId)
  gamingEventId = Number(to.params.gamingEventId)
  startTime = parse(to.query)
  event.value = null;
  await loadData();
  next();
})

onMounted(async () => {
  gameGroupId = Number(route.params.gameGroupId)
  gamingEventId = Number(route.params.gamingEventId)
  startTime = parse(route.query)
  await loadData();
});

function parse(params: LocationQuery): Date | null {
  if (params.startTime) {
    return new Date(parseInt(params.startTime as string));
  }
  return null;
}

async function loadData(){

  const result = await fetchGamingEvent(gamingEventId)
  if (result.success) {
    event.value = result.success;
    if (!startTime) {
      startTime = new Date(event.value.start)
    }
  } else {    
    console.error('Error when loading event ' + gamingEventId)
  }
}

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

async function onConfirmParticipation(participantId: number) {
  await updateStatus(participantId, 'CONFIRMED');  
}

async function onDeclineParticipation(participantId: number) {
  await updateStatus(participantId, 'DECLINED');
}

async function onMaybeParticipation(participantId: number) {
  await updateStatus(participantId, 'MAYBE');
}

async function updateStatus(participantId: number, status: ParticipationStatus) {
  errorMessage.value = '';
  const result = await updateParticipationStatus(gamingEventId, participantId, 'CONFIRMED')
  if (result.error) {
    errorMessage.value = result.error.detail ?? 'Error';
  } else {
    event.value?.participants.filter(participant => participant.participant.id === participantId).forEach(participant => {
      participant.participationStatus = status;
    });
  }
}

async function addMissingPlayers() {
  const result = await addAllGroupMembersToGamingEvent(gamingEventId)
  if (result.success) {
    event.value = result.success;    
  } else {
    errorMessage.value = result.error?.detail ?? 'Error';
  }
}

function isMe(playerId: number): boolean {
  return getCurrentPlayerId() == playerId;
}

async function onGameAdded(game: Game) {
  await addGameToEvent(gamingEventId, game.id!)
  event.value?.games.push({
    game,
    gameStatus: "SUGGESTED",
    comment: null
  })
}

function onGameRemoved(game: Game) {
  event.value?.games.splice(event.value?.games.findIndex(g => g.game.id !== game.id), 1);
}

async function editSingleEvent() {
  const result = await cloneEvent(gameGroupId, gamingEventId)
  if (result.success) {
    
    const newStartTime = startTime ?? new Date()
    const updateResult = await updateEvent(gameGroupId, result.success.id, newStartTime, 'ONCE', event.value!.description)
    if (updateResult.success) {
       router.push({
        name: 'groupGamingEvent',
        params: {
          gamingEventId: updateResult.success.id,
          gameGroupId: gameGroupId
        },
        query: {
          startTime: newStartTime.getTime().toString()
        },
        replace: true
      });
    } else {
      errorMessage.value = updateResult.error?.detail ?? 'Error';
    }
  } else {
    errorMessage.value = result.error?.detail ?? 'Error';
  }
}

async function onDeleteEvent() {
  const result = await deleteEvent(gamingEventId)
  if (result.success) {
    router.push({
      name: 'groupGamingEventsOverview'      
    });
  } else {
    errorMessage.value = result.error?.detail ?? 'Error';
  }
  
}

function onBackToEvents() {
  router.push({
    name: 'groupGamingEventsOverview',
    params: {
      gameGroupId: gameGroupId
    },
    query: {
      startTime: startTime?.getTime().toString()
    }
  });
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

.participant {  
  width: 400px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.participant-buttons {
  width: 100%;  
  display: flex;
  align-items: end;
  justify-content: end;
  gap:4px;
}

</style>