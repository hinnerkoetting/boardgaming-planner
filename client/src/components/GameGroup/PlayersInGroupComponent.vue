<template>
  <DataTable :value="players" tableStyle="min-width: 20rem" class="players">
    <Column field="name" header="Name"></Column>
    <Column header="Type">
      <template #body="slotProps">
        {{ memberTypeToString(slotProps.data.type) }}
      </template>
    </Column>
  </DataTable>
</template>

<script setup lang="ts">
import { memberTypeToString, type GameGroupMember } from '@/model/GameGroupMember';
import { Column, DataTable } from 'primevue';
import type { PropType } from 'vue';


defineProps({
  players: {
    type: Object as PropType<GameGroupMember[]>,
    required: true
  }
})
</script>