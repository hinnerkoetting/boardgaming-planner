<script setup lang="ts">
import { Game } from '@/model/Game'
import Button from 'primevue/button'
import Column from 'primevue/column'
import DataTable from 'primevue/datatable'
import Image from 'primevue/image'
import type { PropType } from 'vue'

const props = defineProps({
  games: Array as PropType<Array<Game>>,
  default: () => []
})

const emit = defineEmits(['delete'])

function onClickDelete(id: Number) {
  emit('delete', id)
}
</script>

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
          <Button @click="onClickDelete(slotProps.data.id)" severity="danger"> Delete </Button>
        </template>
      </Column>
    </DataTable>
  </div>
</template>

<style scoped></style>
