<template>
  <div>
    <h1>Manage gamegroups</h1>
    <DataTable :value="gameGroups" tableStyle="min-width: 50rem">
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
import { deleteGameGroup, fetchGameGroups } from '@/services/ApiService'
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import type { Game } from '@/model/Game'
import type { GameGroup } from '@/model/GameGroup'

const gameGroups: Ref<Game[]> = ref([] as GameGroup[])

onMounted(async () => {
  gameGroups.value = await fetchGameGroups()
})

function onClickDelete(id: Number) {
  deleteGameGroup(id)
  gameGroups.value = gameGroups.value.filter((item) => !(item.id === id))
}

function onGameGroupAdded(gameGroup: GameGroup) {
  gameGroups.value.push(gameGroup)
}
</script>

<script lang="ts"></script>
