<template>
  <DataTable :value="players" tableStyle="min-width: 20rem" class="players">
    <Column field="name" header="Name"></Column>
    <Column header="Type">
      <template #body="slotProps">
        {{ memberTypeToString(slotProps.data.type) }}
        <Button @click="editType(slotProps.data)" severity="info"
          v-if="(amIGroupOwner() || amIGroupAdmin()) && slotProps.data.type !== GameGroupMemberType.OWNER">
          Edit
        </Button>
      </template>
    </Column>
    <Column header="Actions" v-if="amIGroupOwner() || amIGroupAdmin()">
      <template #body="slotProps">
        <Button @click="onClickRemovePlayer(slotProps.data.id)" severity="danger"
          v-if="slotProps.data.id !== getCurrentPlayerId() && slotProps.data.type !== GameGroupMemberType.OWNER">
          Remove player
        </Button>
      </template>
    </Column>
  </DataTable>
  <Dialog v-model:visible="dialogVisible">
    <EditMembershipType :membership="selectedPlayer!" :gameGroupId="Number(props.gameGroupId)"
      @type-changed="onTypeChanged" />
  </Dialog>
</template>

<script setup lang="ts">
import { GameGroupMemberType, memberTypeToString, type GameGroupMember } from '@/model/GameGroupMember';
import { removePlayerFromGroup } from '@/services/api/GameGroupApiService';
import { getCurrentPlayerId } from '@/services/LoginService';
import EditMembershipType from '@/views/GameGroup/EditMembershipType.vue';
import { Button, Column, DataTable, Dialog } from 'primevue';
import { ref, type PropType } from 'vue';


const props = defineProps({
  players: {
    type: Object as PropType<GameGroupMember[]>,
    required: true
  },
  gameGroupId: {
    type: Number,
    required: true
  }
})

const emits = defineEmits<{
  (e: 'player-removed', player: GameGroupMember): void
}>()

const players = ref(props.players)
const dialogVisible = ref(false)
const selectedPlayer = ref<GameGroupMember | null>(null)

function editType(player: GameGroupMember) {
  selectedPlayer.value = player
  dialogVisible.value = true
}

function onClickRemovePlayer(playerId: number) {
  removePlayerFromGroup(props.gameGroupId, playerId)
  players.value = players.value.filter((item) => !(item.id === playerId))
  emits('player-removed', props.players.find((p) => p.id === playerId)!)
}

function amIGroupOwner(): boolean {
  return getMyMembershipType() === GameGroupMemberType.OWNER;
}

function amIGroupAdmin(): boolean {
  return getMyMembershipType() === GameGroupMemberType.ADMIN;
}

function getMyMembershipType(): GameGroupMemberType {
  return players.value.find((p) => p.id === getCurrentPlayerId())?.type ?? GameGroupMemberType.MEMBER
}

function onTypeChanged(newValue: GameGroupMemberType) {
  dialogVisible.value = false;
  selectedPlayer.value!.type = newValue
}
</script>