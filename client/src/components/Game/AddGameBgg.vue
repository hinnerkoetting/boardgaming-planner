<script setup lang="ts">
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { ref, type Ref } from 'vue'
import { fetchFromBgg, searchBgg } from '@/services/api/ApiService'
import type { BggSearchItem } from '@/model/BggSearchItem'
import type { BggFetchItem } from '@/model/BggFetchItem'
import DataView from 'primevue/dataview'
import { addGame } from '@/services/api/GameApiService'

const emit = defineEmits(['game-added'])

const searchTerm: Ref<String> = ref('')
const bggSearchItems: Ref<BggSearchItem[]> = ref([])

async function onClickSearch() {
  const response = await searchBgg(searchTerm.value)
  if (response.success) {
    bggSearchItems.value = response.success
  } else {
    console.log(response.error)
  }
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

    <DataView :value="bggSearchItems">
      <template #list="slotProps">
        <div v-for="(item, index) in slotProps.items" :key="index">
          <div class="row">
            <div>
              {{ item.name }}
            </div>
            <Button @click="onClickAdd(item.id)"> Add </Button>
          </div>
        </div>
      </template>
    </DataView>
  </div>
</template>

<style scoped>
.row {
  border-color: #e2e8f0;
  border-style: solid;
  border-width: 1px 0 1px 0;
  padding: 8px;
  font-size: 15px;
  line-height: 1.6;
  padding-left: 16px;
  display: flex;
  justify-content: space-between;
  vertical-align: middle;
  align-items: center;
}
</style>
