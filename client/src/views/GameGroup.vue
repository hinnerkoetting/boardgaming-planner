<template>
  <div>
    <h1>Gaming group</h1>
    <DataTable :value="gameGroups" tableStyle="min-width: 50rem">
      <Column field="name" header="Name"></Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button @click="onClickJoinGroup(slotProps.data)"> Join </Button>
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
import { addPlayerToGroup, fetchGameGroups } from '@/services/ApiService'
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import type { GameGroup } from '@/model/GameGroup'
import { getCurrentUserId } from '@/services/LoginService'

const gameGroups: Ref<GameGroup[]> = ref([] as GameGroup[])

onMounted(async () => {
  gameGroups.value = await fetchGameGroups()
})

function onClickJoinGroup(gameGroup: GameGroup) {
  addPlayerToGroup(gameGroup.id, getCurrentUserId())
}

function onGameGroupAdded(gameGroup: GameGroup) {
  gameGroups.value.push(gameGroup)
}
</script>

<script lang="ts"></script>
