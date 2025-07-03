<template>
  <div>
    <h1>Manage players</h1>
    <DataTable :value="players" tableStyle="min-width: 50rem">
      <Column field="name" header="Name"></Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button @click="onClickDelete(slotProps.data.id)" severity="danger"> Delete </Button>
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

import { deletePlayer, fetchPlayers } from '@/services/ApiService'
import { onMounted } from 'vue'
import { ref } from 'vue'
import type { Player } from '@/model/Player'

var players = ref([] as Player[])

onMounted(async () => {
  players.value = await fetchPlayers()
})

function onClickDelete(id: Number) {
  deletePlayer(id)
  players.value = players.value.filter((item) => !(item.id === id))
}
</script>
