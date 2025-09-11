<template>
  <div v-if="event">
    <Button label="Back to calendar" variant="link" @click="onBackToEvents" />

    <h1>{{ startTime && formatDate(startTime) }}</h1>
    <div v-if="event.schedule == 'WEEKLY'" class="pi pi-replay">
      <i style="padding-left: 4px">Every week</i>
      <Button label="Edit single event" severity="secondary" variant="link" @click="editSingleEvent"
        v-if="isPartOfGroup" />
    </div>
    <div v-if="event.schedule == 'MONTHLY'" class="pi pi-replay">
      <i style="padding-left: 4px">Every month</i>
      <Button label="Edit single event" severity="secondary" variant="link" @click="editSingleEvent"
        v-if="isPartOfGroup" />
    </div>
    <div v-if="event.parentEventId">
      <RouterLink
        :to="{ name: 'groupGamingEvent', params: { gamingEventId: event.parentEventId, gameGroupId: gameGroupId }, query: { startTime: event.start } }">
        To recurring event</RouterLink>

    </div>
    <div>
      {{ event.description }}
    </div>
    <Button label="Edit" severity="secondary" @click="edit" v-if="isPartOfGroup" />
    <h2>Participants:</h2>

    <div v-for="(participant, index) in event.participants" :key="index" class="participant">
      {{ participant.participant.name }}
      <span class="participant-buttons" v-if="isMe(participant.participant.id)">
        <Message v-if="errorMessage" severity="error">{{ errorMessage }}</Message>
        <Button :disabled="participant.participationStatus == 'CONFIRMED'" label="Confirm"
          @click="onConfirmParticipation(participant.participant.id)" />
        <Button :disabled="participant.participationStatus == 'DECLINED'" label="Decline" severity="warn"
          @click="onDeclineParticipation(participant.participant.id)" />
        <Button :disabled="participant.participationStatus == 'MAYBE'" label="Maybe" severity="secondary"
          @click="onMaybeParticipation(participant.participant.id)" />
      </span>
      <span :class="participicationStatusClass(participant.participationStatus)" style="margin-right: 8px" />
    </div>
    <Button label="Invite missing players" severity="secondary" @click="addMissingPlayers" style="margin-top: 16px"
      v-if="isPartOfGroup" />

    <h2>Games:</h2>


    <div v-for="(game, index) in event.games" :key="index" style="margin: 8px">
      <GameEventGameCard :game="game.game" :game-group-id="gameGroupId" :gameStatus="game.gameStatus"
        :gameComment="game.comment" :withRateButton="true" :with-tag-button="true"
        :players="event.participants.map((p) => p.participant)" :gaming-event-id="gamingEventId"
        :is-part-of-group="isPartOfGroup" @game-removed="onGameRemoved" />
    </div>

    <h2 class="green" v-if="isPartOfGroup">Add game</h2>
    <AddGameToGroupComponent @game-added="onGameAdded" ref="addGameComponent" style="margin-bottom: 16px"
      v-if="isPartOfGroup" />

    <Button severity="danger" label="Delete event" @click="onDeleteEvent" style="margin-top: 16px"
      v-if="isPartOfGroup" />

    <Dialog v-model:visible="editEventDialogVisible" modal header="Edit event">
      <EditGamingEventComponent :game-group-id="gameGroupId" :gaming-event-id="gamingEventId"
        @event-updated="onEventUpdated" />
    </Dialog>
  </div>
</template>

<script lang="ts" setup>
import GameEventGameCard from '@/components/GamingEvents/GameEventGameCard.vue'
import type { GamingEvent, ParticipationStatus } from '@/model/GamingEvent'
import {
  addAllGroupMembersToGamingEvent,
  addGameToEvent,
  cloneEvent,
  deleteEvent,
  fetchGamingEvent,
  updateEvent,
  updateParticipationStatus
} from '@/services/api/GamingEventsApiService'
import { onMounted, ref, type Ref } from 'vue'
import { onBeforeRouteUpdate, useRoute, type LocationQuery } from 'vue-router'
import 'primeicons/primeicons.css'
import { Button, Dialog, Message } from 'primevue'
import { getCurrentPlayerId } from '@/services/LoginService'
import type { Game } from '@/model/Game'
import AddGameToGroupComponent from '@/components/Game/AddGameToGroupComponent.vue'
import router from '@/router'
import { addGameToGroup, fetchPlayersInGroup } from '@/services/api/GameGroupApiService'
import EditGamingEventComponent from '@/components/GamingEvents/EditGamingEventComponent.vue'

const route = useRoute()

let gameGroupId: number
let gamingEventId: number
let startTime: Date | null = null
const errorMessage = ref('')
const event: Ref<GamingEvent | null> = ref(null)
const isPartOfGroup = ref(false)
const editEventDialogVisible = ref(false)

onBeforeRouteUpdate(async (to, from, next) => {
  gameGroupId = Number(to.params.gameGroupId)
  gamingEventId = Number(to.params.gamingEventId)
  startTime = parse(to.query)
  event.value = null
  await loadData()
  next()
})

onMounted(async () => {
  gameGroupId = Number(route.params.gameGroupId)
  gamingEventId = Number(route.params.gamingEventId)
  startTime = parse(route.query)
  await loadData()
})

function edit() {
  editEventDialogVisible.value = true
}

function parse(params: LocationQuery): Date | null {
  if (params.startTime) {
    return new Date(parseInt(params.startTime as string))
  }
  return null
}

function onEventUpdated(newEvent: GamingEvent) {
  event.value = newEvent
  editEventDialogVisible.value = false
}

async function loadData() {
  const result = await fetchGamingEvent(gamingEventId)
  if (result.success) {
    event.value = result.success
    if (!startTime) {
      startTime = new Date(event.value.start)
    }
  } else {
    console.error('Error when loading event ' + gamingEventId)
  }

  const playersResult = await fetchPlayersInGroup(gameGroupId)
  if (playersResult.success) {
    isPartOfGroup.value = playersResult.success.some((player) => player.id === getCurrentPlayerId())
  } else {
    errorMessage.value = playersResult.error?.detail || 'Error fetching players in group'
  }
}

function formatDate(date: Date) {
  return date.toLocaleDateString(undefined, { day: 'numeric', month: 'long', weekday: 'long' })
}

function participicationStatusClass(participant: ParticipationStatus) {
  switch (participant) {
    case 'CONFIRMED':
      return 'pi pi-check accepted'
    case 'DECLINED':
      return 'pi pi-times declined'
    case 'MAYBE':
      return 'pi pi-question-circle maybe'
    case 'NOT_RESPONDED':
      return 'pi pi-question-circle not-responded'
    default:
      return ''
  }
}

async function onConfirmParticipation(participantId: number) {
  await updateStatus(participantId, 'CONFIRMED')
}

async function onDeclineParticipation(participantId: number) {
  await updateStatus(participantId, 'DECLINED')
}

async function onMaybeParticipation(participantId: number) {
  await updateStatus(participantId, 'MAYBE')
}

async function updateStatus(participantId: number, status: ParticipationStatus) {
  errorMessage.value = ''
  const result = await updateParticipationStatus(gamingEventId, participantId, 'CONFIRMED')
  if (result.error) {
    errorMessage.value = result.error.detail ?? 'Error'
  } else {
    event.value?.participants
      .filter((participant) => participant.participant.id === participantId)
      .forEach((participant) => {
        participant.participationStatus = status
      })
  }
}

async function addMissingPlayers() {
  const result = await addAllGroupMembersToGamingEvent(gamingEventId)
  if (result.success) {
    event.value = result.success
  } else {
    errorMessage.value = result.error?.detail ?? 'Error'
  }
}

function isMe(playerId: number): boolean {
  return getCurrentPlayerId() == playerId
}

async function onGameAdded(game: Game) {
  const response = await addGameToGroup(gameGroupId, game.id!)
  if (response.error) {
    console.log("Cannot add game to group, already part of group. Continuing...")
  }
  await addGameToEvent(gamingEventId, game.id!)
  event.value?.games.push({
    game,
    gameStatus: 'SUGGESTED',
    comment: null
  })
}

function onGameRemoved(game: Game) {
  event.value?.games.splice(
    event.value?.games.findIndex((g) => g.game.id !== game.id),
    1
  )
}

async function editSingleEvent() {
  const result = await cloneEvent(gameGroupId, gamingEventId)
  if (result.success) {
    const newStartTime = startTime ?? new Date()
    const updateResult = await updateEvent(
      gameGroupId,
      result.success.id,
      newStartTime,
      'ONCE',
      event.value!.description,
      gamingEventId
    )
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
      })
    } else {
      errorMessage.value = updateResult.error?.detail ?? 'Error'
    }
  } else {
    errorMessage.value = result.error?.detail ?? 'Error'
  }
}

async function onDeleteEvent() {
  const result = await deleteEvent(gamingEventId)
  if (result.success) {
    router.push({
      name: 'calendarOverview'
    })
  } else {
    errorMessage.value = result.error?.detail ?? 'Error'
  }
}

function onBackToEvents() {
  router.push({
    name: 'calendarOverview',
    params: {
      gameGroupId: gameGroupId
    },
    query: {
      startTime: startTime && startOfMonth(startTime).getTime().toString()
    }
  })
}

function startOfMonth(date: Date): Date {
  date.setDate(1)
  date.setHours(0, 0, 0, 0)
  return date
}
</script>

<style lang="css" scoped>
.confirmed {
  color: green;
}

.declined {
  color: red;
}

.maybe {
  color: orange;
}

.not-responded {
  color: gray;
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
  gap: 4px;
}
</style>
