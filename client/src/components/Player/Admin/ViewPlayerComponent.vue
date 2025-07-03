<template>
  <div>
    <h2>Roles</h2>
    <div v-for="role in playerDetails?.roles" :key="role">{{ role }}</div>
    <Message severity="error" v-if="errorMessage">{{ errorMessage }}</Message>
    <Button
      v-if="playerDetails?.roles.includes('ROLE_ADMIN')"
      severity="danger"
      @click="onClickDemote"
      >Demote admin</Button
    >
    <Button
      v-if="!playerDetails?.roles.includes('ROLE_ADMIN')"
      severity="info"
      @click="onClickPromote"
      >Promote admin</Button
    >
  </div>
</template>

<script setup lang="ts">
import { Role } from '@/model/api/JwtPayload'
import { Player } from '@/model/Player/Player'
import type { PlayerDetails } from '@/model/Player/PlayerDetails'
import { fetchPlayerDetails } from '@/services/api/PlayerApiService'
import { addRoleToPlayer, removeRoleFromPlayer } from '@/services/api/RolesService'
import Button from 'primevue/button'
import Message from 'primevue/message'

import { ref, type Ref } from 'vue'

const errorMessage = ref('')

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

async function onClickDemote() {
  const response = await removeRoleFromPlayer(playerDetails!.value!.id, Role.ADMIN)
  if (response.success) {
    emit('player-update')
  } else {
    errorMessage.value = response.error?.detail || 'Error'
  }
}

async function onClickPromote() {
  const response = await addRoleToPlayer(playerDetails!.value!.id, Role.ADMIN)
  if (response.success) {
    emit('player-update')
  } else {
    errorMessage.value = response.error?.detail || 'Error'
  }
}
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
