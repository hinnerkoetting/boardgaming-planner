<template>
  <div>
    <h1>Manage players</h1>
    <DataTable
      :value="players"
      tableStyle="min-width: 50rem"
      @row-click="onClickEdit($event)"
      selectionMode="single"
    >
      <Column field="name" header="Name"></Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button @click="onClickDelete(slotProps.data.id)" severity="danger"> Delete </Button>
        </template>
      </Column>
    </DataTable>
    <Dialog v-model:visible="playerDialogVisible" modal :header="'Player ' + selectedPlayer?.name">
      <ViewPlayerComponent :player="selectedPlayer!" />
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
import type { Player } from '@/model/Player/Player'
import { deletePlayer, fetchPlayers } from '@/services/api/PlayerApiService'
import Dialog from 'primevue/dialog'
import ViewPlayerComponent from '@/components/Player/Admin/ViewPlayerComponent.vue'

const players = ref([] as Player[])
const selectedPlayer: Ref<Player | undefined> = ref()
const playerDialogVisible = ref(false)

onMounted(async () => {
  players.value = await fetchPlayers()
})

function onClickDelete(id: Number) {
  deletePlayer(id)
  players.value = players.value.filter((item) => !(item.id === id))
}

function onClickEdit(event: any) {
  const player = players.value[event.index]
  selectedPlayer.value = player
  playerDialogVisible.value = true
}
</script>
