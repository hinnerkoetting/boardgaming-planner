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
          <div class="actionButtons">
            <Button @click="onClickEdit(slotProps.data)" severity="info"> Edit </Button>
            <Button @click="onClickTags(slotProps.data)" severity="info"> Tags </Button>
            <Button @click="onClickDelete(slotProps.data)" severity="danger"> Delete </Button>
          </div>
        </template>
      </Column>
    </DataTable>
    <Dialog v-model:visible="showEditDialog" model :header="'Edit ' + editingGame?.name">
      <EditGameComponent mode="EDIT" :game="editingGame" @game-added="onGameSaved" />
    </Dialog>
    <Dialog v-model:visible="showTagsDialog" model :header="'Edit tags for ' + editingGame?.name">
      <EditTagsInGameComponent
        :game="editingGame!"
        :allTags="tags"
        :game-tags="editingGame!.globalTags"
      />
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { AdminGame } from '@/model/Game'
import Button from 'primevue/button'
import Column from 'primevue/column'
import DataTable from 'primevue/datatable'
import Image from 'primevue/image'
import { onMounted, ref, type PropType, type Ref } from 'vue'
import EditGameComponent from './EditGameComponent.vue'
import Dialog from 'primevue/dialog'
import EditTagsInGameComponent from './EditTagsInGameComponent.vue'
import type { TagModel } from '@/model/TagModel'
import { fetchTags } from '@/services/api/TagApiService'

defineProps({
  games: Array as PropType<Array<AdminGame>>,
  default: () => []
})

const showEditDialog = ref(false)
const showTagsDialog = ref(false)
const editingGame: Ref<AdminGame | undefined> = ref(undefined)
const tags: Ref<TagModel[]> = ref([])

const emit = defineEmits<{
  (e: 'clickDelete', game: AdminGame): void
  (e: 'game-edited', game: AdminGame): void
}>()

onMounted(async () => {
  tags.value = await fetchTags()
})

function onClickEdit(game: AdminGame) {
  editingGame.value = game
  showEditDialog.value = true
}

function onClickTags(game: AdminGame) {
  editingGame.value = game
  showTagsDialog.value = true
}

function onGameSaved(game: AdminGame) {
  editingGame.value = undefined
  showEditDialog.value = false
  emit('game-edited', game)
}

function onClickDelete(game: AdminGame) {
  emit('clickDelete', game)
}
</script>

<style scoped>
.actionButtons {
  display: flex;
  gap: 4px;
}
</style>
