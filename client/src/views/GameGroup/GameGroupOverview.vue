<template>
  <div class="wrapper">
    <div class="pageContent">
      <h1>My groups</h1>
      <GameGroupCollection
        :game-groups="myGameGroups"
        :with-join-button="false"
        action-button-text="Open"
        @onRowClick="onClickOpenGroup"
        empty-text="Try joining a group or create one below."
        class="gameGroupCollection"
      />

      <h1>Groups <Button @click="onClickLoadButton" severity="secondary">Find others</Button></h1>
      <GameGroupCollection
        :game-groups="gameGroups"
        :with-join-button="true"
        action-button-text="Join"
        @onRowClick="onClickOpenGroup"
        @onClickActionButton="onClickJoinGroup"
        empty-text="No groups found..."
        class="gameGroupCollection"
      />
      <Message v-if="errorMessage" severity="error">{{ errorMessage }}</Message>

      <CreateGameGroupWrapper @game-group-added="onGameGroupAdded" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, type Ref } from 'vue'
import { ref } from 'vue'
import type { GameGroup } from '@/model/GameGroup'
import { getCurrentPlayerId } from '@/services/LoginService'
import router from '@/router'
import EventBus from '@/services/EventBus'
import CreateGameGroupWrapper from '@/components/GameGroup/CreateGameGroupWrapper.vue'
import {
  addPlayerToGroup,
  fetchGameGroups,
  fetchMyGameGroups
} from '@/services/api/GameGroupApiService'
import GameGroupCollection from '@/components/GameGroup/GameGroupCollection.vue'
import Button from 'primevue/button'
import { Message } from 'primevue'

const myGameGroups: Ref<GameGroup[]> = ref([] as GameGroup[])
const gameGroups: Ref<GameGroup[]> = ref([] as GameGroup[])
const errorMessage: Ref<string> = ref('')

onMounted(async () => {
  errorMessage.value = '';
  const result = await fetchMyGameGroups()
  if (result.success) {  
    myGameGroups.value = result.success
  } else {
    errorMessage.value = result.error?.detail || 'Error fetching game groups';    
  }  
})

async function onClickLoadButton() {
  errorMessage.value = '';
  const result = await fetchGameGroups();  
  if (result.success) {  
    gameGroups.value = result.success
  } else {
    errorMessage.value = result.error?.detail || 'Error fetching game groups';    
  }  
}

function removeMyGroupsFromOthers(gameGroups: GameGroup[]): GameGroup[] {
  const myGameGroupIds = myGameGroups.value.map((group) => group.id)
  return gameGroups.filter((group) => !myGameGroupIds.includes(group.id))
}

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

function onClickOpenGroup(gameGroup: GameGroup) {
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
.gameGroupCollection {
  --p-dataview-content-background: var(--color-background);
}
.wrapper {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.pageContent {
  width: 100%;
  max-width: 600px;
}

.collection {
  margin-top: 8px;
}

h1 {
  margin-top: 32px;
}
</style>
