<script setup lang="ts">
import Button from 'primevue/button'
import { ref, watch, type PropType, type Ref } from 'vue'
import type { Game } from '@/model/Game'
import Image from 'primevue/image'
import DataView from 'primevue/dataview'
import { EventMessage } from '@/model/internal/EventMessage'
import Message from 'primevue/message'

const props = defineProps({
  searchItems: {
    type: Array as PropType<Game[]>,
    required: true
  }
})

type GameInTable = Game & { message: string; severity: string }
const gamesInTable: Ref<GameInTable[]> = ref(props.searchItems as GameInTable[])

watch(
  () => props.searchItems,
  (games: Game[]) => {
    gamesInTable.value = games as GameInTable[]
  }
)

const emit = defineEmits<{
  (e: 'game-added', game: Game, callback: (message: EventMessage) => void): void
}>()

async function onClickAdd(game: GameInTable) {
  emit('game-added', game, (message) => {
    if (message.success) {
      game.message = message.message
      game.severity = 'info'
    } else {
      game.message = message.message
      game.severity = 'error'
    }
  })
}
</script>

<template>
  <div>
    <DataView :value="gamesInTable">
      <template #empty> <div></div></template>
      <template #list="slotProps">
        <div v-for="(item, index) in slotProps.items" :key="index">
          <div class="row">
            <div class="gameDescription">
              {{ item.name }} <br />
              <Image :src="item.thumbnailUrl" />
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

<style scoped lang="css">
.gameDescription {
  display: flex;
  align-items: center;
  flex-direction: column;
  gap: 4px;
}
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

.interaction {
  margin-left: 4px;
  display: flex;
  align-items: end;
  flex-direction: column;
  gap: 4px;
}
</style>
