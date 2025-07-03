<template>
  <div>
    <h1>Gaming group</h1>
    <DataTable
      :value="gameGroups"
      tableStyle="min-width: 50rem"
      @row-click="onRowClick($event)"
      selectionMode="single"
    >
      <Column field="name" header="Name"></Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button @click="onClickJoinGroup(slotProps.data)"> Join </Button>
        </template>
      </Column>
    </DataTable>
    <AddGameGroup @game-group-added="onGameGroupAdded" />
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
import { getCurrentPlayerId } from '@/services/LoginService'
import AddGameGroup from '@/components/GameGroup/AddGameGroup.vue'
import router from '@/router'

const gameGroups: Ref<GameGroup[]> = ref([] as GameGroup[])

onMounted(async () => {
  gameGroups.value = await fetchGameGroups()
})

function onClickJoinGroup(gameGroup: GameGroup) {
  if (!gameGroup.id) {
    console.error('Gamegroup has no id')
    return
  }
  addPlayerToGroup(gameGroup.id, getCurrentPlayerId())
  openGroup(gameGroup.id)
}

function onGameGroupAdded(gameGroup: GameGroup) {
  gameGroups.value.push(gameGroup)
}

function onRowClick(event: any) {
  const gameGroup = gameGroups.value[event.index]
  const gameGroupId = gameGroup.id
  if (!gameGroupId) {
    console.error('Gamegroup has no id')
    return
  }
  openGroup(gameGroupId)
}

function openGroup(gameGroupId: number) {
  router.push({
    name: 'gameGroup',
    params: {
      gameGroupId: gameGroupId
    }
  })
}
</script>

<script lang="ts"></script>
