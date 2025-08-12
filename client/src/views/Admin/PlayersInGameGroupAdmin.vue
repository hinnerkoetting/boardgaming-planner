<template>
  <div>
    <h1>Players in group {{ gameGroup?.name }}</h1>
    <DataTable :value="players" tableStyle="min-width: 50rem">
      <Column field="name" header="Name"></Column>
      <Column header="Type">
        <template #body="slotProps">
          {{ memberTypeToString(slotProps.data.type) }}
          <Button @click="editType(slotProps.data)" severity="info">
            Edit
          </Button>
        </template>
      </Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button @click="onClickRemovePlayer(slotProps.data.id)" severity="danger">
            Remove player
          </Button>
        </template>
      </Column>
    </DataTable>
    <Message v-model="errorMessage" v-if="errorMessage" severity="error" />
    <Dialog v-model:visible="dialogVisible">
      <EditMembershipType :membership="selectedPlayer!" :gameGroupId="Number(gameGroup?.id)"
        @type-changed="onTypeChanged" />
    </Dialog>
  </div>
</template>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>

<script setup lang="ts">
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import { GameGroup } from '@/model/GameGroup'
import { useRoute } from 'vue-router'
import {
  fetchPlayersInGroup,
  loadGameGroup,
  removePlayerFromGroup
} from '@/services/api/GameGroupApiService'
import { GameGroupMemberType, memberTypeToString, type GameGroupMember } from '@/model/GameGroupMember'
import { Dialog, Message } from 'primevue'
import EditMembershipType from '../GameGroup/EditMembershipType.vue'

const gameGroup: Ref<GameGroup | null> = ref(null)
const players: Ref<GameGroupMember[]> = ref([])
const route = useRoute()
const gameGroupId = Number(route.params.gameGroupId)
const errorMessage = ref('')
const dialogVisible = ref(false)
const selectedPlayer = ref<GameGroupMember | null>(null)

onMounted(async () => {
  gameGroup.value = await loadGameGroup(gameGroupId)
  const playersResult = await fetchPlayersInGroup(gameGroupId)
  if (!playersResult.success) {
    errorMessage.value = playersResult.error?.detail || 'Error fetching players in group';
    return
  } else {
    players.value = playersResult.success
  }
})

function onClickRemovePlayer(playerId: number) {
  removePlayerFromGroup(gameGroupId, playerId)
  players.value = players.value.filter((item) => !(item.id === playerId))
}

function editType(player: GameGroupMember) {
  selectedPlayer.value = player
  dialogVisible.value = true
}

function onTypeChanged(newValue: GameGroupMemberType) {
  dialogVisible.value = false
  selectedPlayer.value!.type = newValue
}
</script>

<script lang="ts"></script>