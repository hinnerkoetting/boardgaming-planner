<template>
  <div>
    <Select v-model="type" :options="[{ name: GameGroupMemberType.MEMBER, description: memberTypeToString(GameGroupMemberType.MEMBER) },
    { name: GameGroupMemberType.ADMIN, description: memberTypeToString(GameGroupMemberType.ADMIN) },
    { name: GameGroupMemberType.OWNER, description: memberTypeToString(GameGroupMemberType.OWNER) }]"
      optionLabel="description" optionValue="name" />
    <Button @click="onClickSubmit">Submit</Button>
    <Message v-if="errorMessage" severity="warn">{{ errorMessage }} </Message>
  </div>
</template>

<script setup lang="ts">
import { GameGroupMember, GameGroupMemberType, memberTypeToString } from '@/model/GameGroupMember';
import { updatePlayerMembershipType } from '@/services/api/GameGroupApiService';
import { Button, Message, Select } from 'primevue';
import { ref, type PropType } from 'vue';

const props = defineProps({
  membership: {
    type: Object as PropType<GameGroupMember>,
    required: true
  },
  gameGroupId: {
    type: Number,
    required: true
  }
})

const emits = defineEmits<{
  (e: 'type-changed', type: GameGroupMemberType): void
}>()

const type = ref(props.membership.type)
const errorMessage = ref<string>('')

async function onClickSubmit() {
  if (type.value === GameGroupMemberType.OWNER) {
    errorMessage.value = 'Not allowed!';
    return
  }
  const result = await updatePlayerMembershipType(props.gameGroupId, props.membership.id, type.value)
  if (result.success) {
    emits('type-changed', type.value as GameGroupMemberType)
  } else {
    console.error('Error updating membership type:', result.error?.detail || 'Unknown error')
  }

}

</script>