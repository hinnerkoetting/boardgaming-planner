<template>
  <div>
    <h1>Manage games</h1>
    <DataTable :value="games" tableStyle="min-width: 50rem">
      <Column field="name" header="Name"></Column>
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
import { fetchGames } from '@/services/ApiService'
import { onMounted } from 'vue'
import { ref } from 'vue'
import type { Game } from '@/model/Game'

const games = ref([] as Game[])

onMounted(async () => {
  games.value = await fetchGames()
})
</script>
