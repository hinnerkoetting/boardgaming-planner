<template>
  <div>
    <DataTable :value="games" tableStyle="min-width: 50rem">
      <Column field="name" header="Name"></Column>
      <Column field="thumbnailUrl" header="Thumbnail">
        <template #body="slotProps">
          <Image :src="slotProps.data.thumbnailUrl" />
        </template>
      </Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button @click="onClickEdit(slotProps.data)" severity="info"> Edit </Button>
          <Button @click="onClickDelete(slotProps.data)" severity="danger"> Delete </Button>
        </template>
      </Column>
    </DataTable>
    <Dialog v-model:visible="showEditDialog" model :header="'Edit ' + editingGame?.name">
      <EditGameComponent mode="EDIT" :game="editingGame" @game-added="onGameSaved" />
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { Game } from '@/model/Game'
import Button from 'primevue/button'
import Column from 'primevue/column'
import DataTable from 'primevue/datatable'
import Image from 'primevue/image'
import { ref, type PropType, type Ref } from 'vue'
import EditGameComponent from './EditGameComponent.vue'
import Dialog from 'primevue/dialog'

defineProps({
  games: Array as PropType<Array<Game>>,
  default: () => []
})

const showEditDialog = ref(false)
const editingGame: Ref<Game | undefined> = ref(undefined)

const emit = defineEmits<{
  (e: 'clickDelete', game: Game): void
  (e: 'game-edited', game: Game): void
}>()

function onClickEdit(game: Game) {
  editingGame.value = game
  showEditDialog.value = true
}

function onGameSaved(game: Game) {
  editingGame.value = undefined
  showEditDialog.value = false
  emit('game-edited', game)
}

function onClickDelete(game: Game) {
  emit('clickDelete', game)
}
</script>

<style scoped></style>
