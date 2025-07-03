<script setup lang="ts">
import InputText from 'primevue/inputtext'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import { ref, type Ref } from 'vue'
import { addGame, fetchFromBgg, searchBgg } from '@/services/ApiService'
import type { BggSearchItem } from '@/model/BggSearchItem'
import type { BggFetchItem } from '@/model/BggFetchItem'
import Image from 'primevue/image'

const emit = defineEmits(['game-added'])

const searchTerm: Ref<String> = ref('')
const bggSearchItems: Ref<BggSearchItem[]> = ref([])

async function onClickSearch() {
  bggSearchItems.value = await searchBgg(searchTerm.value)
}

async function onClickAdd(bggId: number) {
  const fetchedGame = await fetchFromBgg(bggId)
  switch (fetchedGame) {
    case null:
      console.log('Game not found ' + bggId)
      break
    default: {
      const fetchGameFound = fetchedGame as BggFetchItem
      const addedGame = await addGame({
        name: fetchGameFound.name,
        description: fetchGameFound.description,
        thumbnailUrl: fetchGameFound.thumbnailUrl,
        imageUrl: fetchGameFound.imageUrl,
        id: undefined
      })
      emit('game-added', addedGame)
      break
    }
  }
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

    <DataTable
      :value="bggSearchItems"
      tableStyle="min-width: 50rem"
      scrollable
      scrollHeight="400px"
    >
      <Column field="name" header="Name"></Column>
      <Column header="Actions">
        <template #body="slotProps">
          <Button severity="secondary" @click="onClickAdd(slotProps.data.id)"> Add to list </Button>
        </template>
      </Column>
    </DataTable>
  </div>
</template>

<style scoped></style>
