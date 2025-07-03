<template>
  <div>
    <h1>Players in group {{ gameGroup.name }}</h1>
    <DataTable :value="players" tableStyle="min-width: 50rem">
      <Column field="name" header="Name"></Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button @click="onClickRemovePlayer(slotProps.data.id)" severity="danger">
            Remove player from group
          </Button>
        </template>
      </Column>
    </DataTable>
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
import {
  fetchPlayersInGroup,
  loadGameGroup,
  removePlayerFromGroup
} from '@/services/api/ApiService'
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import { GameGroup } from '@/model/GameGroup'
import { useRoute } from 'vue-router'
import type { Player } from '@/model/Player/Player'

const gameGroup: Ref<GameGroup> = ref(new GameGroup(-1, ''))
const players: Ref<Player[]> = ref([])
const route = useRoute()
const gameGroupId = Number(route.params.gameGroupId)

onMounted(async () => {
  gameGroup.value = await loadGameGroup(gameGroupId)
  players.value = await fetchPlayersInGroup(gameGroupId)
})

function onClickRemovePlayer(playerId: Number) {
  removePlayerFromGroup(gameGroupId, playerId)
  players.value = players.value.filter((item) => !(item.id === playerId))
}
</script>

<script lang="ts"></script>
