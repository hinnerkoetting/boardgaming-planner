<template>
  <div class="wrapper">
    <div class="pageContent">
      <h1>Groups</h1>
      <GameGroupCollection
        :game-groups="gameGroups"
        :with-join-button="true"
        @onRowClick="onRowClick"
        @onClickJoinGroup="onClickJoinGroup"
      />

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
import { addPlayerToGroup, fetchGameGroups } from '@/services/api/GameGroupApiService'
import GameGroupCollection from '@/components/GameGroup/GameGroupCollection.vue'

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
</style>
