<script setup lang="ts">
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { ref, type Ref } from 'vue'
import type { BggSearchItem } from '@/model/BggSearchItem'
import DataView from 'primevue/dataview'
import { importFromBgg, searchBgg } from '@/services/api/BggApiService'

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
  const importedGame = await importFromBgg(bggId)
  if (importedGame.success) {
    emit('game-added', importedGame.success)
  } else {
    console.error('Error when importing ' + importedGame.error)
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
