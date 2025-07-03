<template>
  <div>
    <h1>Groups</h1>
    <DataView :value="gameGroups">
      <template #list="slotProps">
       
          <div v-for="(item, index) in slotProps.items" :key="index">
            <div class="row" @click="onRowClick(item)">
              {{  item.name }}              
              <Button @click="onClickJoinGroup(item)"> Join </Button>                    
            </div>
          </div>
       
      </template>
    </DataView>

    <AddGameGroup @game-group-added="onGameGroupAdded" class="addGameGroup"/>
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
import Button from 'primevue/button'
import { addPlayerToGroup, fetchGameGroups } from '@/services/api/ApiService'
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import type { GameGroup } from '@/model/GameGroup'
import { getCurrentPlayerId } from '@/services/LoginService'
import AddGameGroup from '@/components/GameGroup/AddGameGroup.vue'
import router from '@/router'
import EventBus from '@/services/EventBus'
import DataView from 'primevue/dataview'

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
  openGroup(gameGroup)
}

function onGameGroupAdded(gameGroup: GameGroup) {
  gameGroups.value.push(gameGroup)
}

function onRowClick(gameGroup: GameGroup) {  
  const gameGroupId = gameGroup.id
  if (!gameGroupId) {
    console.error('Gamegroup has no id')
    return
  }
  openGroup(gameGroup)
}

function openGroup(gameGroup: GameGroup) {
  EventBus.emit('gaming-group-opened', gameGroup)
  router.push({
    name: 'gameGroup',
    params: {
      gameGroupId: gameGroup.id
    }
  })
}
</script>

<style scoped lang="css">

.row {
  border-color: #e2e8f0;
  border-style: solid;
  border-width: 1px 0 1px 0;  
  padding: 8px;
  font-size: 15px;
  line-height: 1.6;
  padding-left: 16px;
  display: flex;
  column-gap: 32px;
  vertical-align: middle;
  align-items: center;  
}

.row:hover {
  background: #f1f5f9
}

button {
  margin-left: 20px;
}

.addGameGroup {
  margin-top: 32px
}
</style>>
