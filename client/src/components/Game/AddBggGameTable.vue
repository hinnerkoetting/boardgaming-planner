<script setup lang="ts">
import Button from 'primevue/button'
import { ref, watch, type PropType, type Ref } from 'vue'
import type { BggSearchItem } from '@/model/BggSearchItem'
import DataView from 'primevue/dataview'
import type { Game } from '@/model/Game'
import type { EventMessage } from '@/model/internal/EventMessage'
import Message from 'primevue/message'
import { importFromBgg } from '@/services/api/BggApiService'
const emit = defineEmits<{
  (e: 'game-added', game: Game, callback: (message: EventMessage) => void): void
}>()

const props = defineProps({
  searchItems: {
    type: Array as PropType<BggSearchItem[]>,
    required: true
  },
  emptyTableMessage: {
    type: String
  }
})

type GameInTable = BggSearchItem & { message: string; severity: string }
const gamesInTable: Ref<GameInTable[]> = ref(props.searchItems as GameInTable[])

watch(
  () => props.searchItems,
  (games: BggSearchItem[]) => {
    gamesInTable.value = games as GameInTable[]
  }
)

async function onClickAdd(game: GameInTable) {
  const fetchedGameResponse = await importFromBgg(game.id)
  if (fetchedGameResponse.success) {
    emit('game-added', fetchedGameResponse.success, (message) => {
      if (message.success) {
        game.message = message.message
        game.severity = 'info'
      } else {
        game.message = message.message
        game.severity = 'error'
      }
    })
  } else {
    game.message = fetchedGameResponse.error?.detail || 'Error'
    game.severity = 'error'
  }
}
</script>

<template>
  <div>
    <DataView :value="gamesInTable" dataKey="id">
      <template #empty>
        <div>{{ emptyTableMessage }}</div></template
      >
      <template #list="slotProps">
        <div v-for="(item, index) in slotProps.items" :key="index">
          <div class="row">
            <div>
              {{ item.name }}
            </div>
            <div class="interaction">
              <Message v-if="item.message" :severity="item.severity"> {{ item.message }} </Message>
              <Button @click="onClickAdd(item)"> Add </Button>
            </div>
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

.interaction {
  margin-left: 4px;
  display: flex;
  align-items: end;
  flex-direction: column;
  gap: 4px;
}
</style>
