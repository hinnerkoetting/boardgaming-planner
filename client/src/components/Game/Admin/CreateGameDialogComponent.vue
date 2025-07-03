<template>
  <div>
    <Button @click="onClickCreateGame"> Create game</Button>
    <Dialog v-model:visible="showDialog" modal header="Create game"
      ><div>
        <AddGameManual @game-added="onGameAdded" mode="CREATE" />
      </div>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import Button from 'primevue/button'
import Dialog from 'primevue/dialog'
import { ref } from 'vue'
import AddGameManual from './EditGameComponent.vue'
import { Game } from '@/model/Game'

const emit = defineEmits(['game-added'])

const showDialog = ref(false)

function onClickCreateGame() {
  showDialog.value = true
}
function onGameAdded(game: Game) {
  emit('game-added', game)
  showDialog.value = false
}
</script>
