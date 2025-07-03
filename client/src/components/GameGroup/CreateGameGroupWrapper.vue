<template>
  <div>
    <Dialog v-model:visible="dialogVisible" header="Create group">
      <AddGameGroupDialogContent @game-group-added="onGameGroupAdded" />
    </Dialog>
    <Button @click="onClickOpenDialog" class="createButton">Create group</Button>
  </div>
</template>

<script setup lang="ts">
import type { GameGroup } from '@/model/GameGroup'
import Button from 'primevue/button'
import Dialog from 'primevue/dialog'
import { ref } from 'vue'
import AddGameGroupDialogContent from './AddGameGroupDialogContent.vue'

const dialogVisible = ref(false)

function onClickOpenDialog() {
  dialogVisible.value = true
}

const emit = defineEmits(['game-group-added'])

function onGameGroupAdded(gameGroup: GameGroup) {
  dialogVisible.value = false
  emit('game-group-added', gameGroup)
}
</script>

<style lang="css" scoped>
.createButton {
  margin-top: 16px;
}
</style>
