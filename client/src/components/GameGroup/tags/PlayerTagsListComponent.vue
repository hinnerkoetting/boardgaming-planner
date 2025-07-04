<template>  
    
  <table>
    <tr>
      <th></th>
      <th v-for="(player, index) in props.players" :key="index">
        {{ player.name }}
      </th>
    </tr>    

    <tr v-for="(item, index) in playerTagsDisplay" :key="index">        
          <td>
            {{ item.description}}
          </td>
          <td v-for="(playerTag, index) in item.players" :key="index">            
            <i v-if="playerTag.selected" class="pi pi-heart-fill" style="color: red"></i>            
          </td>        
      </tr>          
    
  </table>
</template>

<script setup lang="ts">
import type { PlayerTagInGameGroup } from '@/model/Game';
import type { Player } from '@/model/Player/Player';
import type { TagModel } from '@/model/TagModel';
import { loadTags } from '@/services/StoreApiService';
import { onMounted, ref, type PropType, type Ref } from 'vue';
import 'primeicons/primeicons.css'


const props = defineProps({
  playerTags: {
    type: Array as PropType<PlayerTagInGameGroup[]>,
    required: true
  },
  players: {
    type: Array as PropType<Player[]>,
    required: true
  }
})

const playerTagsDisplay: Ref<PlayerTagRow[]> = ref([])

onMounted(async () => {

  const allPlayerTags: TagModel[] = (await loadTags()).filter(t => t.type === 'PLAYER')

  playerTagsDisplay.value = allPlayerTags.map(t => new PlayerTagRow(t.description, t.id, mapPlayerSelection(t)))
  
});

function mapPlayerSelection(tag: TagModel): PlayerTagSelection[] {
  return props.players.map(player => {
    const playerTag = props.playerTags.some(playerTag => playerTag.playerId === player.id && playerTag.id === tag.id)
    return new PlayerTagSelection(player.id, playerTag)
  })  
}

</script> 

<script lang="ts">
export class PlayerTagRow {
  constructor(
    readonly description: string,
    readonly id: number,
    readonly players: PlayerTagSelection[]
  ) {}
}

export class PlayerTagSelection {
  constructor(
    readonly playerId: number,
    readonly selected: boolean
  ) {}
}
</script>

<style lang="css" scoped>
th, td {
  border-bottom: 1px solid #ddd;
}
table {
  border-spacing: 10px;
}
</style>