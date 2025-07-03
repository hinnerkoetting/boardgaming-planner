<script setup lang="ts">
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { ref, type Ref } from 'vue'
import { searchExistingGames } from '@/services/api/ApiService'
import type { Game } from '@/model/Game'
import AddExistingGameTable from './AddExistingGameTable.vue'
import type { EventMessage } from '@/model/internal/EventMessage'

const emit = defineEmits<{
  (e: 'game-added', game: Game, callback: (message: EventMessage) => void): void
}>()

const searchTerm: Ref<string> = ref('')
const searchItems: Ref<Game[]> = ref([])
async function onClickSearch() {
  searchItems.value = await searchExistingGames(searchTerm.value)
}

async function onClickAdd(game: Game, callback: (message: EventMessage) => void) {
  emit('game-added', game, callback)
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
    <Button @click="onClickSearch" class="searchButton">Search</Button>

    <AddExistingGameTable :searchItems="searchItems" v-on:game-added="onClickAdd" />
  </div>
</template>

<style scoped lang="css">
.row {
  border-color: #e2e8f0;
  border-style: solid;
  border-width: 1px 0 1px 0;
  padding: 8px;
  font-size: 15px;
  line-height: 1.6;
  padding-left: 16px;
  display: flex;
  vertical-align: middle;
  align-items: center;
  justify-content: space-between;
}

.searchButton {
  margin-left: 4px;
}
</style>
