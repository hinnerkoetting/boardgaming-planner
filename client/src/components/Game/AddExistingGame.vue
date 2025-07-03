<script setup lang="ts">
import InputText from 'primevue/inputtext'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import { ref, type Ref } from 'vue'
import { addGame, searchExistingGames } from '@/services/api/ApiService'
import type { Game } from '@/model/Game'
import Image from 'primevue/image'

const emit = defineEmits(['game-added'])

const searchTerm: Ref<string> = ref('')
const searchItems: Ref<Game[]> = ref([])

async function onClickSearch() {
  searchItems.value = await searchExistingGames(searchTerm.value)
}

async function onClickAdd(game: Game) {
  emit('game-added', game)
}
</script>

<template>
  <div>
    <InputText
      id="searchTerm"
      name="searchTerm"
      v-model="searchTerm"
      placeholder="Searchterm..."
      v-on:keyup.enter="onClickSearch"
    ></InputText>
    <Button @click="onClickSearch">Search</Button>

    <DataTable :value="searchItems" tableStyle="min-width: 50rem" scrollable scrollHeight="400px">
      <Column field="name" header="Name"></Column>
      <Column field="thumbnailUrl" header="Thumbnail">
        <template #body="slotProps">
          <Image :src="slotProps.data.thumbnailUrl" />
        </template>
      </Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button severity="secondary" @click="onClickAdd(slotProps.data)"> Add to list </Button>
        </template>
      </Column>
    </DataTable>
  </div>
</template>

<style scoped></style>
