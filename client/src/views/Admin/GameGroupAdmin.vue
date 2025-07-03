<template>
  <div>
    <h1>Manage gaming group</h1>
    <DataTable :value="gameGroups" tableStyle="min-width: 50rem">
      <Column field="name" header="Name"></Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button severity="secondary">
            <RouterLink :to="'/admin/gameGroups/' + slotProps.data.id">Manage players</RouterLink>
          </Button>
          <Button @click="onClickDelete(slotProps.data.id)" severity="danger">
            Delete group
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
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import type { GameGroup } from '@/model/GameGroup'
import { deleteGameGroup, fetchGameGroups } from '@/services/api/GameGroupApiService'

const gameGroups: Ref<GameGroup[]> = ref([] as GameGroup[])

onMounted(async () => {
  gameGroups.value = await fetchGameGroups()
})

function onClickDelete(id: Number) {
  deleteGameGroup(id)
  gameGroups.value = gameGroups.value.filter((item) => !(item.id === id))
}
</script>

<script lang="ts"></script>
