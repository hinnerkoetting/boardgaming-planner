<template>
  <Card @click="onClickCard" :class="cardClasses()">
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
        {{event.games.map((game: EventGame) => game.game.name).join('&#183;')}}
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

function cardClasses(): any {
  // is same day as today
  const eventDate = new Date(props.event.start)
  if (eventDate.getDate() === new Date().getDate() &&
    eventDate.getMonth() === new Date().getMonth() &&
    eventDate.getFullYear() === new Date().getFullYear()) {
    return {
      clickable_card: true,
      today: true
    }
  }
  return {
    clickable_card: true,
    past: props.event.start < new Date().getTime()
  }


}
</script>

<style lang="css" scoped>
.clickable_card {
  cursor: pointer;
  height: 100%;
}

.past {
  --p-card-background: var(--p-gray-200)
}

.today {
  --p-card-background: var(--p-blue-100)
}
</style>
