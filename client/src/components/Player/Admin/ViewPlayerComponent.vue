<template>
  <div>
    <h2>Roles</h2>
    <div v-for="role in playerDetails?.roles" :key="role">{{ role }}</div>
    <Button v-if="playerDetails?.roles.includes('ROLE_ADMIN')" severity="danger"
      >Demote admin</Button
    >
    <Button v-if="!playerDetails?.roles.includes('ROLE_ADMIN')" severity="info"
      >Promote admin</Button
    >
  </div>
</template>

<script setup lang="ts">
import { Player } from '@/model/Player/Player'
import type { PlayerDetails } from '@/model/Player/PlayerDetails'
import { fetchPlayerDetails } from '@/services/api/PlayerApiService'
import Button from 'primevue/button'

import { ref, type Ref } from 'vue'

const props = defineProps({
  player: {
    type: Player,
    required: true
  }
})

const emit = defineEmits<{
  (e: 'player-update'): void
}>()

const playerDetails: Ref<PlayerDetails | undefined> = ref()
fetchPlayerDetails(props.player.id).then((value) => (playerDetails.value = value))
</script>

<style lang="css" scoped>
#ratingButtons {
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
}

Button {
  margin-top: 4px;
}
</style>
