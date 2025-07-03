<script setup lang="ts">
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import { ref, type Ref } from 'vue'
import { searchExistingGames } from '@/services/api/ApiService'
import type { Game } from '@/model/Game'
import Image from 'primevue/image'
import DataView from 'primevue/dataview'

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

    <DataView :value="searchItems">
      <template #list="slotProps">
       
          <div v-for="(item, index) in slotProps.items" :key="index">
            <div class="row">
              <div class="gameDescription">
                {{  item.name }} <br/>
                <Image :src="item.thumbnailUrl" />             
              </div>
              <Button @click="onClickAdd(item)"> Add </Button>                    
            </div>
          </div>
       
      </template>
    </DataView>
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

</style>
