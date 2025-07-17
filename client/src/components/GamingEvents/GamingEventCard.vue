<template>
  <Card
    @click="onClickCard"
    :class="{ clickable_card: true, past: event.start < new Date().getTime() }"
  >
    <template #title>
      <div class="title">
        {{ formatDate(new Date(event.start)) }}
      </div>
    </template>
    <template #content>
      <div v-if="event.schedule !== 'ONCE'">
        <i>{{ scheduleDescription(event.schedule) }}</i>
      </div>
      <div>
        {{
          event.participants
            .map((participation: Participation) => participation.participant.name)
            .join('&#183;')
        }}
      </div>
      <div>
        {{ event.games.map((game: EventGame) => game.game.name).join('&#183;') }}
      </div>
    </template>
  </Card>
</template>

<script setup lang="ts">
import type { EventGame, GamingEvent, Participation, Schedule } from '@/model/GamingEvent'
import router from '@/router'
import { Card } from 'primevue'
import type { PropType } from 'vue'

const props = defineProps({
  event: {
    type: Object as PropType<GamingEvent>,
    required: true
  },
  gameGroupId: {
    type: Number,
    required: true
  }
})

function formatDate(date: Date) {
  return date.toLocaleDateString(undefined, { day: 'numeric', month: 'long', weekday: 'long' })
}

function onClickCard() {
  router.push({
    name: 'groupGamingEvent',
    params: {
      gamingEventId: props.event.id,
      gameGroupId: props.gameGroupId
    },
    query: {
      startTime: props.event.start.toString()
    }
  })
}

function scheduleDescription(schedule: Schedule): string {
  switch (schedule) {
    case 'WEEKLY':
      return 'weekly'
    case 'MONTHLY':
      return 'monthly'
    default:
      return ''
  }
}
</script>

<style lang="css" scoped>
.clickable_card {
  cursor: pointer;
  height: 100%;
}

.past {
  --p-card-background: lightgray;
}
</style>
