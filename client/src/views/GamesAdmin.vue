<template>
  <div>
    <h1>Manage games</h1>
    <DataTable :value="games" tableStyle="min-width: 50rem">
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
import { deleteGame, fetchGames } from '@/services/ApiService'
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import type { Game } from '@/model/Game'

const games: Ref<Game[]> = ref([] as Game[])

onMounted(async () => {
  games.value = await fetchGames()
})

function onClickDelete(id: Number) {
  deleteGame(id)
  games.value = games.value.filter((item) => !(item.id === id))
}
</script>

<script lang="ts"></script>
