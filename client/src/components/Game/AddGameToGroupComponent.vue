<script setup lang="ts">
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { ref, type Ref } from 'vue'
import { searchExistingGames } from '@/services/api/ApiService'
import type { Game } from '@/model/Game'
import AddExistingGameTable from './AddExistingGameTable.vue'
import type { EventMessage } from '@/model/internal/EventMessage'
import AddBggGameTable from './AddBggGameTable.vue'
import type { BggSearchItem } from '@/model/BggSearchItem'
import { searchBgg } from '@/services/api/BggApiService'

const emit = defineEmits<{
  (e: 'game-added', game: Game, callback: (message: EventMessage) => void): void
}>()

const searchTerm: Ref<string> = ref('')
const searchItems: Ref<Game[]> = ref([])
const someGamesFound = ref(false)

const bggSearchItems: Ref<BggSearchItem[]> = ref([])
const bggEmptyTableMessage = ref('')

async function onClickSearchBgg() {
  searchFromBgg()
}

async function searchFromBgg() {
  reset()
  const bggResponse = await searchBgg(searchTerm.value)
  if (bggResponse.success) {
    bggSearchItems.value = bggResponse.success
    if (bggSearchItems.value.length === 0) {
      bggEmptyTableMessage.value = 'Nothing found on BGG'
    }
  } else {
    bggEmptyTableMessage.value = 'Error when loading from BGG: ' + bggResponse.error?.detail
  }
}

async function onClickSearch() {
  reset()
  if (!searchTerm.value) {
    return
  }
  searchItems.value = await searchExistingGames(searchTerm.value)
  someGamesFound.value = searchItems.value.length > 0
  if (searchItems.value.length === 0) {
    searchFromBgg()
  }
}

async function onClickAdd(game: Game, callback: (message: EventMessage) => void) {
  emit('game-added', game, callback)
}

function reset() {
  bggEmptyTableMessage.value = ''
  bggSearchItems.value = []
  searchItems.value = []
  someGamesFound.value = false
}

defineExpose({
  reset
})
</script>

<template>
  <div>
    <InputText id="searchTerm" name="searchTerm" v-model="searchTerm" placeholder="Searchterm..."
      v-on:keyup.enter="onClickSearch" @focus="($event.target as any).select()" size="small"></InputText>
    <Button @click="onClickSearch" class="searchButton">Search <img
        src="https://cf.geekdo-images.com/HZy35cmzmmyV9BarSuk6ug__small/img/gbE7sulIurZE_Tx8EQJXnZSKI6w=/fit-in/200x150/filters:strip_icc()/pic7779581.png"
        height="28px" /></Button>


    <AddExistingGameTable :searchItems="searchItems" @game-added="onClickAdd" />
    <template v-if="someGamesFound">
      Did not find what you were looking for?
      <Button @click="onClickSearchBgg" class="searchButton">Search on BGG</Button>
    </template>
    <AddBggGameTable :searchItems="bggSearchItems" @game-added="onClickAdd" :emptyTableMessage="bggEmptyTableMessage" />
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
  padding-left: 8px;
  display: flex;
  vertical-align: middle;
  align-items: center;
  justify-content: space-between;
}

.searchButton {
  margin-left: 4px;
}
</style>
